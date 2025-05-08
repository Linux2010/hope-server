package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UploadInstance;

/**
 * 上传实例Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-08
 */
public interface UploadInstanceMapper 
{
    /**
     * 查询上传实例
     * 
     * @param instanceId 上传实例主键
     * @return 上传实例
     */
    public UploadInstance selectUploadInstanceByInstanceId(String instanceId);

    /**
     * 查询上传实例列表
     * 
     * @param uploadInstance 上传实例
     * @return 上传实例集合
     */
    public List<UploadInstance> selectUploadInstanceList(UploadInstance uploadInstance);

    /**
     * 新增上传实例
     * 
     * @param uploadInstance 上传实例
     * @return 结果
     */
    public int insertUploadInstance(UploadInstance uploadInstance);

    /**
     * 修改上传实例
     * 
     * @param uploadInstance 上传实例
     * @return 结果
     */
    public int updateUploadInstance(UploadInstance uploadInstance);

    /**
     * 删除上传实例
     * 
     * @param instanceId 上传实例主键
     * @return 结果
     */
    public int deleteUploadInstanceByInstanceId(String instanceId);

    /**
     * 批量删除上传实例
     * 
     * @param instanceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUploadInstanceByInstanceIds(String[] instanceIds);
}
