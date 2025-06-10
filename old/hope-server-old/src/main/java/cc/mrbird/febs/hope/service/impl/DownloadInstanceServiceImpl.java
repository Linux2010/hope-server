package cc.mrbird.febs.hope.service.impl;

import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.hope.dao.DownloadInstanceMapper;
import cc.mrbird.febs.hope.domain.ChannelInfo;
import cc.mrbird.febs.hope.domain.DownloadInstance;
import cc.mrbird.febs.hope.domain.EngineInfo;
import cc.mrbird.febs.hope.service.DownloadInstanceService;
import cc.mrbird.febs.hope.service.EngineInfoService;
import cc.mrbird.febs.hope.utils.SSH2Util;
import cc.mrbird.febs.hope.utils.ShellUtils;
import ch.ethz.ssh2.Connection;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * @author fanta
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DownloadInstanceServiceImpl extends ServiceImpl<DownloadInstanceMapper, DownloadInstance> implements DownloadInstanceService {


    private static final String CLEAN_VIDEO_TEMP="cat /dev/null > '%s'";
    @Autowired
    private EngineInfoService engineInfoService;
    @Override
    public IPage<DownloadInstance> queryList(DownloadInstance info, QueryRequest request) {
        Page<ChannelInfo> page = new Page<>();
        SortUtil.handlePageSort(request, page, "createTime", FebsConstant.ORDER_DESC, true);
        return this.baseMapper.queryList(page,info);
    }

    @Override
    public void cleanVideo(DownloadInstance info) {
        EngineInfo engineInfo = engineInfoService.getById(info.getEngineId());
        if(engineInfo!=null){
            // clean video file
            log.info("cleanVideo,engineInfo:[{}]",engineInfo);
            Connection connection = SSH2Util.openConnection(engineInfo.getEngineIp(), engineInfo.getUsername(), engineInfo.getPassword());
            SSH2Util.execCommand(connection, String.format(CLEAN_VIDEO_TEMP, info.getVideoPath()));
            connection.close();
            //update instance
            DownloadInstance downloadInstance = new DownloadInstance();
            downloadInstance.setPkInstanceId(info.getPkInstanceId());
            downloadInstance.setModifyTime(new Date());
            downloadInstance.setCleanFlag(1);
            baseMapper.updateById(downloadInstance);
        }else {
            throw new RuntimeException(String.format("engineInfo，查询为空，DownloadInstance-engineId:[%s]", info.getEngineId()));
        }

    }
}

