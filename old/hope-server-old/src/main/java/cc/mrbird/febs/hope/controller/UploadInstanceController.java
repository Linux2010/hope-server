package cc.mrbird.febs.hope.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.hope.domain.UploadInstance;
import cc.mrbird.febs.hope.service.UploadInstanceService;
import cc.mrbird.febs.hope.utils.Constant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author fanta
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("uploadInstance")
@Api(value = "/uploadInstance",tags={"实例管理"})
public class UploadInstanceController extends BaseController {
    private String message;

    @Autowired
    private UploadInstanceService service;


    @ApiOperation(value="实例管理-分页查询接口",notes = "pageNum* 必传\n" +
            "pageSize* 必传\n" +
            "channelName 频道名称\n" +
            "channelType 频道类型 \n " +
            "sortField传字段名称（如：createTime）\n" +
            "sortOrder：（倒序传参：descend，正序传参：ascend）")
    @GetMapping("/queryList")
    public Map<String, Object> queryList(QueryRequest request, UploadInstance info) {
        return getDataTable(service.queryList(info,request));
    }



    @PostMapping("/add")
    @ApiOperation(value="实例管理-新增接口",notes = "POST请求，全部参数* 必传" )
    public FebsResponse addUploadInstance( UploadInstance info) {
        if(StringUtils.isBlank(info.getInstanceId())){
            message="参数不合法,instanceId必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
        }
        try {
            info.setCreateTime(new Date());
            info.setModifyTime(new Date());
            this.service.save(info);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "新增失败";
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);

        }
    }

    @PostMapping("/update")
    @RequiresPermissions("uploadInstance:update")
    @ApiOperation(value="实例管理-修改接口",notes = "PUT请求，uploadId* 必传 + 其他修改项" )
    public FebsResponse updateUploadInstance(UploadInstance info)  {
        if(StringUtils.isBlank(info.getInstanceId())){
            message="参数不合法,instanceId必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
        }
        try {
            info.setModifyTime(new Date());
            this.service.updateById(info);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);

        }
    }

    @PostMapping("/{instanceIds}")
    @RequiresPermissions("uploadInstance:delete")
    @ApiOperation(value="实例管理-删除接口",notes = "String类型\n" +
            "一个或多个uploadId\n" +
            "注意：多个值以，分隔  示例：id1，id2，di3" )
    public FebsResponse deleteInstances(@NotBlank(message = "{required}") @PathVariable String instanceIds) {
        try {
            String[] ids = instanceIds.split(StringPool.COMMA);
            this.service.removeByIds(Arrays.asList(ids));
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);

        }
    }


    @GetMapping("/selectDetail")
    @ApiOperation(value="实例管理-详情接口",notes = "instanceId* 必传" )
    public FebsResponse selectDetail(UploadInstance info) throws FebsException{
        if(StringUtils.isBlank(info.getInstanceId())){
            message="参数不合法,instanceId必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
        }
        try{
            QueryWrapper<UploadInstance> cqw = new QueryWrapper<>();
            cqw.setEntity(info);
            return new FebsResponse().data(this.service.getOne(cqw)).message(Constant.MESSAGE_SUCCESS);
        }catch (Exception e){
            message="查询信息失败: "+e.getMessage();
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }

    @GetMapping("/queryTrend")
    @ApiOperation(value="实例管理-上传趋势图接口",notes = "channelName 频道名称\n" +
            "channelType 频道类型 *必传" )
    public FebsResponse queryTrend(UploadInstance info) throws FebsException{
        if(StringUtils.isBlank(info.getChannelType())){
            message="参数不合法,channelType必传";
            log.info(message);
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
        }
        try{
            log.info("实例管理-上传趋势图接口,查询成功.");
            return new FebsResponse().data(this.service.queryTrend(info)).message(Constant.MESSAGE_SUCCESS);
        }catch (Exception e){
            message="查询信息失败: "+e.getMessage();
            log.error(message);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }

    @GetMapping("/queryFailList")
    @ApiOperation(value="实例管理-失败排行列表接口",notes = "pageNum* 必传\n" +
            "pageSize* 必传\n" +
            "channelName 频道名称\n" +
            "channelType 频道类型\n"+
            "sortField传字段名称（如：createTime）\n" +
            "sortOrder：（倒序传参：descend，正序传参：ascend）")
    public Map<String, Object> queryFailList(QueryRequest request,UploadInstance info) throws FebsException{
        return getDataTable(service.queryFailList(request,info));
    }



    @PostMapping("/queryFailLog")
    @ApiOperation(value="实例管理-失败排行日志接口",notes = "engineName 引擎名称 * 必传\n" +
            "videoPath 下载路径 * 必传" )
    @ResponseBody
    public FebsResponse queryFailLog(@RequestBody UploadInstance  ui){

        try{
            if(StringUtils.isBlank(ui.getEngineName()) || StringUtils.isBlank(ui.getVideoPath())){
                message="参数不合法,engineName或videoPath传参为空";
                return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
            }
            return new FebsResponse().data(this.service.queryFailLog(ui.getEngineName(),ui.getVideoPath())).message(Constant.MESSAGE_SUCCESS);
        }catch (Exception e){
            message="查询信息失败: "+e.getMessage();
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }

    @PostMapping("/deleteInstance")
    @ApiOperation(value="实例管理-失败排行删除接口",notes = "engineName 引擎名称 * 必传\n" +
            "videoPath 下载路径 * 必传" )
    public FebsResponse deleteInstance(@RequestBody UploadInstance  ui){
        try{
            if(StringUtils.isBlank(ui.getEngineName()) || StringUtils.isBlank(ui.getVideoPath())){
                message="参数不合法,engineName或videoPath传参为空";
                return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
            }
            this.service.deleteInstance(ui.getEngineName(),ui.getVideoPath());
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        }catch (Exception e){
            message="查询信息失败: "+e.getMessage();
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }


    @PostMapping("/delVideoAndUpInstance")
    @ApiOperation(value="实例管理-删除视频并更新实例为失败接口",notes = "uploadInstance={id,engineName,videoPath}" )
    @Transactional(rollbackFor = Exception.class)
    public FebsResponse delVideoAndUpInstance(@RequestBody UploadInstance  ui) {
        try {
            ui.setStatus("1");
            ui.setModifyTime(new Date(System.currentTimeMillis()));
            this.service.updateById(ui);
            this.service.delVideoByInstance(ui.getEngineName(),ui.getVideoPath());
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "delVideoAndUpInstance,执行失败";
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);

        }
    }


}
