package cc.mrbird.febs.hope.domain;

import cc.mrbird.febs.common.annotation.Trim;
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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fanta
 */
@Data
@TableName("download_info")
@Excel("下载信息表")
@ApiModel("下载信息表")
public class DownloadInfo implements Serializable {
    private static final long serialVersionUID = 126025538007498301L;

    @ApiParam(value = "自增主键")
    @TableId(value = "download_id", type = IdType.AUTO)
    private Long downloadId;
    /**
     * 组件cookie
     */
    @NotNull
    @TableField(value = "download_name")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "下载名称")
    @ApiParam(value = "下载名称")
    private String downloadName;
    /**
     * youtube，bili，douyin
     */
    @NotNull(message = "downloadType不能为空")
    @TableField(value = "download_type")
    @ExcelField(value = "下载类型")
    @ApiParam(value = "下载类型")
    private String downloadType;
    /**
     * 视频内容：美女，美食，动物...
     */
    @ApiParam(value = "视频分类")
    @TableField(value = "video_type")
    @ExcelField(value = "视频分类")
    private String videoType;
    /**
     * 下载脚本路径
     */
    @ApiParam(value = "下载脚本路径")
    @TableField(value = "shell_path")
    @ExcelField(value = "下载脚本路径")
    private String shellPath;
    /**
     * 网络代理
     */
    @ApiParam(value = "网络代理")
    @TableField(value = "agent")
    @ExcelField(value = "网络代理")
    private String agent;
    /**
     * 下载路径
     */
    @ApiParam(value = "下载路径URL")
    @TableField(value = "download_url")
    @ExcelField(value = "下载路径URL")
    @Trim
    private String downloadUrl;
    /**
     * true为开启，false位关闭
     */
    @ApiParam(value = "下载开关")
    @TableField(value = "download_enable")
    @ExcelField(value = "下载开关")
    private String downloadEnable;
    /**
     * 下载文件 存储路径
     */
    @ApiParam(value = "下载路径")
    @TableField(value = "download_path")
    @ExcelField(value = "下载路径")
    private String downloadPath;
    /**
     * 分页下载开始
     */
    @ApiParam(value = "下载开始位")
    @TableField(value = "download_start")
    @ExcelField(value = "下载开始位")
    private String downloadStart;
    /**
     * 分页下载结束
     */
    @ApiParam(value = "增量下载结束位")
    @TableField(value = "download_end")
    @ExcelField(value = "增量下载结束位")
    private String downloadEnd;

    /**
     * 全量下载结束位
     */
    @ApiParam(value = "全量下载结束位")
    @TableField(value = "download_all_end")
    @ExcelField(value = "全量下载结束位")
    private String downloadAllEnd;

    /**
     * 下载结束后执行的shell脚本路径
     */
    @ApiParam(value = "后置shell")
    @TableField(value = "after_shell_path")
    @ExcelField(value = "后置shell")
    private String afterShellPath;

    @ApiParam(value = "修改时间")
    @TableField(value = "modify_time")
    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;

    @ApiParam(value = "创建时间")
    @TableField(value = "create_time")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ApiParam(value = "下载信息描述")
    @TableField(value = "download_desc")
    @ExcelField(value = "下载信息描述")
    private String downloadDesc;

    @ApiParam(value = "引擎序号")
    @TableField(value = "engine_id")
    @ExcelField(value = "引擎序号")
    private Integer engineId;

    @ApiParam(value = "循环类型")
    @TableField(value = "cycle_type")
    @ExcelField(value = "循环类型")
    private String  cycleType;

    @ApiParam(value = "引擎名称")
    @ExcelField(value = "引擎名称")
    private transient String engineName;

    @ApiParam(value = "已关联频道")
    @ExcelField(value = "已关联频道")
    private transient Integer channelCount;

    @ApiParam(value = "视频质量")
    @ExcelField(value = "视频质量")
    private String quality;

    @ApiParam(value = "磁盘占用量")
    @ExcelField(value = "磁盘占用量")
    private String diskStorageInfo;

}

