package cc.mrbird.febs.hope.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.hope.dao.DownloadInfoMapper;
import cc.mrbird.febs.hope.domain.ChannelInfo;
import cc.mrbird.febs.hope.domain.DownloadInfo;
import cc.mrbird.febs.hope.service.DownloadInfoService;
import cc.mrbird.febs.system.domain.Dict;
import cc.mrbird.febs.system.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author fanta
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DownloadInfoServiceImpl extends ServiceImpl<DownloadInfoMapper, DownloadInfo> implements DownloadInfoService {

    @Autowired
    private DictService dictService;

    @Override
    public IPage<DownloadInfo> queryList(DownloadInfo info, QueryRequest request) {
        Page<ChannelInfo> page = new Page<>();
        SortUtil.handlePageSort(request, page, "downloadId", FebsConstant.ORDER_DESC, true);
        return this.baseMapper.queryList(page,info);
    }

    @Override
    public List<Map<String, Object>> queryDownloadInfoByChannelName(String channelName) {
        return this.baseMapper.queryDownloadInfoByChannelName(channelName);
    }

    @Override
    public List<Map<String, Object>> statistics() {
        return this.baseMapper.statistics();
    }
    @Override
    public boolean save(DownloadInfo info){
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getTableName, "download_constant");
        queryWrapper.eq(Dict::getFieldName,info.getDownloadType());
        List<Map<String, Object>> maps = dictService.listMaps(queryWrapper);
        Gson gson = new Gson();
        HashMap<String, String> newMap = new HashMap<>();
        maps.forEach(map->{
            String key = (String) map.get("keyy");
            String value = (String) map.get("valuee");
            newMap.put(key,value);
        });
        String gsonStr = gson.toJson(newMap);
        log.info("[downloadInfo save method],gsonStr:[{}]",gsonStr);
        DownloadInfo downloadInfo = gson.fromJson(gsonStr,DownloadInfo.class);
        info.setAfterShellPath(downloadInfo.getAfterShellPath());
        info.setShellPath(downloadInfo.getShellPath());
        info.setAgent(downloadInfo.getAgent());
        return retBool(this.baseMapper.insert(info));
    }

}

