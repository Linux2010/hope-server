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

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fanta
 */
@Data
@TableName("upload_info")
@Excel("下载信息表")
@ApiModel("下载信息表")
public class UploadInfo implements Serializable {
    private static final long serialVersionUID = 156950732444522884L;

    @ApiParam(value = "自增主键")
    @TableId(value = "upload_id", type = IdType.AUTO)
    private Long uploadId;

    @TableField(value = "upload_name")
    @ExcelField(value = "上传信息名称")
    @ApiParam(value = "上传信息名称")
    private String uploadName;
    /**
     * true,false
     */
    @ApiParam(value = "上传开关")
    @TableField(value = "upload_enable")
    @ExcelField(value = "上传开关")
    private String uploadEnable;
    /**
     * 5
     */
    @ApiParam(value = "上传信息名称")
    @TableField(value = "upload_day_max")
    @ExcelField(value = "每日上传上限")
    private String uploadDayMax;
    /**
     * 上传内容路径
     */
    @ApiParam(value = "上传路径")
    @TableField(value = "upload_path")
    @ExcelField(value = "上传路径")
    private String uploadPath;
    /**
     * 上传脚本路径
     */
    @ApiParam(value = "上传执行脚本")
    @TableField(value = "upload_shell_path")
    @ExcelField(value = "上传执行脚本")
    private String uploadShellPath;
    /**
     * 播放列表
     */
    @ApiParam(value = "播放列表")
    @TableField(value = "upload_play_list")
    @ExcelField(value = "播放列表")
    private String uploadPlayList;
    /**
     * channel item 类型
     */
    @ApiParam(value = "上传item类型")
    @TableField(value = "upload_item")
    @ExcelField(value = "上传item类型")
    private String uploadItem;
    /**
     * channel 标签
     */
    @ApiParam(value = "上传标签")
    @TableField(value = "upload_label")
    @ExcelField(value = "上传标签")
    private String uploadLabel;
    /**
     * 描述
     */
    @ApiParam(value = "上传信息名称")
    @TableField(value = "upload_desc")
    @ExcelField(value = "上传信息描述")
    private String uploadDesc;
    /**
     * 下载信息外键
     */
    @ApiParam(value = "下载信息名称")
    @TableField(value = "download_name")
    @ExcelField(value = "下载信息名称")
    private String downloadName;
    /**
     * channel info外键
     */
    @ApiParam(value = "频道信息名称")
    @TableField(value = "channel_name")
    @ExcelField(value = "频道信息名称")
    private String channelName;

    @ApiParam(value = "修改时间")
    @TableField(value = "modify_time")
    @ExcelField(value = "修改时间")
    private Date modifyTime;

    @ApiParam(value = "创建时间")
    @TableField(value = "create_time")
    @ExcelField(value = "创建时间")
    private Date createTime;
    /**
     * ok文件后缀
     */
    @ApiParam(value = "上传ok标志")
    @TableField(value = "upload_ok")
    @ExcelField(value = "上传ok标志")
    private String uploadOk;

    @ApiParam(value = "下载类型")
    @ExcelField(value = "下载类型")
    private String downloadType;

    @NotNull(message = "channelType不能为空")
    @ApiParam(value = "频道类型")
    @ExcelField(value = "频道类型")
    transient private String channelType;

    @ApiParam(value = "cookie预警")
    @ExcelField(value = "cookie预警")
    private String cookieEnable;

}

