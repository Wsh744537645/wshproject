package com.mpool.mpoolaccountmultiplecommon.model;

import java.util.Set;

public class MenuModel implements Comparable<MenuModel> {
	private Integer menuId;
	/**
	 * 父菜单ID
	 */
	private Integer parentId;
	private String text;

	private String icon;

	private String sref;

	/**
	 * 排序
	 */
	private Integer orderNum;

	private Set<MenuModel> submenu;

	/**
	 * 菜单类型
	 */
	private Integer type;

	private String translate;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSref() {
		return sref;
	}

	public void setSref(String sref) {
		this.sref = sref;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public Set<MenuModel> getSubmenu() {
		return submenu;
	}

	public void setSubmenu(Set<MenuModel> submenu) {
		this.submenu = submenu;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTranslate() {
		return translate;
	}

	public void setTranslate(String translate) {
		this.translate = translate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MenuModel [menuId=").append(menuId).append(", parentId=").append(parentId).append(", text=")
				.append(text).append(", icon=").append(icon).append(", sref=").append(sref).append(", orderNum=")
				.append(orderNum).append(", submenu=").append(submenu).append(", type=").append(type)
				.append(", translate=").append(translate).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(MenuModel o) {
		int temp = this.getOrderNum() - o.getOrderNum();
		return temp == 0 ? 1 : temp;
	}

}
