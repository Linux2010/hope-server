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
import com.ruoyi.system.domain.UploadInfo;
import com.ruoyi.system.service.IUploadInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 上传管理Controller
 * 
 * @author ruoyi
 * @date 2025-04-28
 */
@RestController
@RequestMapping("/system/uploadInfo")
public class UploadInfoController extends BaseController
{
    @Autowired
    private IUploadInfoService uploadInfoService;

    /**
     * 查询上传管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:uploadInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(UploadInfo uploadInfo)
    {
        startPage();
        List<UploadInfo> list = uploadInfoService.selectUploadInfoList(uploadInfo);
        return getDataTable(list);
    }

    /**
     * 导出上传管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:uploadInfo:export')")
    @Log(title = "上传管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UploadInfo uploadInfo)
    {
        List<UploadInfo> list = uploadInfoService.selectUploadInfoList(uploadInfo);
        ExcelUtil<UploadInfo> util = new ExcelUtil<UploadInfo>(UploadInfo.class);
        util.exportExcel(response, list, "上传管理数据");
    }

    /**
     * 获取上传管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:uploadInfo:query')")
    @GetMapping(value = "/{uploadId}")
    public AjaxResult getInfo(@PathVariable("uploadId") Long uploadId)
    {
        return success(uploadInfoService.selectUploadInfoByUploadId(uploadId));
    }

    /**
     * 新增上传管理
     */
    @PreAuthorize("@ss.hasPermi('system:uploadInfo:add')")
    @Log(title = "上传管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UploadInfo uploadInfo)
    {
        return toAjax(uploadInfoService.insertUploadInfo(uploadInfo));
    }

    /**
     * 修改上传管理
     */
    @PreAuthorize("@ss.hasPermi('system:uploadInfo:edit')")
    @Log(title = "上传管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UploadInfo uploadInfo)
    {
        return toAjax(uploadInfoService.updateUploadInfo(uploadInfo));
    }

    /**
     * 删除上传管理
     */
    @PreAuthorize("@ss.hasPermi('system:uploadInfo:remove')")
    @Log(title = "上传管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{uploadIds}")
    public AjaxResult remove(@PathVariable Long[] uploadIds)
    {
        return toAjax(uploadInfoService.deleteUploadInfoByUploadIds(uploadIds));
    }
}
