package com.mpool.common.model.admin;

import static com.mpool.common.model.constant.AdminConstant.TABLE_PREFIX;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mpool.common.model.AdminBaseEntity;

@TableName(TABLE_PREFIX + "sys_menu")
public class SysMenu extends AdminBaseEntity {
	/**
	 * 菜单ID
	 */
	@TableId
	private Integer menuId;

	private Boolean heading;
	/**
	 * 父菜单ID
	 */
	private Integer parentId;

	/**
	 * 默认名字
	 */
	private String text;

	/**
	 * 路由
	 */
	private String sref;

	/**
	 * 多语言
	 */
	private String translate;

	/**
	 * 菜单下面操作
	 */
	private String perms;

	/**
	 * 菜单类型
	 */
	private Integer type;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 排序
	 */
	private Integer orderNum;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Boolean getHeading() {
		return heading;
	}

	public void setHeading(Boolean heading) {
		this.heading = heading;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSref() {
		return sref;
	}

	public void setSref(String sref) {
		this.sref = sref;
	}

	public String getTranslate() {
		return translate;
	}

	public void setTranslate(String translate) {
		this.translate = translate;
	}

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

}
