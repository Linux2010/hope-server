package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 上传管理对象 upload_info
 * 
 * @author ruoyi
 * @date 2025-04-28
 */
public class UploadInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long uploadId;

    /** 上传名称 */
    @Excel(name = "上传名称")
    private String uploadName;

    /** 开关 */
    @Excel(name = "开关")
    private String uploadEnable;

    /** 日上限 */
    @Excel(name = "日上限")
    private String uploadDayMax;

    /** 视频路径 */
    @Excel(name = "视频路径")
    private String uploadPath;

    /** 脚本路径 */
    @Excel(name = "脚本路径")
    private String uploadShellPath;

    /** 播放列表 */
    @Excel(name = "播放列表")
    private String uploadPlayList;

    /** item */
    @Excel(name = "item")
    private String uploadItem;

    /** 标签 */
    @Excel(name = "标签")
    private String uploadLabel;

    /** 描述 */
    @Excel(name = "描述")
    private String uploadDesc;

    /** 下载名称 */
    @Excel(name = "下载名称")
    private String downloadName;

    /** 频道名称 */
    @Excel(name = "频道名称")
    private String channelName;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modifyTime;

    /** ok后缀 */
    @Excel(name = "ok后缀")
    private String uploadOk;

    public void setUploadId(Long uploadId) 
    {
        this.uploadId = uploadId;
    }

    public Long getUploadId() 
    {
        return uploadId;
    }
    public void setUploadName(String uploadName) 
    {
        this.uploadName = uploadName;
    }

    public String getUploadName() 
    {
        return uploadName;
    }
    public void setUploadEnable(String uploadEnable) 
    {
        this.uploadEnable = uploadEnable;
    }

    public String getUploadEnable() 
    {
        return uploadEnable;
    }
    public void setUploadDayMax(String uploadDayMax) 
    {
        this.uploadDayMax = uploadDayMax;
    }

    public String getUploadDayMax() 
    {
        return uploadDayMax;
    }
    public void setUploadPath(String uploadPath) 
    {
        this.uploadPath = uploadPath;
    }

    public String getUploadPath() 
    {
        return uploadPath;
    }
    public void setUploadShellPath(String uploadShellPath) 
    {
        this.uploadShellPath = uploadShellPath;
    }

    public String getUploadShellPath() 
    {
        return uploadShellPath;
    }
    public void setUploadPlayList(String uploadPlayList) 
    {
        this.uploadPlayList = uploadPlayList;
    }

    public String getUploadPlayList() 
    {
        return uploadPlayList;
    }
    public void setUploadItem(String uploadItem) 
    {
        this.uploadItem = uploadItem;
    }

    public String getUploadItem() 
    {
        return uploadItem;
    }
    public void setUploadLabel(String uploadLabel) 
    {
        this.uploadLabel = uploadLabel;
    }

    public String getUploadLabel() 
    {
        return uploadLabel;
    }
    public void setUploadDesc(String uploadDesc) 
    {
        this.uploadDesc = uploadDesc;
    }

    public String getUploadDesc() 
    {
        return uploadDesc;
    }
    public void setDownloadName(String downloadName) 
    {
        this.downloadName = downloadName;
    }

    public String getDownloadName() 
    {
        return downloadName;
    }
    public void setChannelName(String channelName) 
    {
        this.channelName = channelName;
    }

    public String getChannelName() 
    {
        return channelName;
    }
    public void setModifyTime(Date modifyTime) 
    {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTime() 
    {
        return modifyTime;
    }
    public void setUploadOk(String uploadOk) 
    {
        this.uploadOk = uploadOk;
    }

    public String getUploadOk() 
    {
        return uploadOk;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uploadId", getUploadId())
            .append("uploadName", getUploadName())
            .append("uploadEnable", getUploadEnable())
            .append("uploadDayMax", getUploadDayMax())
            .append("uploadPath", getUploadPath())
            .append("uploadShellPath", getUploadShellPath())
            .append("uploadPlayList", getUploadPlayList())
            .append("uploadItem", getUploadItem())
            .append("uploadLabel", getUploadLabel())
            .append("uploadDesc", getUploadDesc())
            .append("downloadName", getDownloadName())
            .append("channelName", getChannelName())
            .append("modifyTime", getModifyTime())
            .append("createTime", getCreateTime())
            .append("uploadOk", getUploadOk())
            .toString();
    }
}
