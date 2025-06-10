package cc.mrbird.febs.hope.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fanta
 */
@Data
@TableName("account_info")
@Excel("账号管理表")
@ApiModel("账号管理表")
public class AccountInfo implements Serializable {
    private static final long serialVersionUID = -38559227243331985L;

    @ApiParam(value = "自增主键")
    @TableId(value = "acc_id", type = IdType.AUTO)
    private Integer accId;

    @TableField(value = "acc_name")
    @ExcelField(value = "账号名称")
    @ApiParam(value = "账号名称")
    private String accName;

    @TableField(value = "phone")
    @ExcelField(value = "账号手机")
    @ApiParam(value = "账号手机")
    private String phone;

    @ApiParam(value = "手机余额")
    @TableField(value = "balance")
    @ExcelField(value = "账号余额")
    private Integer balance;

    @ApiParam(value = "手机月租")
    @TableField(value = "month_rent")
    @ExcelField(value = "手机月租")
    private Integer monthRent;


    @ApiParam(value = "账号邮箱")
    @TableField(value = "email")
    @ExcelField(value = "账号邮箱")
    private String email;

    @ApiParam(value = "邮箱密码")
    @TableField(value = "email_pwd")
    @ExcelField(value = "账号邮箱")
    private String emailPwd;

    @ApiParam(value = "身份证")
    @TableField(value = "identity_card")
    @ExcelField(value = "身份证")
    private String identityCard;

    @ApiParam(value = "姓名")
    @TableField(value = "person_name")
    @ExcelField(value = "姓名")
    private String personName;



    @ApiParam(value = "创建时间")
    @TableField(value = "create_time")
    @ExcelField(value = "创建时间")
    private Date createTime;

    @ApiParam(value = "修改时间")
    @TableField(value = "modify_time")
    @ExcelField(value = "修改时间")
    private Date modifyTime;

    @ApiParam(value = "账号描述")
    @TableField(value = "acc_desc")
    @ExcelField(value = "账号描述")
    private String accDesc;

    @ApiParam(value = "账号类型")
    @TableField(value = "acc_type")
    @ExcelField(value = "账号类型")
    private String accType;
}

