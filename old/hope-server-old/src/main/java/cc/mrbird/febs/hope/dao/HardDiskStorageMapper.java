package cc.mrbird.febs.hope.dao;

import cc.mrbird.febs.hope.domain.EngineInfo;
import cc.mrbird.febs.hope.domain.HardDiskStorage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author fanta
 */
public interface HardDiskStorageMapper extends BaseMapper<HardDiskStorage> {


    /**
     * 查询engine开启状态的disk信息
     */
    @Select("SELECT hds.* from hard_disk_storage hds  inner join engine_info ei  where ei.engine_id = hds.engine_id  and ei.engine_status ='true'")
    List<HardDiskStorage> queryInfo();


}
