package cc.mrbird.febs.hope.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.domain.FebsConstant;
import cc.mrbird.febs.common.domain.FebsResponse;
import cc.mrbird.febs.common.domain.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.hope.domain.AccountInfo;
import cc.mrbird.febs.hope.service.AccountInfoService;
import cc.mrbird.febs.hope.utils.Constant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @author fanta
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("accountInfo")
@Api(value = "/accountInfo",tags={"账号管理"})
public class AccountInfoController extends BaseController {
    private String messageDetail;
    @Autowired
    private AccountInfoService service;


    @GetMapping("/list")
    @ApiOperation(value="账号管理-分页list接口",notes = "GET请求" )
    @Log("查询账号")
    public Map<String, Object> list(QueryRequest request, AccountInfo info) {
        Page<AccountInfo> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", FebsConstant.ORDER_DESC, true);
        LambdaQueryWrapper<AccountInfo> cqw = new LambdaQueryWrapper<>();
        cqw.like(StringUtils.isNotBlank(info.getAccName()),AccountInfo::getAccName,info.getAccName());
        return getDataTable(service.page(page,cqw));
    }

    @PostMapping("/add")
    @ApiOperation(value="账号管理-新增接口",notes = "POST请求" )
    public FebsResponse addAccountInfo(AccountInfo info) {
        if(StringUtils.isBlank(info.getAccName())){
            messageDetail="参数不合法,accName必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",messageDetail);
        }
        int count = this.service.count(new LambdaQueryWrapper<AccountInfo>().eq(AccountInfo::getAccName,info.getAccName()));
        if (count !=0){
            messageDetail="accName重复，请换一个";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",messageDetail);
        }
        try {
            info.setCreateTime(new Date());
            info.setModifyTime(new Date());
            this.service.save(info);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            messageDetail = "新增失败\n"+e.getMessage();
            log.error(messageDetail, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",messageDetail);
        }
    }

    @PostMapping("/update")
    public FebsResponse updateAccountInfo(AccountInfo info)  {
        if(info.getAccId() == null){
            messageDetail="参数不合法,accId必传";
            return new FebsResponse().message(Constant.MESSAGE_BAD_REQUEST).put("messageDetail",messageDetail);
        }
        try {
            info.setAccName(null);
            info.setModifyTime(new Date());
            this.service.updateById(info);
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            messageDetail = "修改失败\n"+e.getMessage();
            log.error(messageDetail, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",messageDetail);
        }
    }

    @PostMapping("/{accountIds}")
    public FebsResponse deleteAccounts(@NotBlank(message = "{required}") @PathVariable String accountIds) throws FebsException {
        try {
            String[] ids = accountIds.split(StringPool.COMMA);
            this.service.removeByIds(Arrays.asList(ids));
            return new FebsResponse().message(Constant.MESSAGE_SUCCESS);
        } catch (Exception e) {
            messageDetail = "删除失败"+e.getMessage();
            log.error(messageDetail, e);
            return new FebsResponse().message(Constant.MESSAGE_ERROR).put("messageDetail",messageDetail);
        }
    }
}
