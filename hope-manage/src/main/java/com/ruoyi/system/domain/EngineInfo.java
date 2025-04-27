package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 引擎管理对象 engine_info
 * 
 * @author 田富升
 * @date 2025-04-26
 */
public class EngineInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 引擎id */
    private Long engineId;

    /** 名称 */
    @Excel(name = "名称")
    private String engineName;

    /** 名称 */
    @Excel(name = "名称")
    private String engineIp;

    /** 账号 */
    @Excel(name = "账号")
    private String username;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 描述 */
    @Excel(name = "描述")
    private String engineDesc;

    /** 启用状态 */
    @Excel(name = "启用状态")
    private String engineStatus;

    /** 机房 */
    @Excel(name = "机房")
    private Long machineRoomId;

    /** 网络状态 */
    @Excel(name = "网络状态")
    private String engineNetStatus;

    public void setEngineId(Long engineId) 
    {
        this.engineId = engineId;
    }

    public Long getEngineId() 
    {
        return engineId;
    }
    public void setEngineName(String engineName) 
    {
        this.engineName = engineName;
    }

    public String getEngineName() 
    {
        return engineName;
    }
    public void setEngineIp(String engineIp) 
    {
        this.engineIp = engineIp;
    }

    public String getEngineIp() 
    {
        return engineIp;
    }
    public void setUsername(String username) 
    {
        this.username = username;
    }

    public String getUsername() 
    {
        return username;
    }
    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }
    public void setEngineDesc(String engineDesc) 
    {
        this.engineDesc = engineDesc;
    }

    public String getEngineDesc() 
    {
        return engineDesc;
    }
    public void setEngineStatus(String engineStatus) 
    {
        this.engineStatus = engineStatus;
    }

    public String getEngineStatus() 
    {
        return engineStatus;
    }
    public void setMachineRoomId(Long machineRoomId) 
    {
        this.machineRoomId = machineRoomId;
    }

    public Long getMachineRoomId() 
    {
        return machineRoomId;
    }
    public void setEngineNetStatus(String engineNetStatus) 
    {
        this.engineNetStatus = engineNetStatus;
    }

    public String getEngineNetStatus() 
    {
        return engineNetStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("engineId", getEngineId())
            .append("engineName", getEngineName())
            .append("engineIp", getEngineIp())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("engineDesc", getEngineDesc())
            .append("engineStatus", getEngineStatus())
            .append("machineRoomId", getMachineRoomId())
            .append("engineNetStatus", getEngineNetStatus())
            .toString();
    }
}
