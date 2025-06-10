package cc.mrbird.febs.job.task;

import cc.mrbird.febs.hope.domain.AccInfoLog;
import cc.mrbird.febs.hope.domain.AccountInfo;
import cc.mrbird.febs.hope.service.AccInfoLogService;
import cc.mrbird.febs.hope.service.AccountInfoService;
import cc.mrbird.febs.system.domain.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Component

public class ScheduleDeductTask {

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private AccInfoLogService accInfoLogService;

    //@Transactional(rollbackFor = { Exception.class })
    public void deductTask(String param){
        log.info("ScheduleDeductTask,定时扣减方法调用,参数为：{}" , param);
        LambdaQueryWrapper<AccountInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AccountInfo::getAccType, param);

        List<AccountInfo> list = accountInfoService.list(queryWrapper);

        for (AccountInfo accountInfo : list) {
            try {
                log.info("[ {} ]执行定时扣减逻辑",accountInfo.getAccName());
                //若monthRent 为null，按0计算.
                accountInfo.setBalance(accountInfo.getBalance()-(accountInfo.getMonthRent()!=null?accountInfo.getMonthRent():0));
                accountInfoService.updateById(accountInfo);
                log.info("[ {} ]执行日志记录逻辑",accountInfo.getAccName());
                AccInfoLog accInfoLog = new AccInfoLog();
                accInfoLog.setAccName(accountInfo.getAccName());
                accInfoLog.setAccId(accountInfo.getAccId());
                accInfoLog.setMonthRent(accountInfo.getMonthRent());
                accInfoLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
                accInfoLog.setLogRemark("定时扣减");
                accInfoLogService.save(accInfoLog);
            } catch (Exception e) {
                log.error("error:[{}]",e.getMessage(),e);
            }
        }
    }


}
