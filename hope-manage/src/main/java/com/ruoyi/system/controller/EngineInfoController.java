package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.EngineInfo;
import com.ruoyi.system.service.IEngineInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 引擎管理Controller
 * 
 * @author 田富升
 * @date 2025-04-26
 */
@RestController
@RequestMapping("/system/engineInfo")
public class EngineInfoController extends BaseController
{
    @Autowired
    private IEngineInfoService engineInfoService;

    /**
     * 查询引擎管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:engineInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(EngineInfo engineInfo)
    {
        startPage();
        List<EngineInfo> list = engineInfoService.selectEngineInfoList(engineInfo);
        return getDataTable(list);
    }

    /**
     * 导出引擎管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:engineInfo:export')")
    @Log(title = "引擎管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EngineInfo engineInfo)
    {
        List<EngineInfo> list = engineInfoService.selectEngineInfoList(engineInfo);
        ExcelUtil<EngineInfo> util = new ExcelUtil<EngineInfo>(EngineInfo.class);
        util.exportExcel(response, list, "引擎管理数据");
    }

    /**
     * 获取引擎管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:engineInfo:query')")
    @GetMapping(value = "/{engineId}")
    public AjaxResult getInfo(@PathVariable("engineId") Long engineId)
    {
        return success(engineInfoService.selectEngineInfoByEngineId(engineId));
    }

    /**
     * 新增引擎管理
     */
    @PreAuthorize("@ss.hasPermi('system:engineInfo:add')")
    @Log(title = "引擎管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EngineInfo engineInfo)
    {
        return toAjax(engineInfoService.insertEngineInfo(engineInfo));
    }

    /**
     * 修改引擎管理
     */
    @PreAuthorize("@ss.hasPermi('system:engineInfo:edit')")
    @Log(title = "引擎管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EngineInfo engineInfo)
    {
        return toAjax(engineInfoService.updateEngineInfo(engineInfo));
    }

    /**
     * 删除引擎管理
     */
    @PreAuthorize("@ss.hasPermi('system:engineInfo:remove')")
    @Log(title = "引擎管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{engineIds}")
    public AjaxResult remove(@PathVariable Long[] engineIds)
    {
        return toAjax(engineInfoService.deleteEngineInfoByEngineIds(engineIds));
    }
}
