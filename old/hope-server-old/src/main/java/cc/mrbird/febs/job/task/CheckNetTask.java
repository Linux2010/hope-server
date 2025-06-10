package cc.mrbird.febs.job.task;

import cc.mrbird.febs.hope.domain.*;
import cc.mrbird.febs.hope.service.ChannelInfoService;
import cc.mrbird.febs.hope.service.EngineInfoService;
import cc.mrbird.febs.hope.utils.SSH2Util;
import cc.mrbird.febs.job.service.WechatAlertService;
import cc.mrbird.febs.job.service.impl.WechatAlertServiceImpl;
import cc.mrbird.febs.job.util.OkHttpClientUtils;
import ch.ethz.ssh2.Connection;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component

public class CheckNetTask {

    @Autowired
    private EngineInfoService service;
    @Autowired
    private ChannelInfoService channelInfoService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EngineInfoService engineInfoService;

    @Autowired
    private WechatAlertService wechatAlertService;


    @Value("${spring.mail.username}")
    private String sourceEmail;

    @Value("${hope.machine-room-id}")
    private String machineRoomId;


    @Value("${hope.wechatKey}")
    private String wechatKey;





    @Transactional(rollbackFor = { Exception.class })
    public void checkNetTaskByEmail(String param){
        //String cmd = "sh hope-install/hope-engine/shell/translate.sh hope zh-CN auto";
        String cmd = "sh hope-install/hope-engine/shell/check-youtube.sh";
        log.info("checkNetTask,定时检查,参数为：{}" , param);
        LambdaQueryWrapper<EngineInfo> cqw = new LambdaQueryWrapper<>();
        cqw.eq(EngineInfo::getMachineRoomId,machineRoomId);
        List<EngineInfo> list = service.list(cqw);
        AtomicBoolean successFlag = new AtomicBoolean(true);
        list.forEach(info->{
            boolean sendEmailFlag = false;
            StringBuilder emailContext = new StringBuilder();
            EngineInfo engineInfo = new EngineInfo();
            try {
                Connection conn = SSH2Util.openConnection(info.getEngineIp(), info.getUsername(), info.getPassword());
                log.info("获取Connection,成功...engineInfo:[{}]" ,info);
                boolean res = SSH2Util.execCmdWithStatus(conn, cmd);
                conn.close();
                if(!res){
                    log.info("[{}] [{}] vpn conn 失效,即将发送告警!",info.getEngineName(),info.getEngineIp());
                    sendEmailFlag = true;
                    successFlag.set(false);
                    emailContext.append(String.format("[%s] [%s] vpn conn 失效,请尽快处理！", info.getEngineName(),info.getEngineIp()));
                }else {
                    engineInfo.setEngineId(info.getEngineId());
                    engineInfo.setEngineNetStatus("true");
                    engineInfoService.updateById(engineInfo);
                }
            }catch (Exception e){
                log.info("[{}] [{}] checkNet 出现未知错误,请尽快处理,错误信息:[{}]",info.getEngineName(),info.getEngineIp(),e.getMessage());
                sendEmailFlag = true;
                emailContext.append(String.format("[%s] [%s] checkNet 出现未知错误,请尽快处理,错误信息:[%s]", info.getEngineName(),info.getEngineIp(),e.getMessage()));
            }
            if(sendEmailFlag){
                log.info("send email to [{}]",param);
                engineInfo.setEngineId(info.getEngineId());
                engineInfo.setEngineNetStatus("false");
                engineInfoService.updateById(engineInfo);
                wechatAlertService.sendAsText(wechatKey,String.format("[hope] [%s] net conn 预警!\n", info.getEngineName())+emailContext);
                this.sendMail(param,emailContext.toString(), String.format("[hope] [%s] net conn 预警!", info.getEngineName()));
            }
        });
        if(successFlag.get()){
            log.info("checkNet success");
            wechatAlertService.sendAsText(wechatKey,"[hope] checkNet All success!");
            this.sendMail(param,"", "[hope] checkNet All success!");
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
    public void checkCookieTaskByEmail(String param){
        log.info("checkCookieTask,定时检查cookie,参数为：{}" , param);
        LambdaQueryWrapper<ChannelInfo> cqw = new LambdaQueryWrapper<>();
        cqw.eq(ChannelInfo::getCookieEnable,"false").eq(ChannelInfo::getChannelOffOn,"on");
        List<ChannelInfo> list = channelInfoService.list(cqw);
        if(list!=null&& !list.isEmpty()){
            StringBuffer buffer = new StringBuffer();
            list.forEach(info->{
                buffer.append(info.getChannelType());
                buffer.append(" --> ");
                buffer.append(info.getChannelName());
                buffer.append("\n");
            });
            log.info("send email to [{}]",param);
            wechatAlertService.sendAsText(wechatKey,"[hope] cookie 预警!\n"+buffer);
            this.sendMail(param,buffer.toString(), "[hope] cookie 预警!");
        }else {
            log.info("checkCookieTask,检查cookie正常,无预警内容...");
        }

    }

}
