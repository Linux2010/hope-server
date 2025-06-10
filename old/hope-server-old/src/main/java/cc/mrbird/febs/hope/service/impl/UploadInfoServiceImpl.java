package cc.mrbird.febs.hope.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.hope.dao.UploadInfoMapper;
import cc.mrbird.febs.hope.domain.ChannelInfo;
import cc.mrbird.febs.hope.domain.UploadInfo;
import cc.mrbird.febs.hope.service.UploadInfoService;
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
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UploadInfoServiceImpl extends ServiceImpl<UploadInfoMapper, UploadInfo> implements UploadInfoService {


    @Autowired
    private DictService dictService;


    @Override
    public IPage<UploadInfo> queryList(UploadInfo info, QueryRequest request) {
        Page<ChannelInfo> page = new Page<>();
        SortUtil.handlePageSort(request, page, "uploadId", FebsConstant.ORDER_DESC, true);
        return this.baseMapper.queryList(page,info);
    }


    @Override
    public boolean save(UploadInfo info){
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getTableName, "channel_constant");
        queryWrapper.eq(Dict::getFieldName,info.getChannelType());
        List<Map<String, Object>> maps = dictService.listMaps(queryWrapper);
        if(maps.isEmpty()){
            log.info("[uploadInfo save method],字典数据查询为空!参数tableName:[{}],fieldName:[{}]","channel_constant",info.getChannelType());
            return false;
        }
        Gson gson = new Gson();
        HashMap<String, String> newMap = new HashMap<>();
        maps.forEach(map->{
            String key = (String) map.get("keyy");
            String value = (String) map.get("valuee");
            newMap.put(key,value);
        });
        String gsonStr = gson.toJson(newMap);
        log.info("[uploadInfo save method],gsonStr:[{}]",gsonStr);
        UploadInfo uploadInfo = gson.fromJson(gsonStr,UploadInfo.class);
        info.setUploadShellPath(uploadInfo.getUploadShellPath());
        info.setUploadOk(uploadInfo.getUploadOk());
        return retBool(this.baseMapper.insert(info));
    }

}

