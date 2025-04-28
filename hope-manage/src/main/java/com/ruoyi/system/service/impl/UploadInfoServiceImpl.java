package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.UploadInfoMapper;
import com.ruoyi.system.domain.UploadInfo;
import com.ruoyi.system.service.IUploadInfoService;

/**
 * 上传管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-28
 */
@Service
public class UploadInfoServiceImpl implements IUploadInfoService 
{
    @Autowired
    private UploadInfoMapper uploadInfoMapper;

    /**
     * 查询上传管理
     * 
     * @param uploadId 上传管理主键
     * @return 上传管理
     */
    @Override
    public UploadInfo selectUploadInfoByUploadId(Long uploadId)
    {
        return uploadInfoMapper.selectUploadInfoByUploadId(uploadId);
    }

    /**
     * 查询上传管理列表
     * 
     * @param uploadInfo 上传管理
     * @return 上传管理
     */
    @Override
    public List<UploadInfo> selectUploadInfoList(UploadInfo uploadInfo)
    {
        return uploadInfoMapper.selectUploadInfoList(uploadInfo);
    }

    /**
     * 新增上传管理
     * 
     * @param uploadInfo 上传管理
     * @return 结果
     */
    @Override
    public int insertUploadInfo(UploadInfo uploadInfo)
    {
        uploadInfo.setCreateTime(DateUtils.getNowDate());
        return uploadInfoMapper.insertUploadInfo(uploadInfo);
    }

    /**
     * 修改上传管理
     * 
     * @param uploadInfo 上传管理
     * @return 结果
     */
    @Override
    public int updateUploadInfo(UploadInfo uploadInfo)
    {
        return uploadInfoMapper.updateUploadInfo(uploadInfo);
    }

    /**
     * 批量删除上传管理
     * 
     * @param uploadIds 需要删除的上传管理主键
     * @return 结果
     */
    @Override
    public int deleteUploadInfoByUploadIds(Long[] uploadIds)
    {
        return uploadInfoMapper.deleteUploadInfoByUploadIds(uploadIds);
    }

    /**
     * 删除上传管理信息
     * 
     * @param uploadId 上传管理主键
     * @return 结果
     */
    @Override
    public int deleteUploadInfoByUploadId(Long uploadId)
    {
        return uploadInfoMapper.deleteUploadInfoByUploadId(uploadId);
    }
}
