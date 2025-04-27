package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 【下载管理】对象 channel_info
 * 
 * @author ruoyi
 * @date 2025-01-11
 */
public class ChannelInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long channelId;

    /** 排序字段 */
    private String orderByColumn;

    /** 排序方向 */
    private String isAsc;

    /** 频道名称，唯一 */
    @Excel(name = "频道名称，唯一")
    private String channelName;

    /** 频道类型：youtube、bili、baidu、xigua、qq、ntes */
    @Excel(name = "频道类型：youtube、bili、baidu、xigua、qq、ntes")
    private String channelType;

    /** off ,on */
    @Excel(name = "off ,on")
    private String channelOffOn;

    /** true为可用，false为不可用 */
    @Excel(name = "true为可用，false为不可用")
    private String cookieEnable;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String cookieJson;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date modifyTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String username;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String password;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String phone;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String loginUrl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String homeUrl;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 辅助邮箱 */
    @Excel(name = "辅助邮箱")
    private String assistEmail;

    /** channel 描述 */
    @Excel(name = "channel 描述")
    private String channelDesc;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String channelUrl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String cookieDomain;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String dayMax;

    /** title上限 */
    @Excel(name = "title上限")
    private String titleLimit;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long engineId;

    /** 盈利状态,0非盈利,1盈利,2封禁 */
    @Excel(name = "盈利状态,0非盈利,1盈利,2封禁")
    private String gainStatus;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date accDue;

    /** channel_detail_json */
    @Excel(name = "channel_detail_json")
    private String channelDetailJson;

    /** youtube是否需要加工标识，0需要视频加工处理，1不需要视频加工处理 */
    @Excel(name = "youtube是否需要加工标识，0需要视频加工处理，1不需要视频加工处理")
    private Long processStatus;

    public void setChannelId(Long channelId) 
    {
        this.channelId = channelId;
    }

    public Long getChannelId() 
    {
        return channelId;
    }
    public void setChannelName(String channelName) 
    {
        this.channelName = channelName;
    }

    public String getChannelName() 
    {
        return channelName;
    }
    public void setChannelType(String channelType) 
    {
        this.channelType = channelType;
    }

    public String getChannelType() 
    {
        return channelType;
    }
    public void setChannelOffOn(String channelOffOn) 
    {
        this.channelOffOn = channelOffOn;
    }

    public String getChannelOffOn() 
    {
        return channelOffOn;
    }
    public void setCookieEnable(String cookieEnable) 
    {
        this.cookieEnable = cookieEnable;
    }

    public String getCookieEnable() 
    {
        return cookieEnable;
    }
    public void setCookieJson(String cookieJson) 
    {
        this.cookieJson = cookieJson;
    }

    public String getCookieJson() 
    {
        return cookieJson;
    }
    public void setModifyTime(Date modifyTime) 
    {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTime() 
    {
        return modifyTime;
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
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setLoginUrl(String loginUrl) 
    {
        this.loginUrl = loginUrl;
    }

    public String getLoginUrl() 
    {
        return loginUrl;
    }
    public void setHomeUrl(String homeUrl) 
    {
        this.homeUrl = homeUrl;
    }

    public String getHomeUrl() 
    {
        return homeUrl;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setAssistEmail(String assistEmail) 
    {
        this.assistEmail = assistEmail;
    }

    public String getAssistEmail() 
    {
        return assistEmail;
    }
    public void setChannelDesc(String channelDesc) 
    {
        this.channelDesc = channelDesc;
    }

    public String getChannelDesc() 
    {
        return channelDesc;
    }
    public void setChannelUrl(String channelUrl) 
    {
        this.channelUrl = channelUrl;
    }

    public String getChannelUrl() 
    {
        return channelUrl;
    }
    public void setCookieDomain(String cookieDomain) 
    {
        this.cookieDomain = cookieDomain;
    }

    public String getCookieDomain() 
    {
        return cookieDomain;
    }
    public void setDayMax(String dayMax) 
    {
        this.dayMax = dayMax;
    }

    public String getDayMax() 
    {
        return dayMax;
    }
    public void setTitleLimit(String titleLimit) 
    {
        this.titleLimit = titleLimit;
    }

    public String getTitleLimit() 
    {
        return titleLimit;
    }
    public void setEngineId(Long engineId) 
    {
        this.engineId = engineId;
    }

    public Long getEngineId() 
    {
        return engineId;
    }
    public void setGainStatus(String gainStatus) 
    {
        this.gainStatus = gainStatus;
    }

    public String getGainStatus() 
    {
        return gainStatus;
    }
    public void setAccDue(Date accDue) 
    {
        this.accDue = accDue;
    }

    public Date getAccDue() 
    {
        return accDue;
    }
    public void setChannelDetailJson(String channelDetailJson) 
    {
        this.channelDetailJson = channelDetailJson;
    }

    public String getChannelDetailJson() 
    {
        return channelDetailJson;
    }
    public void setProcessStatus(Long processStatus) 
    {
        this.processStatus = processStatus;
    }

    public Long getProcessStatus() 
    {
        return processStatus;
    }

    public String getOrderByColumn() {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn) {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc() {
        return isAsc;
    }

    public void setIsAsc(String isAsc) {
        this.isAsc = isAsc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("channelId", getChannelId())
            .append("orderByColumn", getOrderByColumn())
            .append("isAsc", getIsAsc())
            .append("channelName", getChannelName())
            .append("channelType", getChannelType())
            .append("channelOffOn", getChannelOffOn())
            .append("cookieEnable", getCookieEnable())
            .append("cookieJson", getCookieJson())
            .append("createTime", getCreateTime())
            .append("modifyTime", getModifyTime())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("phone", getPhone())
            .append("loginUrl", getLoginUrl())
            .append("homeUrl", getHomeUrl())
            .append("email", getEmail())
            .append("assistEmail", getAssistEmail())
            .append("channelDesc", getChannelDesc())
            .append("channelUrl", getChannelUrl())
            .append("cookieDomain", getCookieDomain())
            .append("dayMax", getDayMax())
            .append("titleLimit", getTitleLimit())
            .append("engineId", getEngineId())
            .append("gainStatus", getGainStatus())
            .append("accDue", getAccDue())
            .append("channelDetailJson", getChannelDetailJson())
            .append("processStatus", getProcessStatus())
            .toString();
    }
}
