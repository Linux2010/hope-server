package cc.mrbird.febs.hope.dao;

import cc.mrbird.febs.hope.domain.ChannelInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author fanta
 */
public interface ChannelInfoMapper extends BaseMapper<ChannelInfo> {

    IPage<ChannelInfo> queryList(Page page, @Param("channelInfo") ChannelInfo channelInfo);

    List<Map<String, Object>> queryChannelInfoByDownloadName(@Param("downloadName") String downloadName);

    List<Map<String, Object>> statistics();

    @Select("select count(1) from channel_info where channel_name = #{channelName}")
    int countByChannelName(String channelName);
}
