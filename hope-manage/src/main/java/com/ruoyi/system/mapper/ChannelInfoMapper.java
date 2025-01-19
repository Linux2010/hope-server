package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.ChannelInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-11
 */
public interface ChannelInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param channelId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public ChannelInfo selectChannelInfoByChannelId(Long channelId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param channelInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ChannelInfo> selectChannelInfoList(ChannelInfo channelInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param channelInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertChannelInfo(ChannelInfo channelInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param channelInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateChannelInfo(ChannelInfo channelInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param channelId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteChannelInfoByChannelId(Long channelId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param channelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChannelInfoByChannelIds(Long[] channelIds);
}
