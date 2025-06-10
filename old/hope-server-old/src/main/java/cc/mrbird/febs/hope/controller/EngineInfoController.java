package cc.mrbird.febs.hope.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.hope.domain.AccountInfo;
import cc.mrbird.febs.hope.domain.EngineInfo;
import cc.mrbird.febs.hope.service.EngineInfoService;
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
import java.util.List;
import java.util.Map;

/**
 * @author fanta
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("engineInfo")
@Api(value = "/engineInfo",tags={"引擎管理"})
public class EngineInfoController extends BaseController {
    private String message;
    @Autowired
    private EngineInfoService service;


    @GetMapping("/list")
    @ApiOperation(value="引擎管理-分页list接口",notes = "GET请求" )
    @Log("查询引擎")
    public Map<String, Object> list(QueryRequest request, EngineInfo info) {
        Page<EngineInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        LambdaQueryWrapper<EngineInfo> cqw = new LambdaQueryWrapper<>();
        cqw.like(StringUtils.isNotBlank(info.getEngineName()),EngineInfo::getEngineName,info.getEngineName());
        return getDataTable(service.page(page,cqw));
    }

    @GetMapping("/queryAll")
    @ApiOperation(value="引擎管理-查询全部接口",notes = "无参数，该接口适用于引擎下拉框" )
    public List<EngineInfo> queryAll() {
        log.info("engine queryAll enable");
        LambdaQueryWrapper<EngineInfo> cqw = new LambdaQueryWrapper<>();
        cqw.eq(EngineInfo::getEngineStatus,"true");
        return service.list(cqw);
    }



    @ApiOperation(value="引擎管理-新增接口",notes = "POST请求" )
    @PostMapping("/add")
    public FebsResponse add(EngineInfo info) {
        try {
            int count = this.service.count(new LambdaQueryWrapper<EngineInfo>().eq(EngineInfo::getEngineName, info.getEngineName()));
            if (count !=0){
                message="engineName重复，请换一个";
                return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
            }
            this.service.save(info);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "新增失败\n"+e.getMessage();
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);

        }
    }
    @ApiOperation(value="引擎管理-修改接口",notes = "engineId必传" )
    @PostMapping("/update")
    public FebsResponse update(EngineInfo info){
        if(info.getEngineId() == null){
            message="参数不合法,engineId必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",message);
        }
        try {
            info.setEngineName(null);
            this.service.updateById(info);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "修改信息失败\n"+e.getMessage();
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }
    }


    @PostMapping("/{accountIds}")
    public FebsResponse deleteAccounts(@NotBlank(message = "{required}") @PathVariable String accountIds) throws FebsException {
        try {
            String[] ids = accountIds.split(StringPool.COMMA);
            this.service.removeByIds(Arrays.asList(ids));
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            message = "删除失败"+e.getMessage();
            log.error(message, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",message);
        }
    }


}
