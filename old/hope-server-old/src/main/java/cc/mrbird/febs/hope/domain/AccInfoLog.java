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

import java.util.Date;

@Data
@TableName("acc_info_log")
@Excel("账号日志表")
@ApiModel("账号日志表")
public class AccInfoLog {

    @ApiParam(value = "自增主键")
    @TableId(value = "acc_log_id", type = IdType.AUTO)
    private Integer accLogId;

    @TableField(value = "acc_id")
    @ApiParam(value = "账户ID")
    @ExcelField(value = "账户ID")
    private Integer accId;

    @TableField(value = "acc_name")
    @ExcelField(value = "账号名称")
    @ApiParam(value = "账号名称")
    private String accName;

    @TableField(value = "month_rent")
    @ExcelField(value = "账号扣费")
    @ApiParam(value = "账号扣费")
    private Integer monthRent;

    @ApiParam(value = "创建时间")
    @TableField(value = "create_time")
    @ExcelField(value = "创建时间")
    private Date createTime;

    @ApiParam(value = "日志备注")
    @TableField(value = "log_remark")
    @ExcelField(value = "日志备注")
    private String logRemark;

}
