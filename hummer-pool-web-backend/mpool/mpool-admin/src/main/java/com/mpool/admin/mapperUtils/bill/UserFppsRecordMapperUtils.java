package com.mpool.admin.mapperUtils.bill;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mpool.admin.exception.AdminException;
import com.mpool.admin.exception.ExceptionCode;
import com.mpool.admin.mapperUtils.bill.mapperBase.UserFppsRecordBaseMapper;
import com.mpool.admin.module.bill.mapper.UserFppsRecordMapper;
import com.mpool.admin.module_zec.bill.mapper.UserFppsRecordZecMapper;
import com.mpool.admin.module_zec.bill.model.UserFppsRecordZec;
import com.mpool.admin.utils.AdminSecurityUtils;
import com.mpool.common.model.account.fpps.UserFppsRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: stephen
 * @Date: 2019/5/13 20:23
 */

@Component
@Slf4j
public class UserFppsRecordMapperUtils implements UserFppsRecordBaseMapper {
    @Autowired
    private UserFppsRecordMapper userFppsRecordMapper;
    @Autowired
    private UserFppsRecordZecMapper userFppsRecordZecMapper;

    protected UserFppsRecordBaseMapper getInstance(){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return userFppsRecordMapper;
        }else if(currency.equals("ZEC")){
            return userFppsRecordZecMapper;
        }
        log.error("UserFppsRecordMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }

    @Override
    public List<UserFppsRecord> masterUserFppsRecord(List<Long> userList, String day) {
        return getInstance().masterUserFppsRecord(userList, day);
    }

    public UserFppsRecord selectOne(UserFppsRecord record){
        String currency = AdminSecurityUtils.getCurrencyName();
        if(currency == null || currency.equals("BTC")){
            return userFppsRecordMapper.selectOne(new QueryWrapper<UserFppsRecord>(record));
        }else if(currency.equals("ZEC")){
            UserFppsRecordZec zecRecord = (UserFppsRecordZec) record;
            return userFppsRecordZecMapper.selectOne(new QueryWrapper<UserFppsRecordZec>(zecRecord));
        }
        log.error("UserFppsRecordMapperUtils 调用失败， currency={}", AdminSecurityUtils.getCurrencyName());
        throw new AdminException(ExceptionCode.service_not_exist);
    }
}
