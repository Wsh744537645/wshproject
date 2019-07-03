package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.istack.NotNull;

@TableName(TABLE_PREFIX + "alarm_setting")
public class AlarmSetting {
	/**
	 * 用户ID
	 */
	@TableId(type = IdType.INPUT)
	private Long userId;
	/**
	 * 用户算力
	 */
	@NotNull
	private Double userShare;
	/**
	 * 矿工活跃数
	 */
	@NotNull
	private Integer workerActive;

	private Date createTime;
	private Date updateTime;

	/**
	 * 通知周期 秒
	 */
	@NotNull
	private Integer cycle;
	/**
	 * 是否启用
	 */
	@NotNull
	private Boolean isEnable;

	@Valid
	@TableField(exist = false)
	private List<AlarmNotifyUser> notifyUser;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getUserShare() {
		return userShare;
	}

	public void setUserShare(Double userShare) {
		this.userShare = userShare;
	}

	public Integer getWorkerActive() {
		return workerActive;
	}

	public void setWorkerActive(Integer workerActive) {
		this.workerActive = workerActive;
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

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Integer getCycle() {
		return cycle;
	}

	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}

	public List<AlarmNotifyUser> getNotifyUser() {
		return notifyUser;
	}

	public void setNotifyUser(List<AlarmNotifyUser> notifyUser) {
		this.notifyUser = notifyUser;
	}

}
