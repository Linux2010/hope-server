package cc.mrbird.febs.hope.service.impl;

import cc.mrbird.febs.hope.dao.AccountInfoMapper;
import cc.mrbird.febs.hope.domain.AccountInfo;
import cc.mrbird.febs.hope.service.AccountInfoService;
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
public class AccountInfoServiceImpl extends ServiceImpl<AccountInfoMapper, AccountInfo> implements AccountInfoService {
}

