package cc.mrbird.febs.hope.dao;

import cc.mrbird.febs.hope.domain.DownloadInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fanta
 */
public interface DownloadInfoMapper extends BaseMapper<DownloadInfo> {

    IPage<DownloadInfo> queryList(Page page, @Param("downloadInfo") DownloadInfo downloadInfo);

    List<Map<String, Object>> queryDownloadInfoByChannelName(@Param("channelName") String channelName);

    List<Map<String, Object>> statistics();
}
