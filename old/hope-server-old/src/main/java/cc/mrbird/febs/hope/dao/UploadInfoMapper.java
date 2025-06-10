package cc.mrbird.febs.hope.dao;

import cc.mrbird.febs.hope.domain.UploadInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author fanta
 */
public interface UploadInfoMapper extends BaseMapper<UploadInfo> {
    IPage<UploadInfo> queryList(Page page, @Param("uploadInfo") UploadInfo uploadInfo);

}
