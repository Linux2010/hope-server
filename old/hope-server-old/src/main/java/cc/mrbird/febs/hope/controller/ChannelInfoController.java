package cc.mrbird.febs.hope.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.hope.domain.ChannelInfo;
import cc.mrbird.febs.hope.service.ChannelInfoService;
import cc.mrbird.febs.hope.utils.Constant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
@RequestMapping("channelInfo")
@Api(value = "/channelInfo",tags={"频道管理"})
public class ChannelInfoController extends BaseController {
    private String message;
    @Autowired
    private  ChannelInfoService channelInfoService;


    @ApiOperation(value="频道管理-分页查询接口",notes = "pageNum* 必传\n" +
            "pageSize* 必传\n" +
            "channelName 频道名称\n" +
            "channelType 频道类型 \n " +
            "sortField传字段名称（如：createTime）\n" +
            "sortOrder：（倒序传参：descend，正序传参：ascend）")
    @GetMapping("/queryList")
    public Map<String, Object> queryList(QueryRequest request,ChannelInfo channelInfo) {
        return getDataTable(channelInfoService.queryList(channelInfo,request));
    }

    @ApiOperation(value="频道管理-已关联频道查询接口",notes = "downloadName* 必传" )
    @GetMapping("/queryChannelInfoByDownloadName")
    public FebsResponse queryChannelInfoByDownloadName(String downloadName) {
        try {
            List<Map<String, Object>> maps = channelInfoService.queryChannelInfoByDownloadName(downloadName);
            FebsResponse febsResponse = new FebsResponse();
            febsResponse.data(maps).message(Constant.MESSAGE_SUCCESS);
            return febsResponse;
        } catch (Exception e){
            log.error(e.getMessage());
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",e.getMessage());
        }
    }


    @ApiOperation(value="频道管理-新增接口",notes = "POST请求，全部参数* 必传" )
    @PostMapping("/add")
    public FebsResponse addChannel(ChannelInfo channelInfo) {
        if(StringUtils.isBlank(channelInfo.getChannelName())){
            message="参数不合法,channelName必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
        }
        try {
            int count = this.channelInfoService.count(new LambdaQueryWrapper<ChannelInfo>().eq(ChannelInfo::getChannelName, channelInfo.getChannelName()));
            if (count !=0){
                message="channelName重复，请换一个";
                return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
            }
            boolean b = this.channelInfoService.checkChannelName(channelInfo.getChannelName());
            if(!b){
                message="频道名称[channelName]需唯一，不可以重复";
                return new FebsResponse().message(message);
            }
            channelInfo.setCreateTime(new Date());
            channelInfo.setModifyTime(new Date());
            this.channelInfoService.save(channelInfo);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",e.getMessage());
        }
    }

    @ApiOperation(value="频道管理-修改接口",notes = "Post请求，channelId* 必传" )
    @PostMapping("/update")
    public FebsResponse updateChannelInfo(ChannelInfo channelInfo){
        if(channelInfo.getChannelId() == null){
            message="参数不合法,channelId必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
        }

        try {
            channelInfo.setChannelName(null);
            channelInfo.setModifyTime(new Date());
            this.channelInfoService.updateById(channelInfo);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",e.getMessage());

        }
    }

    @ApiOperation(value="频道管理-删除接口",notes = "DELETE请求，String类型\n" +
            "一个或多个channelId\n" +
            "注意：多个值以，分隔" )
    @PostMapping("/{channelIds}")
    public FebsResponse deleteChannels(@NotBlank(message = "{required}") @PathVariable String channelIds) {
        try {
            String[] ids = channelIds.split(StringPool.COMMA);
            this.channelInfoService.removeByIds(Arrays.asList(ids));
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "发生异常，删除频道失败\n"+e.getMessage();
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }
    }

    @ApiOperation(value="频道管理-启动后台实例",notes = "channelName，String类型")
    @PostMapping("/openBack/{channelType}/{channelName}")
    public FebsResponse openBack(@NotBlank(message = "{required}") @PathVariable String channelType,@NotBlank(message = "{required}") @PathVariable String channelName) {

        try {
            channelInfoService.openBack(channelType,channelName);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "发生异常，启动后台实例失败\n"+e.getMessage();
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }
    @ApiOperation(value="频道管理-刷新cookies" )
    @PostMapping("/refresh/{channelType}/{channelName}")
    public FebsResponse refresh(@NotBlank(message = "{required}") @PathVariable String channelType,@NotBlank(message = "{required}") @PathVariable String channelName) {
        try{
            this.channelInfoService.refresh(channelName,channelType);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        }catch (Exception e){
            message="执行失败: "+e.getMessage();
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }

    @ApiOperation(value="频道管理-详情接口",notes = "channelId* 必传" )
    @GetMapping("/selectDetail")
    public FebsResponse selectDetail(ChannelInfo info) throws FebsException{
        try{
            QueryWrapper<ChannelInfo> cqw = new QueryWrapper<>();
            cqw.setEntity(info);
            return new FebsResponse().data(this.channelInfoService.getOne(cqw)).message(Constant.MESSAGE_SUCCESS);
        }catch (Exception e){
            message="查询信息失败: "+e.getMessage();
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }

    @ApiOperation(value="频道管理-频道统计接口" )
    @GetMapping("/statistics")
    public FebsResponse statistics() throws FebsException{
        try{
            return new FebsResponse().data(this.channelInfoService.statistics()).message(Constant.MESSAGE_SUCCESS);
        }catch (Exception e){
            message="查询信息失败: "+e.getMessage();
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }



}
