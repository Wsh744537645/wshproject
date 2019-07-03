package com.mpool.common.model.account.bill;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 每天8点产生支付账单（待生成账单数据结构），对应表 user_pay_bill_item
 */

@TableName("user_pay_bill_item")
public class UserPayBillItem {
	@TableId
	protected Long id;
	protected String billNumber;
	protected Long puid;
	protected String walletAddress;
	protected Long payBtc;
	protected Date cteateAt;
	protected Long day;
	@TableField("`desc`")
	protected String desc;

	@TableField(exist = false)
	protected Long payBtcSum;
	@TableField(exist = false)
	protected String username;

	/**
	 * 获得支出的类型（对应收益记录表user_fee_record的fee_type字段）
	 * @return
	 */
	public int getFeeType(){
		// TODO: 2019/4/3  此处还需要完善，支出类型用描述来判断区分不合适

		if(desc.equals("打币")){
			return 3;
		}else if(desc.equals("奖励")){
			return 4;
		}else if(desc.equals("补发")){
			return 5;
		}
		return 6;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Long getPuid() {
		return puid;
	}

	public void setPuid(Long puid) {
		this.puid = puid;
	}

	public String getWalletAddress() {
		return walletAddress;
	}

	public void setWalletAddress(String walletAddress) {
		this.walletAddress = walletAddress;
	}

	public Long getPayBtc() {
		return payBtc;
	}

	public void setPayBtc(Long payBtc) {
		this.payBtc = payBtc;
	}

	public Date getCteateAt() {
		return cteateAt;
	}

	public void setCteateAt(Date cteateAt) {
		this.cteateAt = cteateAt;
	}

	public Long getDay() {
		return day;
	}

	public void setDay(Long day) {
		this.day = day;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPayBtcSum() {
		return payBtcSum;
	}

	public void setPayBtcSum(Long payBtcSum) {
		this.payBtcSum = payBtcSum;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
