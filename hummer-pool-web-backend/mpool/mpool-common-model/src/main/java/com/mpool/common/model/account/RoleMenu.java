package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(TABLE_PREFIX + "role_menu")
public class RoleMenu {
	@TableId
	private Integer id;

	private Integer roleId;

	private Integer menuId;

	protected Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
		this.id = id;
	}

	protected Integer getRoleId() {
		return roleId;
	}

	protected void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	protected Integer getMenuId() {
		return menuId;
	}

	protected void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

}
