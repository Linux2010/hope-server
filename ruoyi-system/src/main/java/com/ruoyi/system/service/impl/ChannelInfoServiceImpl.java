package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ChannelInfoMapper;
import com.ruoyi.system.domain.ChannelInfo;
import com.ruoyi.system.service.IChannelInfoService;

/**
 * 【请填写功能名称】Service业务层处理
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
     * 查询【请填写功能名称】
     * 
     * @param channelId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public ChannelInfo selectChannelInfoByChannelId(Long channelId)
    {
        return channelInfoMapper.selectChannelInfoByChannelId(channelId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param channelInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ChannelInfo> selectChannelInfoList(ChannelInfo channelInfo)
    {
        return channelInfoMapper.selectChannelInfoList(channelInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param channelInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertChannelInfo(ChannelInfo channelInfo)
    {
        channelInfo.setCreateTime(DateUtils.getNowDate());
        return channelInfoMapper.insertChannelInfo(channelInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param channelInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateChannelInfo(ChannelInfo channelInfo)
    {
        return channelInfoMapper.updateChannelInfo(channelInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param channelIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteChannelInfoByChannelIds(Long[] channelIds)
    {
        return channelInfoMapper.deleteChannelInfoByChannelIds(channelIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param channelId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteChannelInfoByChannelId(Long channelId)
    {
        return channelInfoMapper.deleteChannelInfoByChannelId(channelId);
    }
}
