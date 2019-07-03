package com.mpool.accountmultiple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.i18n.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

	Resource findByCodeAndLocale(@Param("code") String code, @Param("locale") String locale);

}
