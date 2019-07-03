package com.mpool.admin.module.account.service;

import java.util.List;

import com.mpool.common.BaseService;
import com.mpool.common.model.account.Menu;

/**
 * account 菜单管理
 * @author chenjian2
 *
 */
public interface MenuService extends BaseService<Menu> {

	List<Menu> findByUserId(String userId);

}
