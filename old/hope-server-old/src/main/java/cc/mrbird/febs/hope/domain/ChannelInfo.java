package cc.mrbird.febs.hope.domain;


import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fanta
 */
@Data
@TableName("channel_info")
@Excel("频道信息表")
@ApiModel("频道信息表")
public class ChannelInfo implements Serializable {
    private static final long serialVersionUID = -4352868070794165001L;

    @ApiParam(value = "自增主键")
    @TableId(value = "channel_id", type = IdType.AUTO)
    private Long channelId;

    @TableField(value = "channel_name")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "频道名称")
    @ApiParam(value = "频道名称")
    private String channelName;

    @TableField(value = "channel_type")
    @ExcelField(value = "频道类型")
    @ApiParam(value = "频道类型")
    private String channelType;

    @ApiParam(value = "频道开关")
    @TableField(value = "channel_off_on")
    @ExcelField(value = "频道开关")
    private String channelOffOn;

    @ApiParam(value = "频道cookie状态")
    @TableField(value = "cookie_enable")
    @ExcelField(value = "频道cookie状态")
    private String cookieEnable;

    @ApiParam(value = "频道cookie信息")
    @TableField(value = "cookie_json")
    @ExcelField(value = "频道cookie信息")
    private String cookieJson;

    @ApiParam(value = "创建时间")
    @TableField(value = "create_time")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;


    @ApiParam(value = "修改时间")
    @TableField(value = "modify_time")
    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;

    @ApiParam(value = "登录账号")
    @TableField(value = "username")
    @ExcelField(value = "登录账号")
    private String username;

    @ApiParam(value = "登录密码")
    @TableField(value = "password")
    @ExcelField(value = "登录密码")
    private String password;

    @ApiParam(value = "手机号")
    @TableField(value = "phone")
    @ExcelField(value = "手机号")
    private String phone;

    @ApiParam(value = "登录页url")
    @TableField(value = "login_url")
    @ExcelField(value = "登录页url")
    private String loginUrl;

    @ApiParam(value = "home页url")
    @TableField(value = "home_url")
    @ExcelField(value = "home页url")
    private String homeUrl;

    @ApiParam(value = "电子邮箱")
    @TableField(value = "email")
    @ExcelField(value = "电子邮箱")
    private String email;

    @ApiParam(value = "辅助邮箱")
    @TableField(value = "assist_email")
    @ExcelField(value = "辅助邮箱")
    private String assistEmail;


    @ApiParam(value = "频道描述")
    @TableField(value = "channel_desc")
    @ExcelField(value = "频道描述")
    private String channelDesc;

    @ApiParam(value = "频道url")
    @TableField(value = "channel_url")
    @ExcelField(value = "频道url")
    private String channelUrl;

    @ApiParam(value = "cookie_domain")
    @TableField(value = "cookie_domain")
    @ExcelField(value = "cookie_domain")
    private String cookieDomain;

    @ApiParam(value = "每日上传上限")
    @TableField(value = "day_max")
    @ExcelField(value = "每日上传上限")
    private String dayMax;

    @ApiParam(value = "频道标题上限")
    @TableField(value = "title_limit")
    @ExcelField(value = "频道标题上限")
    private String titleLimit;

    @ApiParam(value = "频道盈利状态")
    @TableField(value = "gain_status")
    @ExcelField(value = "频道盈利状态")
    private String gainStatus;

    @ApiParam(value = "关联引擎序号")
    @TableField(value = "engine_id")
    @ExcelField(value = "关联引擎序号")
    private String engineId;

    @ApiParam(value = "账号过期时间")
    @TableField(value = "acc_due")
    @ExcelField(value = "账号过期时间")
    private Date accDue;

    @ApiParam(value = "关联引擎名称")
    @TableField(value = "engine_name")
    @ExcelField(value = "关联引擎名称")
    private transient String engineName;

    @ApiParam(value = "已关联下载项")
    @ExcelField(value = "已关联下载项")
    private transient Integer downloadCount;

    @ApiParam(value = "已配置上传数量")
    @ExcelField(value = "已配置上传数量")
    private transient Integer sumDayMax;

}
