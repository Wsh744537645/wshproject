package com.mpool.common.model.account;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

@TableName(TABLE_PREFIX + "_recommend")
@Data
public class RecommendUser implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 9020204956664763485L;

	/**
	 * 推荐人Id
	 */
	@TableId
	private Long  recommendId;
	/**
	 * 推荐人名称
	 */
	private String recommendName;

	/**
	 * 被推荐的userId 子账号
	 */
	private Long userId;

	/**
	 * 创建时间
	 */
	private Date createdTime;

	/**
	 * 被推荐人的主账号id
	 */
	private Long masterId;

	/**
	 * 推荐人费率
	 */
	private Float feeRate;

	/**
	 * 币种
	 */
	private int currencyId;
}
