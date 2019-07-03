package com.mpool.common.model.admin;

import static com.mpool.common.model.constant.AdminConstant.TABLE_PREFIX;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mpool.common.model.AdminBaseEntity;

@TableName(TABLE_PREFIX + "sys_role")
public class SysRole extends AdminBaseEntity {
	/**
	 * 主键角色Id
	 */
	@TableId
	private Integer roleId;

	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 0 删除 1有效
	 */
	private String roleFlag;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleFlag() {
		return roleFlag;
	}

	public void setRoleFlag(String roleFlag) {
		this.roleFlag = roleFlag;
	}

}
