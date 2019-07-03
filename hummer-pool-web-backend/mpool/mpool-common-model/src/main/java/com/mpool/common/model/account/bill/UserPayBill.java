package com.mpool.common.model.account.bill;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 支付账单记录，有两种状态（待支付和已支付）， 对应表 user_pay_bill
 */

@TableName("user_pay_bill")
public class UserPayBill {
	@TableId(type = IdType.INPUT)
	protected String billNumber;
	protected String txid;
	protected Long payAllBtc;
	protected Date payAt;
	protected Integer status;
	protected Integer payCount;
	protected Date cteateAt;
	protected Long createBy;

	protected Long payBy;
	/**
	 * 币种id
	 */
	protected Integer currencyId;
	/**
	 * 操作人
	 */
	@TableField(exist = false)
	protected String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public Long getPayAllBtc() {
		return payAllBtc;
	}

	public void setPayAllBtc(Long payAllBtc) {
		this.payAllBtc = payAllBtc;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getPayAt() {
		return payAt;
	}

	public void setPayAt(Date payAt) {
		this.payAt = payAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCteateAt() {
		return cteateAt;
	}

	public void setCteateAt(Date cteateAt) {
		this.cteateAt = cteateAt;
	}

	public Integer getPayCount() {
		return payCount;
	}

	public void setPayCount(Integer payCount) {
		this.payCount = payCount;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Long getPayBy() {
		return payBy;
	}

	public void setPayBy(Long payBy) {
		this.payBy = payBy;
	}

}
