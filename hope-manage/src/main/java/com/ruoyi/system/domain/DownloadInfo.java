package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 下载管理对象 download_info
 * 
 * @author 田富升
 * @date 2025-04-26
 */
public class DownloadInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long downloadId;

    /** 名称 */
    @Excel(name = "名称")
    private String downloadName;

    /** 类型 */
    @Excel(name = "类型")
    private String downloadType;

    /** 内容 */
    @Excel(name = "内容")
    private String videoType;

    /** 脚本 */
    private String shellPath;

    /** 代理 */
    private String agent;

    /** URL */
    @Excel(name = "URL")
    private String downloadUrl;

    /** 开关 */
    @Excel(name = "开关")
    private String downloadEnable;

    /** 存储 */
    private String downloadPath;

    /** 开始 */
    private String downloadStart;

    /** 结束 */
    private String downloadEnd;

    /** 全量 */
    private String downloadAllEnd;

    /** 后置处理 */
    private String afterShellPath;

    /** 修改时间 */
    private Date modifyTime;

    /** 描述 */
    private String downloadDesc;

    /** 引擎ID */
    private Long engineId;

    /** 循环类型 */
    private String cycleType;

    /** 存储量 */
    @Excel(name = "存储量")
    private BigDecimal usedSize;

    /** 质量 */
    @Excel(name = "质量")
    private String quality;

    /** 存储 */
    @Excel(name = "存储")
    private String diskStorageInfo;

    public void setDownloadId(Long downloadId) 
    {
        this.downloadId = downloadId;
    }

    public Long getDownloadId() 
    {
        return downloadId;
    }
    public void setDownloadName(String downloadName) 
    {
        this.downloadName = downloadName;
    }

    public String getDownloadName() 
    {
        return downloadName;
    }
    public void setDownloadType(String downloadType) 
    {
        this.downloadType = downloadType;
    }

    public String getDownloadType() 
    {
        return downloadType;
    }
    public void setVideoType(String videoType) 
    {
        this.videoType = videoType;
    }

    public String getVideoType() 
    {
        return videoType;
    }
    public void setShellPath(String shellPath) 
    {
        this.shellPath = shellPath;
    }

    public String getShellPath() 
    {
        return shellPath;
    }
    public void setAgent(String agent) 
    {
        this.agent = agent;
    }

    public String getAgent() 
    {
        return agent;
    }
    public void setDownloadUrl(String downloadUrl) 
    {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadUrl() 
    {
        return downloadUrl;
    }
    public void setDownloadEnable(String downloadEnable) 
    {
        this.downloadEnable = downloadEnable;
    }

    public String getDownloadEnable() 
    {
        return downloadEnable;
    }
    public void setDownloadPath(String downloadPath) 
    {
        this.downloadPath = downloadPath;
    }

    public String getDownloadPath() 
    {
        return downloadPath;
    }
    public void setDownloadStart(String downloadStart) 
    {
        this.downloadStart = downloadStart;
    }

    public String getDownloadStart() 
    {
        return downloadStart;
    }
    public void setDownloadEnd(String downloadEnd) 
    {
        this.downloadEnd = downloadEnd;
    }

    public String getDownloadEnd() 
    {
        return downloadEnd;
    }
    public void setDownloadAllEnd(String downloadAllEnd) 
    {
        this.downloadAllEnd = downloadAllEnd;
    }

    public String getDownloadAllEnd() 
    {
        return downloadAllEnd;
    }
    public void setAfterShellPath(String afterShellPath) 
    {
        this.afterShellPath = afterShellPath;
    }

    public String getAfterShellPath() 
    {
        return afterShellPath;
    }
    public void setModifyTime(Date modifyTime) 
    {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTime() 
    {
        return modifyTime;
    }
    public void setDownloadDesc(String downloadDesc) 
    {
        this.downloadDesc = downloadDesc;
    }

    public String getDownloadDesc() 
    {
        return downloadDesc;
    }
    public void setEngineId(Long engineId) 
    {
        this.engineId = engineId;
    }

    public Long getEngineId() 
    {
        return engineId;
    }
    public void setCycleType(String cycleType) 
    {
        this.cycleType = cycleType;
    }

    public String getCycleType() 
    {
        return cycleType;
    }
    public void setUsedSize(BigDecimal usedSize) 
    {
        this.usedSize = usedSize;
    }

    public BigDecimal getUsedSize() 
    {
        return usedSize;
    }
    public void setQuality(String quality) 
    {
        this.quality = quality;
    }

    public String getQuality() 
    {
        return quality;
    }
    public void setDiskStorageInfo(String diskStorageInfo) 
    {
        this.diskStorageInfo = diskStorageInfo;
    }

    public String getDiskStorageInfo() 
    {
        return diskStorageInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("downloadId", getDownloadId())
            .append("downloadName", getDownloadName())
            .append("downloadType", getDownloadType())
            .append("videoType", getVideoType())
            .append("shellPath", getShellPath())
            .append("agent", getAgent())
            .append("downloadUrl", getDownloadUrl())
            .append("downloadEnable", getDownloadEnable())
            .append("downloadPath", getDownloadPath())
            .append("downloadStart", getDownloadStart())
            .append("downloadEnd", getDownloadEnd())
            .append("downloadAllEnd", getDownloadAllEnd())
            .append("afterShellPath", getAfterShellPath())
            .append("modifyTime", getModifyTime())
            .append("createTime", getCreateTime())
            .append("downloadDesc", getDownloadDesc())
            .append("engineId", getEngineId())
            .append("cycleType", getCycleType())
            .append("usedSize", getUsedSize())
            .append("quality", getQuality())
            .append("diskStorageInfo", getDiskStorageInfo())
            .toString();
    }
}
