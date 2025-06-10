package cc.mrbird.febs.hope.dao;

import cc.mrbird.febs.hope.domain.ChannelInfo;
import cc.mrbird.febs.hope.domain.UploadInstance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fanta
 */
public interface UploadInstanceMapper extends BaseMapper<UploadInstance> {

    IPage<UploadInstance> queryFailList(Page page, @Param("uploadInstance") UploadInstance uploadInstance);

    List<Map<String, Object>> queryTrend(@Param("uploadInstance") UploadInstance uploadInstance);

    List<Map<String, Object>> queryFailLog(@Param("engineName") String engineName, @Param("videoPath") String videoPath);

    Integer deleteInstance(@Param("engineName") String engineName, @Param("videoPath") String videoPath);

    IPage<UploadInstance> queryList(Page<ChannelInfo> page, @Param("uploadInstance") UploadInstance uploadInstance);
}
