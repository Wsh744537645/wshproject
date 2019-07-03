package com.mpool.common.model.admin;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mpool.common.model.AdminBaseEntity;

import java.io.Serializable;
import java.util.Date;

import static com.mpool.common.model.constant.AdminConstant.TABLE_PREFIX;

/**
 * @author chenjian2
 *
 */
@TableName(TABLE_PREFIX + "sys_user")
public class SysUser extends AdminBaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7914643730375987805L;

	@TableId
	private Long userId;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 真实姓名
	 */
	private String fullName;

	private Date loginTime;

	private String loginIp;

	/**
	 * 手机
	 */
	private String telphone;

	private String email;

	/**
	 * 0锁定人员（不再登录或已离职的）,1正常在职人员允许登录
	 * 初始值赋值 by stephen, 注释掉与生产环境保持一致
	 */
	//private Integer status = 1;

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getPassword() {
		return password;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public Integer getStatus() {
//		return status;
//	}

//	public void setStatus(Integer status) {
//		this.status = status;
//	}
}
