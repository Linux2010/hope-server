package cc.mrbird.febs.job.task;

import cc.mrbird.febs.hope.domain.*;
import cc.mrbird.febs.hope.service.*;
import cc.mrbird.febs.hope.utils.*;
import cc.mrbird.febs.job.service.WechatAlertService;
import cc.mrbird.febs.system.domain.Dict;
import ch.ethz.ssh2.Connection;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ Desc hope的task任务
 */
@Slf4j
@Component
public class HopeTask {
    @Autowired
    private ChannelInfoService channelInfoService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EngineInfoService engineInfoService;

    @Autowired
    private DownloadInfoService downloadInfoService;

    @Autowired
    private DownloadInstanceService downloadInstanceService;

    @Autowired
    private TransService transService;


    @Value("${spring.mail.username}")
    private String sourceEmail;

    @Value("${hope.machine-room-id}")
    private String machineRoomId;

    @Autowired
    private HardDiskStorageService hardDiskStorageService;

    @Value("${hope.wechatKey}")
    private String wechatKey;

    @Autowired
    private WechatAlertService wechatAlertService;

    public  static  final  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Transactional(rollbackFor = { Exception.class })
    public void checkChannelDue(String param){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, +7);
        Date sevenDaysAfter = calendar.getTime();
        log.info("checkChannelDue,定时task,参数为：{}" , param);
        LambdaQueryWrapper<ChannelInfo> cqw = new LambdaQueryWrapper<>();
        cqw.ne(ChannelInfo::getGainStatus,"2")
                .le(ChannelInfo::getAccDue,sdf.format(sevenDaysAfter.getTime()));
        List<ChannelInfo> list = channelInfoService.list(cqw);
        if(list!=null&& !list.isEmpty()){
            StringBuffer buffer = new StringBuffer();
            list.forEach(info->{
                buffer.append(info.getChannelType());
                buffer.append(" --> ");
                buffer.append(info.getChannelName());
                buffer.append(" --> ");
                buffer.append(sdf.format(info.getAccDue()));
                buffer.append("\n");
            });
            log.info("send email to [{}]",param);
            wechatAlertService.sendAsText(wechatKey,"[hope] 账号过期acc_due预警!\n"+buffer);
            this.sendMail(param,buffer.toString(), "[hope] 账号过期acc_due预警!");
        }else {
            log.info("checkChannelDue,账号过期acc_due正常,无预警内容...");
        }
    }


    public void sendMail(String targetEmails,String context,String title){
        SimpleMailMessage message = new SimpleMailMessage();
        String[] targetEmailArray = targetEmails.split(",");
        message.setFrom(sourceEmail);
        message.setTo(targetEmailArray);
        message.setSubject(title);
        message.setText(context);
        mailSender.send(message);
    }


    @Transactional(rollbackFor = { Exception.class })
    public void updateHardDisk(String param){
        String cmdTmp = "df -hl %s | awk 'NR==2 { print $5 }'";
        log.info("updateHardDisk,定时更新和检查,参数为：{}" , param);

        List<HardDiskStorage> list = hardDiskStorageService.queryInfo();
        list.forEach(info->{
            EngineInfo engineInfo = engineInfoService.getById(info.getEngineId());
            try {
                Connection conn = SSH2Util.openConnection(engineInfo.getEngineIp(), engineInfo.getUsername(), engineInfo.getPassword());
                log.info("获取Connection,成功...engineInfo:[{}]" ,info);
                String cmd = String.format(cmdTmp, info.getStoragePath());
                ExecCmdResult res = SSH2Util.execCommand(conn, cmd);
                conn.close();
                if (res != null && res.getFlag() && StringUtils.isNotEmpty(res.getResult())) {
                    //update
                    info.setUsedPercentage(res.getResult());
                    info.setModifyTime(new Timestamp(System.currentTimeMillis()));
                    hardDiskStorageService.updateById(info);
                }else {
                    log.error("updateHardDisk , cmd:[{}], result:[{}]",cmd, Objects.requireNonNull(res));
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        });
    }


    public void updateDownloadInfo(String param){
        String cmdTmp = "du -sh %s";
        log.info("updateDownloadInfo,定时更新文件夹大小：{}" , param);
        int pageSize = 10;
        int currentPage = 1;
        boolean hasNextPage = true;
        LambdaQueryWrapper<DownloadInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DownloadInfo::getDownloadEnable, "true");
        while (hasNextPage) {
            Page<DownloadInfo> page = new Page<>(currentPage, pageSize);
            Page<DownloadInfo> result = downloadInfoService.page(page, queryWrapper);
            List<DownloadInfo> list = result.getRecords();
            for (DownloadInfo info : list) {
                String res = ShellUtils.execCmdWithResInfo(String.format(cmdTmp,info.getDownloadPath()));
                // fixme 针对删除过的是否需要更新为0B（无文件夹类型）
                if(StringUtils.isNotEmpty(res)){
                    DownloadInfo up = new DownloadInfo();
                    up.setDownloadId(info.getDownloadId());
                    up.setDiskStorageInfo(res.split("\t")[0]);
                    downloadInfoService.updateById(up);
                }
            }
            if (currentPage < result.getPages()) {
                currentPage++;
            } else {
                hasNextPage = false;
            }
        }
    }



    public void downloadInstanceInsert(String param){
        log.info("[视频入库] threadName:[{}],程序运行开始...",Thread.currentThread().getName());
        try {
            log.info("[视频入库] 程序运行开始--------");
            int pageSize = 10;
            int currentPage = 1;
            boolean hasNextPage = true;
            LambdaQueryWrapper<DownloadInfo> pageQuery = new LambdaQueryWrapper<>();
            pageQuery.eq(DownloadInfo::getDownloadEnable, "true");
            while (hasNextPage) {
                Page<DownloadInfo> page = new Page<>(currentPage, pageSize);
                Page<DownloadInfo> result = downloadInfoService.page(page, pageQuery);
                List<DownloadInfo> list = result.getRecords();
                list.forEach(downloadInfo->{
                    log.info("[{}] 视频入库程序执行开始! ",downloadInfo.getDownloadName());
                    List<VideoInfo> videoInfoList = HopeFileUtils.getVideoInfoList(downloadInfo.getDownloadPath(), 0);
                    log.info("[{}] [{}] 本次获取size大于[{}]的视频共计:[{}]个",downloadInfo.getDownloadName(),downloadInfo.getDownloadPath(),0,videoInfoList.size());
                    //1，入库非0 size的视频文件
                    videoInfoList.forEach(vi->{
                        try {
                            String pk = MD5Util.md5(vi.getTitle());
                            LambdaQueryWrapper<DownloadInstance> queryWrapper = new LambdaQueryWrapper<>();
                            queryWrapper.eq(DownloadInstance::getPkInstanceId, pk);
                            int count = downloadInstanceService.count(queryWrapper);
                            if(count>0){
                                log.info("[{}] 视频入库 [{}]，检查已经入库",downloadInfo.getDownloadName(),vi.getTitle());
                            }
                            else {
                                DownloadInstance instance = new DownloadInstance();
                                if("youtube".equals(downloadInfo.getDownloadType())){//youtube-->国内频道，翻译为中文
                                    String cnTitle = transService.azureTrans(vi.getTitle().replaceAll("_", " ").replace("#", ""),"zh-Hans");
                                    //azure翻译失败，尝试使用google翻译
                                    if(cnTitle ==null){
                                        try {
                                            cnTitle = GTShellUtils.translateText(vi.getTitle(), "zh-CN", "auto");
                                        } catch (Exception e) {
                                            log.error("[{}] 视频入库 [{}]，翻译标题失败，error:[{}]",downloadInfo.getDownloadName(),vi.getTitle(),e.getMessage());
                                        }
                                    }
                                    if(cnTitle!=null){
                                        instance.setTranslationTitle(cnTitle.replaceAll("\n","").replaceAll(" ", "_"));//处理换行
                                    }
                                }
                                //fixme 暂时不翻译英语，使用懒加载模式
//                                else { //国内下载内容-->youtube，翻译为英文备用
//                                    String cnTitle = transService.azureTrans(vi.getTitle(),"en");
//                                    //azure翻译失败，尝试使用google翻译
//                                    if(cnTitle ==null){
//                                        try {
//                                            cnTitle = GTShellUtils.translateText(vi.getTitle(), "en", "auto");
//                                        } catch (Exception e) {
//                                            log.error("[{}] 视频入库 [{}]，翻译标题失败，error:[{}]",downloadInfo.getDownloadName(),vi.getTitle(),e.getMessage());
//                                        }
//                                    }
//                                    if(cnTitle!=null){
//                                        instance.setTranslationTitle(cnTitle.replaceAll("\n","").replaceAll(" ", "_"));//处理换行
//                                    }
//                                }
                                instance.setPkInstanceId(pk);
                                instance.setDownloadId(downloadInfo.getDownloadId());
                                instance.setCreateTime(new Date());
                                instance.setFileCreateTime(HopeFileUtils.getFileCreateDate(new File(vi.getPath())));
                                instance.setCleanFlag(2);
                                instance.setVideoFile(vi.getFileName());
                                instance.setVideoName(vi.getTitle());
                                instance.setVideoPath(vi.getPath());
                                instance.setVideoType(vi.getFileType());
                                instance.setVideoSize(String.valueOf(HopeFileUtils.getFileSize(vi.getPath())));
                                downloadInstanceService.save(instance);
                            }
                        } catch (Exception e) {
                            log.error("[{}] ，视频入库报错 :[{}],error:[{}]",downloadInfo.getDownloadName(),vi.getPath(),e.getMessage());
                        }
                    });
                });
                if (currentPage < result.getPages()) {
                    currentPage++;
                } else {
                    hasNextPage = false;
                }
            }

            log.info("[视频入库] , 程序运行结束--------");
        }  catch (Exception e) {
            log.error("[视频入库]程序运行出错 error message:[{}]",e.getMessage(),e);
        }
    }

}
