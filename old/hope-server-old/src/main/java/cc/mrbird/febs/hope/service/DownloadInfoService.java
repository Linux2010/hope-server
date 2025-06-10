package cc.mrbird.febs.hope.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.hope.domain.DownloadInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author fanta
 */
public interface DownloadInfoService extends IService<DownloadInfo> {
    IPage<DownloadInfo> queryList(DownloadInfo info, QueryRequest request);

    List<Map<String, Object>> queryDownloadInfoByChannelName(String channelName);

    List<Map<String, Object>>statistics();

    boolean save(DownloadInfo info);


}
