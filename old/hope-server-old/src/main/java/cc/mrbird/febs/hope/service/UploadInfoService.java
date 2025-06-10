package cc.mrbird.febs.hope.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.hope.domain.UploadInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author fanta
 */
public interface UploadInfoService extends IService<UploadInfo> {

    IPage<UploadInfo> queryList(UploadInfo info, QueryRequest request);

    boolean save(UploadInfo info);

}
