package cc.mrbird.febs.hope.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.hope.dao.UploadInstanceMapper;
import cc.mrbird.febs.hope.domain.ChannelInfo;
import cc.mrbird.febs.hope.domain.EngineInfo;
import cc.mrbird.febs.hope.domain.UploadInstance;
import cc.mrbird.febs.hope.service.EngineInfoService;
import cc.mrbird.febs.hope.service.UploadInstanceService;
import cc.mrbird.febs.hope.utils.SSH2Util;
import ch.ethz.ssh2.Connection;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * @author fanta
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UploadInstanceServiceImpl extends ServiceImpl<UploadInstanceMapper, UploadInstance> implements UploadInstanceService {
    @Autowired
    private EngineInfoService engineInfoService;
    @Override
    public IPage<UploadInstance> queryList(UploadInstance info, QueryRequest request) {
        Page<ChannelInfo> page = new Page<>();
        SortUtil.handlePageSort(request, page, "createTime", FebsConstant.ORDER_DESC, true);
        return this.baseMapper.queryList(page,info);
    }

    @Override
    public IPage<UploadInstance> queryFailList(QueryRequest queryRequest, UploadInstance uploadInstance) {
        Page<ChannelInfo> page = new Page<>();
        SortUtil.handlePageSort(queryRequest, page, "failCount", FebsConstant.ORDER_DESC, true);
        return this.baseMapper.queryFailList(page,uploadInstance);
    }

    @Override
    public List<Map<String, Object>> queryTrend(UploadInstance uploadInstance) {
        return this.baseMapper.queryTrend(uploadInstance);
    }

    @Override
    public List<Map<String, Object>> queryFailLog(String engineName, String videoPath) {
        return this.baseMapper.queryFailLog(engineName,videoPath);
    }

    @Override
    public void deleteInstance(String engineName, String videoPath) throws Exception {
        this.delVideoByInstance(engineName,videoPath);
        //删除记录
        this.baseMapper.deleteInstance(engineName, videoPath);
    }

    @Override
    public void delVideoByInstance(String engineName, String videoPath) throws Exception {
        //1，远程连接服务器engineName
        LambdaQueryWrapper<EngineInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EngineInfo::getEngineName,engineName);
        EngineInfo engineInfo = engineInfoService.getOne(queryWrapper);
        if(engineInfo == null){
            throw  new Exception("查询engineInfo为空");
        }
        //2，清空服务器内的video path
        Connection connection =  SSH2Util.openConnection(engineInfo.getEngineIp(),engineInfo.getUsername(),engineInfo.getPassword());
        String removeVideoCmd = "cat /dev/null > "+videoPath;
        SSH2Util.execCommand(connection, removeVideoCmd);
        connection.close();
    }

}

