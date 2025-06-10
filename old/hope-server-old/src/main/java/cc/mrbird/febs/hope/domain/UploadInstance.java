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
@TableName("upload_instance")
@Excel("上传记录表")
@ApiModel("上传记录表")
public class UploadInstance implements Serializable {
    private static final long serialVersionUID = 639649551831707956L;

    @ApiParam(value = "自增主键")
    @TableId(value = "instance_id", type = IdType.AUTO)
    private String instanceId;


    @TableField(value = "channel_name")
    @ExcelField(value = "频道名称")
    @ApiParam(value = "频道名称")
    private String channelName;

    @TableField(value = "upload_name")
    @ExcelField(value = "上传信息名称")
    @ApiParam(value = "上传信息名称")
    private String uploadName;

    @ApiParam(value = "下载信息名称")
    @TableField(value = "download_name")
    @ExcelField(value = "下载信息名称")
    private String downloadName;

    @ApiParam(value = "频道类型")
    @TableField(value = "channel_type")
    @ExcelField(value = "频道类型")
    private String channelType;

    @ApiParam(value = "实例状态")
    @TableField(value = "status")
    @ExcelField(value = "实例状态")
    //0成功 ，1失败，2执行中，3初始化
    private String status;

    @ApiParam(value = "上传日期")
    @TableField(value = "upload_date")
    @ExcelField(value = "上传日期")
    private String uploadDate;

    @ApiParam(value = "视频名称")
    @TableField(value = "video_name")
    @ExcelField(value = "视频名称")
    private String videoName;

    @ApiParam(value = "上传命令")
    @TableField(value = "upload_cmd")
    @ExcelField(value = "上传命令")
    private String uploadCmd;

    @ApiParam(value = "上传日志记录")
    @TableField(value = "upload_log")
    @ExcelField(value = "上传日志记录")
    private String uploadLog;

    @ApiParam(value = "上传时间")
    @TableField(value = "create_time")
    @ExcelField(value = "上传时间")
    private Date createTime;

    @ApiParam(value = "修改时间")
    @TableField(value = "modify_time")
    @ExcelField(value = "修改时间")
    private Date modifyTime;

    @ApiParam(value = "视频地址")
    @TableField(value = "video_url")
    @ExcelField(value = "视频地址")
    private String videoUrl;

    @ApiParam(value = "上传次数")
    @TableField(value = "upload_overflow")
    @ExcelField(value = "上传次数")
    private Integer uploadOverflow;

    @ApiParam(value = "视频路径")
    @TableField(value = "video_path")
    @ExcelField(value = "视频路径")
    private String videoPath;

    @ApiParam(value = "引擎名称")
    @TableField(value = "engine_name")
    @ExcelField(value = "引擎名称")
    private String engineName;

    @ApiParam(value = "最新失败时间")
    @ExcelField(value = "最新失败时间")
    private transient String lastFailDate;

    @ApiParam(value = "累计失败次数")
    @ExcelField(value = "累计失败次数")
    private transient Integer failCount;

}

