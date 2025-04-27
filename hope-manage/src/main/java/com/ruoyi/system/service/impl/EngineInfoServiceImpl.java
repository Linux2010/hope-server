package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.EngineInfoMapper;
import com.ruoyi.system.domain.EngineInfo;
import com.ruoyi.system.service.IEngineInfoService;

/**
 * 引擎管理Service业务层处理
 * 
 * @author 田富升
 * @date 2025-04-26
 */
@Service
public class EngineInfoServiceImpl implements IEngineInfoService 
{
    @Autowired
    private EngineInfoMapper engineInfoMapper;

    /**
     * 查询引擎管理
     * 
     * @param engineId 引擎管理主键
     * @return 引擎管理
     */
    @Override
    public EngineInfo selectEngineInfoByEngineId(Long engineId)
    {
        return engineInfoMapper.selectEngineInfoByEngineId(engineId);
    }

    /**
     * 查询引擎管理列表
     * 
     * @param engineInfo 引擎管理
     * @return 引擎管理
     */
    @Override
    public List<EngineInfo> selectEngineInfoList(EngineInfo engineInfo)
    {
        return engineInfoMapper.selectEngineInfoList(engineInfo);
    }

    /**
     * 新增引擎管理
     * 
     * @param engineInfo 引擎管理
     * @return 结果
     */
    @Override
    public int insertEngineInfo(EngineInfo engineInfo)
    {
        return engineInfoMapper.insertEngineInfo(engineInfo);
    }

    /**
     * 修改引擎管理
     * 
     * @param engineInfo 引擎管理
     * @return 结果
     */
    @Override
    public int updateEngineInfo(EngineInfo engineInfo)
    {
        return engineInfoMapper.updateEngineInfo(engineInfo);
    }

    /**
     * 批量删除引擎管理
     * 
     * @param engineIds 需要删除的引擎管理主键
     * @return 结果
     */
    @Override
    public int deleteEngineInfoByEngineIds(Long[] engineIds)
    {
        return engineInfoMapper.deleteEngineInfoByEngineIds(engineIds);
    }

    /**
     * 删除引擎管理信息
     * 
     * @param engineId 引擎管理主键
     * @return 结果
     */
    @Override
    public int deleteEngineInfoByEngineId(Long engineId)
    {
        return engineInfoMapper.deleteEngineInfoByEngineId(engineId);
    }
}
