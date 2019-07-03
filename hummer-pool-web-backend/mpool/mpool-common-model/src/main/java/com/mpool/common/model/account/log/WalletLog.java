package com.mpool.common.model.account.log;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 钱包地址修改记录
 * 
 * @author chenjian2
 *
 */
@TableName("log_wallet")
public class WalletLog {

	@TableId
	private Integer id;

	/**
	 * 目标主键Id
	 */
	private Integer tagetId;

	/**
	 * 原钱包
	 */
	private String oldData;

	/**
	 * 新钱包
	 */
	private String newData;

	/**
	 * puid
	 */
	private Long puid;

	/**
	 * 修改人Id
	 */
	private Long updateBy;

	/**
	 * 修改时间
	 */
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTagetId() {
		return tagetId;
	}

	public void setTagetId(Integer tagetId) {
		this.tagetId = tagetId;
	}

	public Long getPuid() {
		return puid;
	}

	public void setPuid(Long puid) {
		this.puid = puid;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOldData() {
		return oldData;
	}

	public void setOldData(String oldData) {
		this.oldData = oldData;
	}

	public String getNewData() {
		return newData;
	}

	public void setNewData(String newData) {
		this.newData = newData;
	}

}
