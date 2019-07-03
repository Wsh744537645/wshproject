package com.mpool.account.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpool.account.mapper.MenuMapper;
import com.mpool.account.model.MenuModel;
import com.mpool.account.service.MenuService;
import com.mpool.common.model.account.Menu;

@Service
public class MenuServiceImpl implements MenuService {

	private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

	@Autowired
	private MenuMapper menuMapper;

	@Override
	public Set<MenuModel> transformMenuModel(List<Menu> sysMenus) {
		Set<MenuModel> menus = new TreeSet<MenuModel>();
		long start = System.currentTimeMillis();
		List<MenuModel> list = new ArrayList<MenuModel>();
		for (Menu menu : sysMenus) {
			MenuModel model = new MenuModel();
			model.setIcon(menu.getIcon());
			model.setMenuId(menu.getMenuId());
			model.setOrderNum(menu.getOrderNum());
			model.setParentId(menu.getParentId());
			model.setType(menu.getType());
			model.setText(menu.getText());
			model.setSref(menu.getSref());
			model.setTranslate(menu.getTranslate());
			list.add(model);
		}
		Map<Integer, MenuModel> temp = new HashMap<Integer, MenuModel>();
		for (MenuModel menu : list) {
			temp.put(menu.getMenuId(), menu);
		}
		Set<Entry<Integer, MenuModel>> entrySet = temp.entrySet();
		for (Entry<Integer, MenuModel> entry : entrySet) {
			MenuModel son = entry.getValue();
			if (son == null) {
				logger.warn("The child menus is null");
				continue;
			}
			if (son.getParentId() != null && son.getParentId().equals(0)) {
				menus.add(son);
			} else {
				Integer getpId = son.getParentId();
				//
				MenuModel pater = temp.get(getpId);

				if (pater != null) {
					Set<MenuModel> submenu = pater.getSubmenu();
					if (submenu == null) {
						pater.setSubmenu(new TreeSet<MenuModel>());
					}
					pater.getSubmenu().add(son);
				} else {
					logger.warn("The child menus did not find the parent menu son={},pId=", son, getpId);
				}

			}
		}
		logger.debug("menu run Time Millis [{}]", System.currentTimeMillis() - start);
		return menus;
	}

	@Override
	public List<Menu> findByUserId(Long userId) {
		return menuMapper.findByUserId(userId);
	}
}
