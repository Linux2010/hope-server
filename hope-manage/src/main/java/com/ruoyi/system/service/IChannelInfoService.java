package com.ruoyi.system.service;


import com.ruoyi.system.domain.ChannelInfo;

import java.util.List;

/**
 * 【下载管理】Service接口
 * 
 * @author ruoyi
 * @date 2025-01-11
 */
public interface IChannelInfoService 
{
    /**
     * 查询【下载管理】
     * 
     * @param channelId 【下载管理】主键
     * @return 【下载管理】
     */
    public ChannelInfo selectChannelInfoByChannelId(Long channelId);

    /**
     * 查询【下载管理】列表
     * 
     * @param channelInfo 【下载管理】
     * @return 【下载管理】集合
     */
    public List<ChannelInfo> selectChannelInfoList(ChannelInfo channelInfo);

    /**
     * 新增【下载管理】
     * 
     * @param channelInfo 【下载管理】
     * @return 结果
     */
    public int insertChannelInfo(ChannelInfo channelInfo);

    /**
     * 修改【下载管理】
     * 
     * @param channelInfo 【下载管理】
     * @return 结果
     */
    public int updateChannelInfo(ChannelInfo channelInfo);

    /**
     * 批量删除【下载管理】
     * 
     * @param channelIds 需要删除的【下载管理】主键集合
     * @return 结果
     */
    public int deleteChannelInfoByChannelIds(Long[] channelIds);

    /**
     * 删除【下载管理】信息
     * 
     * @param channelId 【下载管理】主键
     * @return 结果
     */
    public int deleteChannelInfoByChannelId(Long channelId);
    
    /**
     * 模糊搜索频道名称
     * 
     * @param channelName 频道名称关键字
     * @param limit 限制返回数量
     * @return 符合条件的频道名称列表
     */
    public List<String> searchChannelNames(String channelName, int limit);

    void openBack(String channelType, String channelName);
}
