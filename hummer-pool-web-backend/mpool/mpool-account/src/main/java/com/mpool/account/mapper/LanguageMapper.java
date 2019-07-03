package com.mpool.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.Language;

@Mapper
public interface LanguageMapper extends BaseMapper<Language> {
	List<Language> findByLangType(@Param("langType") String langType);

}
