package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.EngineInfo;

/**
 * 引擎管理Service接口
 * 
 * @author 田富升
 * @date 2025-04-26
 */
public interface IEngineInfoService 
{
    /**
     * 查询引擎管理
     * 
     * @param engineId 引擎管理主键
     * @return 引擎管理
     */
    public EngineInfo selectEngineInfoByEngineId(Long engineId);

    /**
     * 查询引擎管理列表
     * 
     * @param engineInfo 引擎管理
     * @return 引擎管理集合
     */
    public List<EngineInfo> selectEngineInfoList(EngineInfo engineInfo);

    /**
     * 新增引擎管理
     * 
     * @param engineInfo 引擎管理
     * @return 结果
     */
    public int insertEngineInfo(EngineInfo engineInfo);

    /**
     * 修改引擎管理
     * 
     * @param engineInfo 引擎管理
     * @return 结果
     */
    public int updateEngineInfo(EngineInfo engineInfo);

    /**
     * 批量删除引擎管理
     * 
     * @param engineIds 需要删除的引擎管理主键集合
     * @return 结果
     */
    public int deleteEngineInfoByEngineIds(Long[] engineIds);

    /**
     * 删除引擎管理信息
     * 
     * @param engineId 引擎管理主键
     * @return 结果
     */
    public int deleteEngineInfoByEngineId(Long engineId);
}
