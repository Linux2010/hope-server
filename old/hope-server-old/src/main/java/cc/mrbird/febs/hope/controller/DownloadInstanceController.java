package cc.mrbird.febs.hope.controller;


import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.hope.domain.DownloadInfo;
import cc.mrbird.febs.hope.domain.DownloadInstance;
import cc.mrbird.febs.hope.domain.UploadInstance;
import cc.mrbird.febs.hope.service.DownloadInstanceService;
import cc.mrbird.febs.hope.service.UploadInstanceService;
import cc.mrbird.febs.hope.utils.Constant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@RequestMapping("downloadInstance")
@Api(value = "/downloadInstance",tags={"下载实例管理"})
public class DownloadInstanceController extends BaseController {
    private String message;

    @Autowired
    private DownloadInstanceService service;


    @ApiOperation(value="下载实例管理-分页查询接口",notes = "pageNum* 必传\n" +
            "pageSize* 必传\n" +
            "downloadName 下载名称\n" +
            "downloadType 下载类型 \n " +
            "sortField传字段名称（如：createTime）\n" +
            "sortOrder：（倒序传参：descend，正序传参：ascend）")
    @GetMapping("/queryList")
    public Map<String, Object> queryList(QueryRequest request, DownloadInstance info) {
        return getDataTable(service.queryList(info,request));
    }

    @ApiOperation(value="下载实例管理-清理视频接口",notes = "POST请求，CreateTime,ModifyTime不传" )
    @PostMapping("/cleanVideo")
    public FebsResponse cleanVideo(DownloadInstance info) {
        try {
            this.service.cleanVideo(info);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "清理视频失败\n"+e.getMessage();
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);

        }
    }

}
