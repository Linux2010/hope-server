package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.DownloadInfo;

/**
 * 下载管理Service接口
 * 
 * @author 田富升
 * @date 2025-04-26
 */
public interface IDownloadInfoService 
{
    /**
     * 查询下载管理
     * 
     * @param downloadId 下载管理主键
     * @return 下载管理
     */
    public DownloadInfo selectDownloadInfoByDownloadId(Long downloadId);

    /**
     * 查询下载管理列表
     * 
     * @param downloadInfo 下载管理
     * @return 下载管理集合
     */
    public List<DownloadInfo> selectDownloadInfoList(DownloadInfo downloadInfo);

    /**
     * 新增下载管理
     * 
     * @param downloadInfo 下载管理
     * @return 结果
     */
    public int insertDownloadInfo(DownloadInfo downloadInfo);

    /**
     * 修改下载管理
     * 
     * @param downloadInfo 下载管理
     * @return 结果
     */
    public int updateDownloadInfo(DownloadInfo downloadInfo);

    /**
     * 批量删除下载管理
     * 
     * @param downloadIds 需要删除的下载管理主键集合
     * @return 结果
     */
    public int deleteDownloadInfoByDownloadIds(Long[] downloadIds);

    /**
     * 删除下载管理信息
     * 
     * @param downloadId 下载管理主键
     * @return 结果
     */
    public int deleteDownloadInfoByDownloadId(Long downloadId);
    
    /**
     * 模糊搜索下载名称
     * 
     * @param downloadName 下载名称关键字
     * @param limit 限制返回数量
     * @return 符合条件的下载名称列表
     */
    public List<String> searchDownloadNames(String downloadName, int limit);
}
