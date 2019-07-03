package com.mpool.account.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.i18n.Resource;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

	Resource findByCodeAndLocale(@Param("code") String code, @Param("locale") String locale);

}
