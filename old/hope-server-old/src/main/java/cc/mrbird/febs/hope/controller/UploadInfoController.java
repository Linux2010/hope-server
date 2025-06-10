package cc.mrbird.febs.hope.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.hope.domain.UploadInfo;
import cc.mrbird.febs.hope.service.UploadInfoService;
import cc.mrbird.febs.hope.utils.Constant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.Map;

/**
 * @author fanta
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("uploadInfo")
@Api(value = "/uploadInfo",tags={"上传管理"})
public class UploadInfoController extends BaseController {
    private String message;

    @Autowired
    private UploadInfoService service;


    @GetMapping("/queryList")
    @ApiOperation(value="上传管理-分页查询接口",notes = "pageNum* 必传\n" +
            "pageSize* 必传\n" +
            "channelType【频道类型】\n" +
            "downloadType【下载类型】\n" +
            "sortField传字段名称（如：createTime）\n" +
            "sortOrder：（倒序传参：descend，正序传参：ascend）")
    public Map<String, Object> list(QueryRequest request, UploadInfo info) {
        Page<UploadInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        QueryWrapper<UploadInfo> cqw = new QueryWrapper<>();
        cqw.setEntity(info);
        return getDataTable(service.queryList(info, request));
    }



    @PostMapping("/add")
    @ApiOperation(value="上传管理-新增接口",notes = "POST请求，全部参数* 必传，（uploadName拼接逻辑：channelName_downloadName）" )
    public FebsResponse addUploadInfo(UploadInfo info) {
        if(StringUtils.isBlank(info.getChannelName()) || StringUtils.isBlank(info.getDownloadName()) || StringUtils.isBlank(info.getChannelType()) ){
            message="参数不合法,channelName 、 downloadName、channelType 必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
        }
        try {
            String uploadName = info.getChannelName() + "_" + info.getDownloadName();
            int count = this.service.count(new LambdaQueryWrapper<UploadInfo>().eq(UploadInfo::getUploadName,uploadName));
            if (count !=0){
                message= String.format("uploadName重复，下载项[%s]已关联频道[%s]",info.getDownloadName(),info.getChannelName());
                log.info("uploadInfo add接口:[{}]",message);
                return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
            }
            info.setUploadName(uploadName);
            info.setCreateTime(new Date());
            info.setModifyTime(new Date());
            boolean b = this.service.save(info);
            if(!b){
                message = "保存失败,联系管理员!";
                return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
            }
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "新增失败\n"+e.getMessage();
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }
    }

    @PostMapping("/update")
    @ApiOperation(value="上传管理-修改接口",notes = "PUT请求，uploadId* 必传 + 其他修改项" )
    public FebsResponse updateUploadInfo( UploadInfo info){
        if(info.getUploadId() == null){
            message="参数不合法,downloadId必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
        }
        try {
            if(StringUtils.isNotBlank(info.getChannelName())&&StringUtils.isNotBlank(info.getDownloadName())){
                String uploadName = info.getChannelName() + "_" + info.getDownloadName();
                info.setUploadName(uploadName);
            }
            info.setModifyTime(new Date());
            this.service.updateById(info);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "修改失败";
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }
    }

    @PostMapping("/{uploadIds}")
    @ApiOperation(value="上传管理-删除接口",notes = "String类型\n" +
            "一个或多个uploadId\n" +
            "注意：多个值以，分隔  示例：id1，id2，di3" )
    public FebsResponse deleteUploads(@NotBlank(message = "{required}") @PathVariable String uploadIds){
        try {
            String[] ids = uploadIds.split(StringPool.COMMA);
            this.service.removeByIds(Arrays.asList(ids));
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);

        }
    }

    @GetMapping("/selectDetail")
    @ApiOperation(value="下载管理-详情接口",notes = "uploadId* 必传" )
    public FebsResponse selectDetail(UploadInfo info) throws FebsException{
        try{
            QueryWrapper<UploadInfo> cqw = new QueryWrapper<>();
            cqw.setEntity(info);
            return new FebsResponse().data(this.service.getOne(cqw)).message(Constant.MESSAGE_SUCCESS);
        }catch (Exception e){
            message="查询信息失败: "+e.getMessage();
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }

    }

}
