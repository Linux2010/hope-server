package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ChannelInfo;
import com.ruoyi.system.mapper.ChannelInfoMapper;
import com.ruoyi.system.service.IChannelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 【下载管理】Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-11
 */
@Service
public class ChannelInfoServiceImpl implements IChannelInfoService
{
    @Autowired
    private ChannelInfoMapper channelInfoMapper;

    /**
     * 查询【下载管理】
     * 
     * @param channelId 【下载管理】主键
     * @return 【下载管理】
     */
    @Override
    public ChannelInfo selectChannelInfoByChannelId(Long channelId)
    {
        return channelInfoMapper.selectChannelInfoByChannelId(channelId);
    }

    /**
     * 查询【下载管理】列表
     * 
     * @param channelInfo 【下载管理】
     * @return 【下载管理】
     */
    @Override
    public List<ChannelInfo> selectChannelInfoList(ChannelInfo channelInfo)
    {
        return channelInfoMapper.selectChannelInfoList(channelInfo);
    }

    /**
     * 新增【下载管理】
     * 
     * @param channelInfo 【下载管理】
     * @return 结果
     */
    @Override
    public int insertChannelInfo(ChannelInfo channelInfo)
    {
        channelInfo.setCreateTime(DateUtils.getNowDate());
        return channelInfoMapper.insertChannelInfo(channelInfo);
    }

    /**
     * 修改【下载管理】
     * 
     * @param channelInfo 【下载管理】
     * @return 结果
     */
    @Override
    public int updateChannelInfo(ChannelInfo channelInfo)
    {
        return channelInfoMapper.updateChannelInfo(channelInfo);
    }

    /**
     * 批量删除【下载管理】
     * 
     * @param channelIds 需要删除的【下载管理】主键
     * @return 结果
     */
    @Override
    public int deleteChannelInfoByChannelIds(Long[] channelIds)
    {
        return channelInfoMapper.deleteChannelInfoByChannelIds(channelIds);
    }

    /**
     * 删除【下载管理】信息
     * 
     * @param channelId 【下载管理】主键
     * @return 结果
     */
    @Override
    public int deleteChannelInfoByChannelId(Long channelId)
    {
        return channelInfoMapper.deleteChannelInfoByChannelId(channelId);
    }
    
    /**
     * 模糊搜索频道名称
     *
     * @param channelName 频道名称关键字
     * @param limit 限制返回数量
     * @return 符合条件的频道名称列表
     */
    @Override
    public List<String> searchChannelNames(String channelName, int limit) {
        // 直接调用Mapper方法，现在它已经返回String列表
        return channelInfoMapper.searchChannelNamesFuzzy(channelName, limit);
    }
}
