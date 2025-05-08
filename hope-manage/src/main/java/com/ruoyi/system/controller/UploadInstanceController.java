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
import com.ruoyi.system.domain.UploadInstance;
import com.ruoyi.system.service.IUploadInstanceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 上传实例Controller
 * 
 * @author ruoyi
 * @date 2025-05-08
 */
@RestController
@RequestMapping("/system/instance")
public class UploadInstanceController extends BaseController
{
    @Autowired
    private IUploadInstanceService uploadInstanceService;

    /**
     * 查询上传实例列表
     */
    @PreAuthorize("@ss.hasPermi('system:instance:list')")
    @GetMapping("/list")
    public TableDataInfo list(UploadInstance uploadInstance)
    {
        startPage();
        List<UploadInstance> list = uploadInstanceService.selectUploadInstanceList(uploadInstance);
        return getDataTable(list);
    }

    /**
     * 导出上传实例列表
     */
    @PreAuthorize("@ss.hasPermi('system:instance:export')")
    @Log(title = "上传实例", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UploadInstance uploadInstance)
    {
        List<UploadInstance> list = uploadInstanceService.selectUploadInstanceList(uploadInstance);
        ExcelUtil<UploadInstance> util = new ExcelUtil<UploadInstance>(UploadInstance.class);
        util.exportExcel(response, list, "上传实例数据");
    }

    /**
     * 获取上传实例详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:instance:query')")
    @GetMapping(value = "/{instanceId}")
    public AjaxResult getInfo(@PathVariable("instanceId") String instanceId)
    {
        return success(uploadInstanceService.selectUploadInstanceByInstanceId(instanceId));
    }

    /**
     * 新增上传实例
     */
    @PreAuthorize("@ss.hasPermi('system:instance:add')")
    @Log(title = "上传实例", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UploadInstance uploadInstance)
    {
        return toAjax(uploadInstanceService.insertUploadInstance(uploadInstance));
    }

    /**
     * 修改上传实例
     */
    @PreAuthorize("@ss.hasPermi('system:instance:edit')")
    @Log(title = "上传实例", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UploadInstance uploadInstance)
    {
        return toAjax(uploadInstanceService.updateUploadInstance(uploadInstance));
    }

    /**
     * 删除上传实例
     */
    @PreAuthorize("@ss.hasPermi('system:instance:remove')")
    @Log(title = "上传实例", businessType = BusinessType.DELETE)
	@DeleteMapping("/{instanceIds}")
    public AjaxResult remove(@PathVariable String[] instanceIds)
    {
        return toAjax(uploadInstanceService.deleteUploadInstanceByInstanceIds(instanceIds));
    }
}
