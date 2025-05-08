package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.UploadInstanceMapper;
import com.ruoyi.system.domain.UploadInstance;
import com.ruoyi.system.service.IUploadInstanceService;

/**
 * 上传实例Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-08
 */
@Service
public class UploadInstanceServiceImpl implements IUploadInstanceService 
{
    @Autowired
    private UploadInstanceMapper uploadInstanceMapper;

    /**
     * 查询上传实例
     * 
     * @param instanceId 上传实例主键
     * @return 上传实例
     */
    @Override
    public UploadInstance selectUploadInstanceByInstanceId(String instanceId)
    {
        return uploadInstanceMapper.selectUploadInstanceByInstanceId(instanceId);
    }

    /**
     * 查询上传实例列表
     * 
     * @param uploadInstance 上传实例
     * @return 上传实例
     */
    @Override
    public List<UploadInstance> selectUploadInstanceList(UploadInstance uploadInstance)
    {
        return uploadInstanceMapper.selectUploadInstanceList(uploadInstance);
    }

    /**
     * 新增上传实例
     * 
     * @param uploadInstance 上传实例
     * @return 结果
     */
    @Override
    public int insertUploadInstance(UploadInstance uploadInstance)
    {
        uploadInstance.setCreateTime(DateUtils.getNowDate());
        return uploadInstanceMapper.insertUploadInstance(uploadInstance);
    }

    /**
     * 修改上传实例
     * 
     * @param uploadInstance 上传实例
     * @return 结果
     */
    @Override
    public int updateUploadInstance(UploadInstance uploadInstance)
    {
        return uploadInstanceMapper.updateUploadInstance(uploadInstance);
    }

    /**
     * 批量删除上传实例
     * 
     * @param instanceIds 需要删除的上传实例主键
     * @return 结果
     */
    @Override
    public int deleteUploadInstanceByInstanceIds(String[] instanceIds)
    {
        return uploadInstanceMapper.deleteUploadInstanceByInstanceIds(instanceIds);
    }

    /**
     * 删除上传实例信息
     * 
     * @param instanceId 上传实例主键
     * @return 结果
     */
    @Override
    public int deleteUploadInstanceByInstanceId(String instanceId)
    {
        return uploadInstanceMapper.deleteUploadInstanceByInstanceId(instanceId);
    }
}
