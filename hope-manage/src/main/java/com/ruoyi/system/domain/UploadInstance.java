package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 上传实例对象 upload_instance
 * 
 * @author ruoyi
 * @date 2025-05-08
 */
public class UploadInstance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String instanceId;

    /** 频道名称 */
    @Excel(name = "频道名称")
    private String channelName;

    /** 上传名称 */
    @Excel(name = "上传名称")
    private String uploadName;

    /** 下载名称 */
    @Excel(name = "下载名称")
    private String downloadName;

    /** 频道类型 */
    @Excel(name = "频道类型")
    private String channelType;

    /** 上传状态 */
    @Excel(name = "上传状态")
    private String status;

    /** 上传日期 */
    @Excel(name = "上传日期")
    private String uploadDate;

    /** 视频名称 */
    @Excel(name = "视频名称")
    private String videoName;

    /** up_cmd */
    private String uploadCmd;

    /** log日志 */
    private String uploadLog;

    /** 修改时间 */
    private Date modifyTime;

    /** 视频URL */
    @Excel(name = "视频URL")
    private String videoUrl;

    /** 执行次数 */
    private Long uploadOverflow;

    /** 视频路径 */
    private String videoPath;

    /** 引擎 */
    private String engineName;

    /** 视频名称 */
    private String videoFile;

    /** 视频MD5 */
    private String videoMd5;

    /** 频道ID */
    private Long channelId;

    /** 下载ID */
    private Long downloadId;

    /** 公开 */
    private Long isPublic;

    public void setInstanceId(String instanceId) 
    {
        this.instanceId = instanceId;
    }

    public String getInstanceId() 
    {
        return instanceId;
    }
    public void setChannelName(String channelName) 
    {
        this.channelName = channelName;
    }

    public String getChannelName() 
    {
        return channelName;
    }
    public void setUploadName(String uploadName) 
    {
        this.uploadName = uploadName;
    }

    public String getUploadName() 
    {
        return uploadName;
    }
    public void setDownloadName(String downloadName) 
    {
        this.downloadName = downloadName;
    }

    public String getDownloadName() 
    {
        return downloadName;
    }
    public void setChannelType(String channelType) 
    {
        this.channelType = channelType;
    }

    public String getChannelType() 
    {
        return channelType;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setUploadDate(String uploadDate) 
    {
        this.uploadDate = uploadDate;
    }

    public String getUploadDate() 
    {
        return uploadDate;
    }
    public void setVideoName(String videoName) 
    {
        this.videoName = videoName;
    }

    public String getVideoName() 
    {
        return videoName;
    }
    public void setUploadCmd(String uploadCmd) 
    {
        this.uploadCmd = uploadCmd;
    }

    public String getUploadCmd() 
    {
        return uploadCmd;
    }
    public void setUploadLog(String uploadLog) 
    {
        this.uploadLog = uploadLog;
    }

    public String getUploadLog() 
    {
        return uploadLog;
    }
    public void setModifyTime(Date modifyTime) 
    {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTime() 
    {
        return modifyTime;
    }
    public void setVideoUrl(String videoUrl) 
    {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() 
    {
        return videoUrl;
    }
    public void setUploadOverflow(Long uploadOverflow) 
    {
        this.uploadOverflow = uploadOverflow;
    }

    public Long getUploadOverflow() 
    {
        return uploadOverflow;
    }
    public void setVideoPath(String videoPath) 
    {
        this.videoPath = videoPath;
    }

    public String getVideoPath() 
    {
        return videoPath;
    }
    public void setEngineName(String engineName) 
    {
        this.engineName = engineName;
    }

    public String getEngineName() 
    {
        return engineName;
    }
    public void setVideoFile(String videoFile) 
    {
        this.videoFile = videoFile;
    }

    public String getVideoFile() 
    {
        return videoFile;
    }
    public void setVideoMd5(String videoMd5) 
    {
        this.videoMd5 = videoMd5;
    }

    public String getVideoMd5() 
    {
        return videoMd5;
    }
    public void setChannelId(Long channelId) 
    {
        this.channelId = channelId;
    }

    public Long getChannelId() 
    {
        return channelId;
    }
    public void setDownloadId(Long downloadId) 
    {
        this.downloadId = downloadId;
    }

    public Long getDownloadId() 
    {
        return downloadId;
    }
    public void setIsPublic(Long isPublic) 
    {
        this.isPublic = isPublic;
    }

    public Long getIsPublic() 
    {
        return isPublic;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("instanceId", getInstanceId())
            .append("channelName", getChannelName())
            .append("uploadName", getUploadName())
            .append("downloadName", getDownloadName())
            .append("channelType", getChannelType())
            .append("status", getStatus())
            .append("uploadDate", getUploadDate())
            .append("videoName", getVideoName())
            .append("uploadCmd", getUploadCmd())
            .append("uploadLog", getUploadLog())
            .append("createTime", getCreateTime())
            .append("modifyTime", getModifyTime())
            .append("videoUrl", getVideoUrl())
            .append("uploadOverflow", getUploadOverflow())
            .append("videoPath", getVideoPath())
            .append("engineName", getEngineName())
            .append("videoFile", getVideoFile())
            .append("videoMd5", getVideoMd5())
            .append("channelId", getChannelId())
            .append("downloadId", getDownloadId())
            .append("isPublic", getIsPublic())
            .toString();
    }
}
