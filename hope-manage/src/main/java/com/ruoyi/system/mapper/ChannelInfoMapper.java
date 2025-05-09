package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.ChannelInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【下载管理】Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-11
 */
public interface ChannelInfoMapper 
{
    /**
     * 查询【下载管理】
     * 
     * @param channelId 【下载管理】主键
     * @return 【下载管理】
     */
    public ChannelInfo selectChannelInfoByChannelId(Long channelId);

    /**
     * 根据频道名称查询频道信息
     * 
     * @param channelName 频道名称
     * @return 频道信息
     */
    public ChannelInfo selectChannelInfoByName(@Param("channelName") String channelName);

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
     * 删除【下载管理】
     * 
     * @param channelId 【下载管理】主键
     * @return 结果
     */
    public int deleteChannelInfoByChannelId(Long channelId);

    /**
     * 批量删除【下载管理】
     * 
     * @param channelIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChannelInfoByChannelIds(Long[] channelIds);
    
    /**
     * 模糊搜索频道名称
     * 
     * @param channelName 频道名称关键字
     * @param limit 限制返回数量
     * @return 符合条件的频道名称列表
     */
    public List<String> searchChannelNamesFuzzy(@Param("channelName") String channelName, @Param("limit") int limit);
}
