package com.mpool.admin.module.system.service.impl;

import com.mpool.admin.module.system.mapper.SysMenuMapper;
import com.mpool.admin.module.system.model.MenuModel;
import com.mpool.admin.module.system.service.SysMenuService;
import com.mpool.common.model.admin.SysMenu;
import com.mpool.common.model.aspect.annotation.Insert;
import com.mpool.common.model.aspect.annotation.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

@Service
public class SysMenuServiceImpl implements SysMenuService {

	private static final Logger logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);
	@Autowired
	private SysMenuMapper sysMenuMapper;

	@Override
	public Set<MenuModel> transformMenuModel(List<SysMenu> sysMenus) {
		Set<MenuModel> menus = new TreeSet<MenuModel>();
		long start = System.currentTimeMillis();
		List<MenuModel> list = new ArrayList<MenuModel>();
		if (sysMenus == null || sysMenus.isEmpty()) {
			return new TreeSet<>();
		}
		for (SysMenu menu : sysMenus) {
			if(menu == null){
				continue;
			}
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
	public List<SysMenu> findByUserId(Long userId) {
		return sysMenuMapper.findByUserId(userId);
	}

	@Override
	@Insert
	public void insert(SysMenu entity) {
		sysMenuMapper.insert(entity);
	}

	@Override
	public void inserts(List<SysMenu> entitys) {

	}

	@Override
	@Update
	public void update(SysMenu entity) {
//		SysMenu selectById = sysMenuMapper.selectById(entity.getMenuId());
//		if (selectById != null) {
//			BeanUtils.copyProperties(entity, selectById);
//		}
		sysMenuMapper.updateById(entity);
	}

	@Override
	public void delete(Serializable id) {
		sysMenuMapper.deleteById(id);
	}

	@Override
	public SysMenu findById(Serializable id) {
		return sysMenuMapper.selectById(id);
	}

	@Override
	public List<SysMenu> getList() {
		return sysMenuMapper.getSysMenu();
	}

}
