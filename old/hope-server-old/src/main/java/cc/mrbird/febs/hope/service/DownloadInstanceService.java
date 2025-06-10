package cc.mrbird.febs.hope.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.hope.domain.DownloadInstance;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author fanta
 */
public interface DownloadInstanceService extends IService<DownloadInstance> {
    IPage<DownloadInstance> queryList(DownloadInstance info, QueryRequest request);

    void cleanVideo(DownloadInstance info);
}
