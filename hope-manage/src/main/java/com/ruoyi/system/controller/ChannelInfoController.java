package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.ChannelInfo;
import com.ruoyi.system.service.IChannelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 【频道管理】Controller
 * 
 * @author ruoyi
 * @date 2025-01-11
 */
@RestController
@RequestMapping("/system/channel")
public class ChannelInfoController extends BaseController
{
    @Autowired
    private IChannelInfoService channelInfoService;

    /**
     * 查询【频道管理】列表
     */
    @PreAuthorize("@ss.hasPermi('system:channel:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChannelInfo channelInfo)
    {
        startPage();
        List<ChannelInfo> list = channelInfoService.selectChannelInfoList(channelInfo);
        return getDataTable(list);
    }

    /**
     * 导出【频道管理】列表
     */
    @PreAuthorize("@ss.hasPermi('system:channel:export')")
    @Log(title = "【频道管理】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChannelInfo channelInfo)
    {
        List<ChannelInfo> list = channelInfoService.selectChannelInfoList(channelInfo);
        ExcelUtil<ChannelInfo> util = new ExcelUtil<ChannelInfo>(ChannelInfo.class);
        util.exportExcel(response, list, "【频道管理】数据");
    }

    /**
     * 获取【频道管理】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:channel:query')")
    @GetMapping(value = "/{channelId}")
    public AjaxResult getInfo(@PathVariable("channelId") Long channelId)
    {
        return success(channelInfoService.selectChannelInfoByChannelId(channelId));
    }

    /**
     * 新增【频道管理】
     */
    @PreAuthorize("@ss.hasPermi('system:channel:add')")
    @Log(title = "【频道管理】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChannelInfo channelInfo)
    {
        return toAjax(channelInfoService.insertChannelInfo(channelInfo));
    }

    /**
     * 修改【频道管理】
     */
    @PreAuthorize("@ss.hasPermi('system:channel:edit')")
    @Log(title = "【频道管理】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChannelInfo channelInfo)
    {
        return toAjax(channelInfoService.updateChannelInfo(channelInfo));
    }

    /**
     * 删除【频道管理】
     */
    @PreAuthorize("@ss.hasPermi('system:channel:remove')")
    @Log(title = "【频道管理】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{channelIds}")
    public AjaxResult remove(@PathVariable Long[] channelIds)
    {
        return toAjax(channelInfoService.deleteChannelInfoByChannelIds(channelIds));
    }

    /**
     * 获取所有频道名称列表
     */
    @GetMapping("/listAllNames")
    public AjaxResult listAllNames()
    {
        ChannelInfo query = new ChannelInfo();
        // 只查询启用状态的频道
        query.setChannelOffOn("0"); // 假设0表示启用状态 
        List<ChannelInfo> list = channelInfoService.selectChannelInfoList(query);
        List<String> channelNames = list.stream().map(ChannelInfo::getChannelName).collect(java.util.stream.Collectors.toList());
        return success(channelNames);
    }
    
    /**
     * 模糊搜索频道名称，最多返回5条
     */
    @GetMapping("/searchNames")
    public AjaxResult searchChannelNames(@RequestParam(value = "channelName", required = false) String channelName)
    {
        List<String> channelNames = channelInfoService.searchChannelNames(channelName, 5);
        return success(channelNames);
    }



    @PostMapping("/openBack/{channelType}/{channelName}")
    @PreAuthorize("@ss.hasPermi('system:channel:openBack')")
    public AjaxResult openBack(@NotBlank(message = "{required}") @PathVariable String channelType, @NotBlank(message = "{required}") @PathVariable String channelName) {

        try {
            channelInfoService.openBack(channelType, channelName);
            return success("启动成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return error("启动失败");
        }

    }
}
