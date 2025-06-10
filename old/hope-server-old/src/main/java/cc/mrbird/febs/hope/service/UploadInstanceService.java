package cc.mrbird.febs.hope.service;

import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.hope.domain.UploadInstance;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author fanta
 */
public interface UploadInstanceService extends IService<UploadInstance> {


    IPage<UploadInstance> queryList(UploadInstance info, QueryRequest request);

    IPage<UploadInstance> queryFailList(QueryRequest queryRequest, UploadInstance uploadInstance);

    List<Map<String, Object>> queryTrend(UploadInstance uploadInstance);

    List<Map<String, Object>> queryFailLog(String engineName, String videoPath);

    void deleteInstance(String engineName, String videoPath) throws Exception;


    void delVideoByInstance(String engineName, String videoPath) throws Exception;

}
