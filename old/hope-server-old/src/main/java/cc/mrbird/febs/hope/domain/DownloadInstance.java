package cc.mrbird.febs.hope.domain;

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

@Data
@TableName("download_instance")
@ApiModel("下载实例")
public class DownloadInstance implements Serializable {
    private static final long serialVersionUID = -83259505468335588L;

    @TableId(value = "pk_instance_id")
    private String pkInstanceId;

    @TableField(value = "download_id")
    private Long downloadId;

    @TableField(value = "video_file")
    private String videoFile;

    @TableField(value = "video_name")
    private String videoName;

    @TableField(value = "video_type")
    private String videoType;

    @TableField(value = "video_path")
    private String videoPath;

    @TableField(value = "clean_flag")
    private Integer cleanFlag;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "modify_time")
    private Date modifyTime;

    @TableField(value = "video_size")
    private String videoSize;

    @TableField(value = "file_create_time")
    private Date fileCreateTime;

    @TableField(value = "upload_instance_id")
    private String uploadInstanceId;

    @TableField(value = "translation_title")
    private String translationTitle;

    private transient String downloadName;

    private transient String engineId;


}
