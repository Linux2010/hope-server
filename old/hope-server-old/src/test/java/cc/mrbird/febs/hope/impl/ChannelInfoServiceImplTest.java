package cc.mrbird.febs.hope.impl;

import cc.mrbird.febs.hope.domain.ChannelInfo;
import cc.mrbird.febs.hope.service.impl.ChannelInfoServiceImpl;
import cc.mrbird.febs.job.util.OkHttpClientUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChannelInfoServiceImplTest {

    @Autowired
    private ChannelInfoServiceImpl service;

    @Test
    public void testSave() {
        ChannelInfo info = new ChannelInfo();
        info.setChannelName("test1");
        info.setChannelType("youtube");
        service.save(info);
        LambdaQueryWrapper<ChannelInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ChannelInfo::getChannelName, "test2");
        service.remove(queryWrapper);
    }

}