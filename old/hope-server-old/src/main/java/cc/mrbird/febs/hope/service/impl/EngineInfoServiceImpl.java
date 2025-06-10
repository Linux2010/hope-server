package cc.mrbird.febs.hope.service.impl;

import cc.mrbird.febs.hope.dao.EngineInfoMapper;
import cc.mrbird.febs.hope.domain.EngineInfo;
import cc.mrbird.febs.hope.service.EngineInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author fanta
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EngineInfoServiceImpl extends ServiceImpl<EngineInfoMapper, EngineInfo> implements EngineInfoService {

}

