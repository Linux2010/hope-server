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

/**
 * @author fanta
 */
@Data
@TableName("engine_info")
@Excel("引擎信息表")
@ApiModel("引擎信息表")
public class EngineInfo implements Serializable {
    private static final long serialVersionUID = -83259505568335585L;

    @ApiParam(value = "自增主键")
    @TableId(value = "engine_id", type = IdType.AUTO)
    private Integer engineId;

    @ApiParam(value = "引擎服务器ip")
    @TableField(value = "engine_ip")
    @ExcelField(value = "引擎服务器ip")
    private String engineIp;

    @TableField(value = "username")
    @ExcelField(value = "服务器用户")
    @ApiParam(value = "服务器用户")
    private String username;

    @TableField(value = "password")
    @ExcelField(value = "服务器密码")
    @ApiParam(value = "服务器密码")
    private String password;

    @TableField(value = "engine_desc")
    @ExcelField(value = "引擎描述")
    @ApiParam(value = "引擎描述")
    private String engineDesc;

    @TableField(value = "engine_status")
    @ExcelField(value = "引擎状态")
    @ApiParam(value = "引擎状态")
    private String engineStatus;

    @TableField(value = "engine_name")
    @ExcelField(value = "引擎名称")
    @ApiParam(value = "引擎名称")
    private String engineName;


    @TableField(value = "machine_room_id")
    @ExcelField(value = "引擎机房ID")
    @ApiParam(value = "引擎机房ID")
    private String machineRoomId;


    @TableField(value = "engine_net_status")
    @ExcelField(value = "引擎网络状态")
    @ApiParam(value = "引擎网络状态")
    private String engineNetStatus;



}

