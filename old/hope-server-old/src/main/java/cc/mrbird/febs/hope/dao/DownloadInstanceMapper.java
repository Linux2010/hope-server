package cc.mrbird.febs.hope.dao;

import cc.mrbird.febs.hope.domain.ChannelInfo;
import cc.mrbird.febs.hope.domain.DownloadInstance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author fanta
 */
public interface DownloadInstanceMapper extends BaseMapper<DownloadInstance> {

    IPage<DownloadInstance> queryList(Page<ChannelInfo> page, @Param("downloadInstance")  DownloadInstance info);
}
