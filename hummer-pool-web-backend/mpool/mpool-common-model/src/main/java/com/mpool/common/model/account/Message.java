package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(TABLE_PREFIX + "message")
public class Message {

//	CREATE TABLE `account_message` (
//			  `id` int(11) NOT NULL AUTO_INCREMENT,
//			  `title` varchar(255) DEFAULT NULL COMMENT '标题',
//			  `text` longtext COMMENT '类容base 64 ',
//			  `enabled` varchar(255) DEFAULT NULL COMMENT '是否可见',
//			  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
//			  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
//			  `keyword` varchar(255) DEFAULT NULL COMMENT '关键字',
//			  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
//			  `update_by` bigint(20) DEFAULT NULL COMMENT '修改人',
//			  `release_time` datetime DEFAULT NULL COMMENT '发布时间',
//			  `release_by` bigint(20) DEFAULT NULL,
//			  PRIMARY KEY (`id`)
//			) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	@TableId
	private Integer id;
	private String title;
	private String text;
	private Boolean enabled;
	private Date createTime;
	private Date updateTime;
	private String keyword;

	private Date releaseTime;
	private Long createBy;
	private Long updateBy;
	private Long releaseBy;
	@TableField(exist = false)
	private String releaseUser;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public Long getReleaseBy() {
		return releaseBy;
	}

	public void setReleaseBy(Long releaseBy) {
		this.releaseBy = releaseBy;
	}

	public String getReleaseUser() {
		return releaseUser;
	}

	public void setReleaseUser(String releaseUser) {
		this.releaseUser = releaseUser;
	}
}
