package com.mpool.admin.module.bill.mapper;

import com.mpool.admin.mapperUtils.bill.mapperBase.UserPayBillItemBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import com.mpool.common.model.account.bill.UserPayBillItem;

@Mapper
public interface UserPayBillItemMapper extends UserPayBillItemBaseMapper<UserPayBillItem> {

}
