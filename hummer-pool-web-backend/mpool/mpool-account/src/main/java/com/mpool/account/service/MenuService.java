package com.mpool.account.service;

import java.util.List;
import java.util.Set;

import com.mpool.account.model.MenuModel;
import com.mpool.common.model.account.Menu;

public interface MenuService {

	Set<MenuModel> transformMenuModel(List<Menu> menus);

	List<Menu> findByUserId(Long userId);

}
