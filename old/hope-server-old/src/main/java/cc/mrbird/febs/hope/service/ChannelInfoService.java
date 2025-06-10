package cc.mrbird.febs.hope.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.hope.domain.ChannelInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface ChannelInfoService extends IService<ChannelInfo> {

    IPage<ChannelInfo> queryList(ChannelInfo user, QueryRequest queryRequest);

    List<Map<String, Object>> queryChannelInfoByDownloadName(String downloadName);

    List<Map<String, Object>> statistics();

    boolean checkChannelName(String channelName);

    boolean save(ChannelInfo info);

    void refresh(String channelName,String channelType);

    void openBack(String channelType, String channelName);
}
