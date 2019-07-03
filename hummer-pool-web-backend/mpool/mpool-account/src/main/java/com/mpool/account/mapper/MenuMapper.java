package com.mpool.account.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.account.Menu;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
	List<Menu> findByUserId(@Param("userId") Long userId);
}
