package com.mpool.admin.module.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mpool.common.model.admin.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
	List<SysMenu> findByUserId(@Param("userId") Long userId);

	List<SysMenu> getSysMenu();
}
