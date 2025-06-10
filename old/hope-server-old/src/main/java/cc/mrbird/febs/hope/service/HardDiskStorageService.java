package cc.mrbird.febs.hope.service;

import cc.mrbird.febs.hope.domain.EngineInfo;
import cc.mrbird.febs.hope.domain.HardDiskStorage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author fanta
 */
public interface HardDiskStorageService extends IService<HardDiskStorage> {


    List<HardDiskStorage> queryInfo();


}
