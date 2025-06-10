package cc.mrbird.febs.hope.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.hope.domain.DownloadInfo;
import cc.mrbird.febs.hope.service.DownloadInfoService;
import cc.mrbird.febs.hope.utils.Constant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fanta
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("downloadInfo")
@Api(value = "/downloadInfo",tags={"下载管理"})
public class DownloadInfoController extends BaseController {
    private String message;

    @Autowired
    private DownloadInfoService downloadInfoService;

    @GetMapping("/queryList")
    @ApiOperation(value="下载管理-分页查询接口",notes = "pageNum* 必传\n" +
            "pageSize* 必传\n" +
            "engineName 【引擎名称】\n" +
            "downloadName【下载名称】\n" +
            "downloadType【下载类型】\n" +
            "sortField传字段名称（如：createTime）\n" +
            "sortOrder：（倒序传参：descend，正序传参：ascend）")
    public Map<String, Object> queryList(QueryRequest request,DownloadInfo info) {
        return getDataTable(downloadInfoService.queryList(info,request));
    }

    @GetMapping("/queryDownloadInfoByChannelName")
    @ApiOperation(value="下载管理-已关下载查询接口",notes = "channelName* 必传" )
    public Map<String, Object> queryDownloadInfoByChannelName(String channelName) {
        try {
            List<Map<String, Object>> maps = downloadInfoService.queryDownloadInfoByChannelName(channelName);
            return new FebsResponse().data(maps).message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e){
            message="查询失败\n"+e.getMessage();
            log.error(message,e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }
    }



    @ApiOperation(value="下载管理-新增接口",notes = "POST请求，CreateTime,ModifyTime不传" )
    @PostMapping("/add")
    public FebsResponse addDownloadInfo(DownloadInfo info) {
        try {
            int count = this.downloadInfoService.count(new LambdaQueryWrapper<DownloadInfo>().eq(DownloadInfo::getDownloadName, info.getDownloadName()));
            if (count !=0){
                message="downloadName重复，请换一个";
                return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
            }
            info.setCreateTime(new Date());
            info.setModifyTime(new Date());
            info.setDownloadStart("1");
            this.downloadInfoService.save(info);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "新增下载信息失败\n"+e.getMessage();
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);

        }
    }
    @ApiOperation(value="下载管理-修改接口",notes = "downloadId* 必传，其他修改项" )
    @PostMapping("/update")
    public FebsResponse updateDownloadInfo(DownloadInfo info){
        if(info.getDownloadId() == null){
            message="参数不合法,且downloadId必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
        }
        try {
            info.setDownloadName(null);
            info.setModifyTime(new Date());
            this.downloadInfoService.updateById(info);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "修改下载信息失败\n"+e.getMessage();
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }
    }

    @ApiOperation(value="下载管理-删除接口",notes = "downloadIds 【String类型】\n" +
            "一个或多个channelId\n" +
            "注意：多个值以，分隔" )
    @PostMapping("/{downloadIds}")
    public FebsResponse deleteDownloadInfos(@NotBlank(message = "{required}") @PathVariable String downloadIds){
        try {
            String[] ids = downloadIds.split(StringPool.COMMA);
            this.downloadInfoService.removeByIds(Arrays.asList(ids));
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "删除下载信息失败\n"+e.getMessage();
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);

        }
    }


    @GetMapping("/selectDetail")
    @ApiOperation(value="下载管理-详情接口",notes = "downloadId* 必传" )
    public FebsResponse selectDetail(DownloadInfo info) throws FebsException{
        try{
            QueryWrapper<DownloadInfo> cqw = new QueryWrapper<>();
            cqw.setEntity(info);
            return new FebsResponse().data(this.downloadInfoService.getOne(cqw)).message(Constant.MESSAGE_SUCCESS);
        }catch (Exception e){
            message="查询信息失败\n "+e.getMessage();
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }



    @GetMapping("/statistics")
    @ApiOperation(value="下载管理-下载统计接口" )
    public FebsResponse statistics() throws FebsException{
        try{
            return new FebsResponse().data(this.downloadInfoService.statistics()).message(Constant.MESSAGE_SUCCESS);
        }catch (Exception e){
            message="查询信息失败\n "+e.getMessage();
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }


}
