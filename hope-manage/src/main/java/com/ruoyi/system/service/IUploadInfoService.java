package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.UploadInfo;

/**
 * 上传管理Service接口
 * 
 * @author ruoyi
 * @date 2025-04-28
 */
public interface IUploadInfoService 
{
    /**
     * 查询上传管理
     * 
     * @param uploadId 上传管理主键
     * @return 上传管理
     */
    public UploadInfo selectUploadInfoByUploadId(Long uploadId);

    /**
     * 查询上传管理列表
     * 
     * @param uploadInfo 上传管理
     * @return 上传管理集合
     */
    public List<UploadInfo> selectUploadInfoList(UploadInfo uploadInfo);

    /**
     * 新增上传管理
     * 
     * @param uploadInfo 上传管理
     * @return 结果
     */
    public int insertUploadInfo(UploadInfo uploadInfo);

    /**
     * 修改上传管理
     * 
     * @param uploadInfo 上传管理
     * @return 结果
     */
    public int updateUploadInfo(UploadInfo uploadInfo);

    /**
     * 批量删除上传管理
     * 
     * @param uploadIds 需要删除的上传管理主键集合
     * @return 结果
     */
    public int deleteUploadInfoByUploadIds(Long[] uploadIds);

    /**
     * 删除上传管理信息
     * 
     * @param uploadId 上传管理主键
     * @return 结果
     */
    public int deleteUploadInfoByUploadId(Long uploadId);
    
    /**
     * 根据频道名称获取频道类型
     * 
     * @param channelName 频道名称
     * @return 频道类型
     */
    public String getChannelTypeByName(String channelName);
}
