package com.mpool.common.model.admin;

import static com.mpool.common.model.constant.AdminConstant.TABLE_PREFIX;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mpool.common.model.AdminBaseEntity;

@TableName(TABLE_PREFIX + "sys_user_sys_role")
public class SysUserSysRole extends AdminBaseEntity {
	@TableField
	private Integer id;

	private Long userId;

	private Integer roleId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
