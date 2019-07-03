package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author chen 用户与角色
 */
@TableName(TABLE_PREFIX + "user_role")
public class UserRole {
	@TableId("`id`")
	private Integer id;
	private Long userId;
	private Integer roleId;

	public UserRole() {
		super();
	}

	public UserRole(Long userId, Integer roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SysUserRole [id=").append(id).append(", userId=").append(userId).append(", roleId=")
				.append(roleId).append("]");
		return builder.toString();
	}

}
