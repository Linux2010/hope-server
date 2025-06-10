package cc.mrbird.febs.hope.service.impl;

import cc.mrbird.febs.hope.dao.EngineInfoMapper;
import cc.mrbird.febs.hope.dao.HardDiskStorageMapper;
import cc.mrbird.febs.hope.domain.EngineInfo;
import cc.mrbird.febs.hope.domain.HardDiskStorage;
import cc.mrbird.febs.hope.service.EngineInfoService;
import cc.mrbird.febs.hope.service.HardDiskStorageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author fanta
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class HardDiskStorageServiceImpl extends ServiceImpl<HardDiskStorageMapper, HardDiskStorage> implements HardDiskStorageService {

    @Autowired
    private HardDiskStorageMapper mapper;

    @Override
    public List<HardDiskStorage> queryInfo() {
        return mapper.queryInfo();
    }
}

