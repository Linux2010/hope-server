package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DownloadInfoMapper;
import com.ruoyi.system.domain.DownloadInfo;
import com.ruoyi.system.service.IDownloadInfoService;

/**
 * 下载管理Service业务层处理
 * 
 * @author 田富升
 * @date 2025-04-26
 */
@Service
public class DownloadInfoServiceImpl implements IDownloadInfoService 
{
    @Autowired
    private DownloadInfoMapper downloadInfoMapper;

    /**
     * 查询下载管理
     * 
     * @param downloadId 下载管理主键
     * @return 下载管理
     */
    @Override
    public DownloadInfo selectDownloadInfoByDownloadId(Long downloadId)
    {
        return downloadInfoMapper.selectDownloadInfoByDownloadId(downloadId);
    }

    /**
     * 查询下载管理列表
     * 
     * @param downloadInfo 下载管理
     * @return 下载管理
     */
    @Override
    public List<DownloadInfo> selectDownloadInfoList(DownloadInfo downloadInfo)
    {
        return downloadInfoMapper.selectDownloadInfoList(downloadInfo);
    }

    /**
     * 新增下载管理
     * 
     * @param downloadInfo 下载管理
     * @return 结果
     */
    @Override
    public int insertDownloadInfo(DownloadInfo downloadInfo)
    {
        downloadInfo.setCreateTime(DateUtils.getNowDate());
        return downloadInfoMapper.insertDownloadInfo(downloadInfo);
    }

    /**
     * 修改下载管理
     * 
     * @param downloadInfo 下载管理
     * @return 结果
     */
    @Override
    public int updateDownloadInfo(DownloadInfo downloadInfo)
    {
        return downloadInfoMapper.updateDownloadInfo(downloadInfo);
    }

    /**
     * 批量删除下载管理
     * 
     * @param downloadIds 需要删除的下载管理主键
     * @return 结果
     */
    @Override
    public int deleteDownloadInfoByDownloadIds(Long[] downloadIds)
    {
        return downloadInfoMapper.deleteDownloadInfoByDownloadIds(downloadIds);
    }

    /**
     * 删除下载管理信息
     * 
     * @param downloadId 下载管理主键
     * @return 结果
     */
    @Override
    public int deleteDownloadInfoByDownloadId(Long downloadId)
    {
        return downloadInfoMapper.deleteDownloadInfoByDownloadId(downloadId);
    }
}
