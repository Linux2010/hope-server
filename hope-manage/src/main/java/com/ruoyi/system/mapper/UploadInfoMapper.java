package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UploadInfo;

/**
 * 上传管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-28
 */
public interface UploadInfoMapper 
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
     * 删除上传管理
     * 
     * @param uploadId 上传管理主键
     * @return 结果
     */
    public int deleteUploadInfoByUploadId(Long uploadId);

    /**
     * 批量删除上传管理
     * 
     * @param uploadIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUploadInfoByUploadIds(Long[] uploadIds);
}
