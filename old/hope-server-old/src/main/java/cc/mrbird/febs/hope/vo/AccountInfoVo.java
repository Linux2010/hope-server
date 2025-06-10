package cc.mrbird.febs.hope.vo;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fanta
 * @Date 2022-08-21
 */
@Data
@Excel("账号管理表")
@ApiModel("账号管理表")
public class AccountInfoVo implements Serializable {
    private static final long serialVersionUID = -38559227243331985L;

    @ApiParam(value = "自增主键")
    private Integer accId;

    @ExcelField(value = "账号名称")
    @ApiParam(value = "账号名称")
    private String accName;

    @ExcelField(value = "账号手机")
    @ApiParam(value = "账号手机")
    private String phone;

    @ApiParam(value = "账号邮箱")
    @ExcelField(value = "账号邮箱")
    private String email;

    @ApiParam(value = "账号余额")
    @ExcelField(value = "账号余额")
    private String balance;

    @ApiParam(value = "月租金")
    @ExcelField(value = "月租金")
    private String monthRent;

    @ApiParam(value = "创建时间")
    @ExcelField(value = "创建时间")
    private Date createTime;

    @ApiParam(value = "修改时间")
    @ExcelField(value = "修改时间")
    private Date modifyTime;

    @ApiParam(value = "账号描述")
    @ExcelField(value = "账号描述")
    private String accDesc;

    @ApiParam(value = "身份证")
    @ExcelField(value = "身份证")
    private String identityCard;

    @ApiParam(value = "姓名")
    @ExcelField(value = "姓名")
    private String personName;


}

