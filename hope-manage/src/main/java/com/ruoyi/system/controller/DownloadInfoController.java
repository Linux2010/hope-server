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
import com.ruoyi.system.domain.DownloadInfo;
import com.ruoyi.system.service.IDownloadInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 下载管理Controller
 * 
 * @author 田富升
 * @date 2025-04-26
 */
@RestController
@RequestMapping("/system/downloadInfo")
public class DownloadInfoController extends BaseController
{
    @Autowired
    private IDownloadInfoService downloadInfoService;

    /**
     * 查询下载管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:downloadInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(DownloadInfo downloadInfo)
    {
        startPage();
        List<DownloadInfo> list = downloadInfoService.selectDownloadInfoList(downloadInfo);
        return getDataTable(list);
    }

    /**
     * 导出下载管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:downloadInfo:export')")
    @Log(title = "下载管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DownloadInfo downloadInfo)
    {
        List<DownloadInfo> list = downloadInfoService.selectDownloadInfoList(downloadInfo);
        ExcelUtil<DownloadInfo> util = new ExcelUtil<DownloadInfo>(DownloadInfo.class);
        util.exportExcel(response, list, "下载管理数据");
    }

    /**
     * 获取下载管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:downloadInfo:query')")
    @GetMapping(value = "/{downloadId}")
    public AjaxResult getInfo(@PathVariable("downloadId") Long downloadId)
    {
        return success(downloadInfoService.selectDownloadInfoByDownloadId(downloadId));
    }

    /**
     * 新增下载管理
     */
    @PreAuthorize("@ss.hasPermi('system:downloadInfo:add')")
    @Log(title = "下载管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DownloadInfo downloadInfo)
    {
        return toAjax(downloadInfoService.insertDownloadInfo(downloadInfo));
    }

    /**
     * 修改下载管理
     */
    @PreAuthorize("@ss.hasPermi('system:downloadInfo:edit')")
    @Log(title = "下载管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DownloadInfo downloadInfo)
    {
        return toAjax(downloadInfoService.updateDownloadInfo(downloadInfo));
    }

    /**
     * 删除下载管理
     */
    @PreAuthorize("@ss.hasPermi('system:downloadInfo:remove')")
    @Log(title = "下载管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{downloadIds}")
    public AjaxResult remove(@PathVariable Long[] downloadIds)
    {
        return toAjax(downloadInfoService.deleteDownloadInfoByDownloadIds(downloadIds));
    }
}
