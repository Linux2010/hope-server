package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${ruoyi.okSuffix}")
    private String okSuffix;
    
    @Value("${ruoyi.uploadScripts.youtube}")
    private String youtubeScriptPath;
    
    @Value("${ruoyi.uploadScripts.bili}")
    private String biliScriptPath;
    
    @Value("${ruoyi.uploadScripts.xigua}")
    private String xiguaScriptPath;
    
    // 创建一个Map来存储脚本路径
    private Map<String, String> uploadScripts = new HashMap<>();
    
    /**
     * 初始化脚本路径映射
     */
    @PostConstruct
    public void initScriptPaths() {
        uploadScripts.put("youtube", youtubeScriptPath);
        uploadScripts.put("bili", biliScriptPath);
        uploadScripts.put("xigua", xiguaScriptPath);
    }

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
        uploadInfo.setUploadOk(okSuffix);
        // 根据上传类型设置脚本路径
        setUploadShellPathByType(uploadInfo);
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
        if (uploadInfo.getUploadOk() == null || uploadInfo.getUploadOk().isEmpty()) {
            uploadInfo.setUploadOk(okSuffix);
        }
        // 根据上传类型设置脚本路径
        setUploadShellPathByType(uploadInfo);
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
    
    /**
     * 获取ok后缀
     */
    @GetMapping("/okSuffix")
    public AjaxResult getOkSuffix()
    {
        return success(okSuffix);
    }
    
    /**
     * 获取上传脚本路径
     */
    @GetMapping("/uploadScripts")
    public AjaxResult getUploadScripts()
    {
        return success(uploadScripts);
    }
    
    /**
     * 获取指定类型的脚本路径
     */
    @GetMapping("/scriptPath/{type}")
    public AjaxResult getScriptPathByType(@PathVariable("type") String type)
    {
        String path = uploadScripts.getOrDefault(type.toLowerCase(), "");
        return success(path);
    }
    
    /**
     * 根据上传类型设置脚本路径
     */
    private void setUploadShellPathByType(UploadInfo uploadInfo)
    {
        // 如果已有频道名称，则根据频道类型自动设置脚本路径
        if (uploadInfo.getChannelName() != null && !uploadInfo.getChannelName().isEmpty()) {
            // 查询频道信息，获取类型
            String channelType = uploadInfoService.getChannelTypeByName(uploadInfo.getChannelName());
            
            if (channelType != null) {
                // 根据频道类型判断使用哪个脚本路径
                String scriptPath = null;
                if (channelType.toLowerCase().contains("youtube")) {
                    scriptPath = uploadScripts.get("youtube");
                } else if (channelType.toLowerCase().contains("bili")) {
                    scriptPath = uploadScripts.get("bili");
                } else if (channelType.toLowerCase().contains("xigua")) {
                    scriptPath = uploadScripts.get("xigua");
                }
                
                // 设置脚本路径
                if (scriptPath != null) {
                    uploadInfo.setUploadShellPath(scriptPath);
                }
            }
        }
    }
}
