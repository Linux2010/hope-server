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
@TableName("hard_disk_storage")
@Excel("存储信息表")
@ApiModel("存储信息表")
public class HardDiskStorage implements Serializable {
    private static final long serialVersionUID = -83259505568335585L;

    @ApiParam(value = "自增主键")
    @TableId(value = "storage_id", type = IdType.AUTO)
    private Integer storageId;

    @ApiParam(value = "引擎服务器id")
    @TableField(value = "engine_id")
    @ExcelField(value = "引擎服务器id")
    private String engineId;

    @TableField(value = "storage_path")
    @ExcelField(value = "存储路径")
    @ApiParam(value = "存储路径")
    private String storagePath;

    @TableField(value = "storage_desc")
    @ExcelField(value = "存储备注")
    @ApiParam(value = "存储备注")
    private String storageDesc;


    @TableField(value = "total_size")
    @ExcelField(value = "存储容量")
    @ApiParam(value = "存储容量")
    private String totalSize;


    @TableField(value = "used_percentage")
    @ExcelField(value = "使用率")
    @ApiParam(value = "使用率")
    private String usedPercentage;


    @TableField(value = "create_time")
    @ExcelField(value = "创建时间")
    @ApiParam(value = "创建时间")
    private Date createTime;

    @TableField(value = "modify_time")
    @ExcelField(value = "更新时间")
    @ApiParam(value = "更新时间")
    private Date modifyTime;

}

