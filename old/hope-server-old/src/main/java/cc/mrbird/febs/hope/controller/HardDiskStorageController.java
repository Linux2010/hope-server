package cc.mrbird.febs.hope.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.hope.domain.EngineInfo;
import cc.mrbird.febs.hope.domain.HardDiskStorage;
import cc.mrbird.febs.hope.service.EngineInfoService;
import cc.mrbird.febs.hope.service.HardDiskStorageService;
import cc.mrbird.febs.hope.utils.Constant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
@RequestMapping("hardDiskStorage")
@Api(value = "/hardDiskStorage",tags={"存储管理"})
public class HardDiskStorageController extends BaseController {
    @Autowired
    private HardDiskStorageService service;

    @GetMapping("/query")
    @ApiOperation(value="存储管理-查询接口",notes = "引擎ID作为查询参数" )
    public List<HardDiskStorage> query(String engineId) {
        log.info("HardDiskStorage query...");
        HardDiskStorage info = new HardDiskStorage();
        info.setEngineId(engineId);
        LambdaQueryWrapper<HardDiskStorage> cqw = new LambdaQueryWrapper<>();
        cqw.eq(StringUtils.isNotBlank(info.getEngineId()),HardDiskStorage::getEngineId,info.getEngineId());
        return service.list(cqw);
    }
}
