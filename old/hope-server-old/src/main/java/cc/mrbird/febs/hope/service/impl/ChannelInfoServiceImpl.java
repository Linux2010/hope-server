package cc.mrbird.febs.hope.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.hope.dao.ChannelInfoMapper;
import cc.mrbird.febs.hope.domain.ChannelInfo;
import cc.mrbird.febs.hope.service.ChannelInfoService;
import cc.mrbird.febs.hope.utils.ShellUtils;
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
import org.springframework.beans.factory.annotation.Value;
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
public class ChannelInfoServiceImpl extends ServiceImpl<ChannelInfoMapper, ChannelInfo> implements ChannelInfoService {

    @Autowired
    private DictService dictService;

    @Override
    public IPage<ChannelInfo> queryList(ChannelInfo channelInfo, QueryRequest queryRequest) {
        Page<ChannelInfo> page = new Page<>();
        SortUtil.handlePageSort(queryRequest, page, "createTime", FebsConstant.ORDER_DESC, true);
        return this.baseMapper.queryList(page,channelInfo);
    }

    @Override
    public List<Map<String, Object>> queryChannelInfoByDownloadName(String downloadName) {
        return this.baseMapper.queryChannelInfoByDownloadName(downloadName);
    }

    @Override
    public List<Map<String, Object>> statistics() {
        return this.baseMapper.statistics();
    }

    @Override
    public boolean checkChannelName(String channelName) {
        int i = this.baseMapper.countByChannelName(channelName);
        return i == 0;
    }
    @Override
    public boolean save(ChannelInfo info){
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getTableName, "channel_constant");
        queryWrapper.eq(Dict::getFieldName,info.getChannelType());
        List<Map<String, Object>> maps = dictService.listMaps(queryWrapper);
        Gson gson = new Gson();
        HashMap<String, String> newMap = new HashMap<>();
        maps.forEach(map->{
           String key = (String) map.get("keyy");
           String value = (String) map.get("valuee");
           newMap.put(key,value);
        });
        String gsonStr = gson.toJson(newMap);
        log.info("[channelInfo save method],gsonStr:[{}]",gsonStr);
        ChannelInfo channelInfo = gson.fromJson(gsonStr,ChannelInfo.class);
        info.setLoginUrl(channelInfo.getLoginUrl());
        info.setHomeUrl(channelInfo.getHomeUrl());
        info.setCookieDomain(channelInfo.getCookieDomain());
        info.setTitleLimit(channelInfo.getTitleLimit());
        info.setCookieEnable("true");
        return retBool(this.baseMapper.insert(info));
    }

    @Override
    public void refresh(String channelName, String channelType) {
        String cmd = null;
        if("youtube".equals(channelType)){
            cmd = engineShellPath+"/you_login.sh "+channelName;
        }else if("bili".equals(channelType)){
            cmd = engineShellPath+"/bili-login.sh "+channelName;
        }else {
            cmd = engineShellPath+"/refresh_channel.sh "+channelName;
        }
        log.info("execCmd: [{}]",cmd);
        ShellUtils.execCmd(cmd);
    }


    @Value("${hope.engine-shell-path}")
    private String engineShellPath;

    @Override
    public void openBack(String channelType, String channelName) {
        String cmd = null;
        if("youtube".equals(channelType)){
            ShellUtils.execCmd(engineShellPath+"/stop-cmd.sh go_youtube");
            cmd = engineShellPath+"/go_youtube.sh "+channelName;
        } else if ("bili".equals(channelType)) {
            ShellUtils.execCmd(engineShellPath+"/stop-cmd.sh go_bili");
            cmd = engineShellPath+"/go_bili.sh "+channelName;
        } else {
            ShellUtils.execCmd(engineShellPath+"/stop-cmd.sh go_channel");
            cmd = engineShellPath+"/go_channel.sh "+channelName;
        }
        log.info("execCmd: [{}]",cmd);
        ShellUtils.execCmd(cmd);
    }

}

