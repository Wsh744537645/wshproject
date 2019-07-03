package com.mpool.common.model.admin;

import static com.mpool.common.model.constant.AdminConstant.TABLE_PREFIX;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.istack.NotNull;
import lombok.Data;

@TableName(TABLE_PREFIX + "alarm_setting")
@Data
public class SysAlarmSetting {
	@TableId
	private Integer id;
	/**
	 * 用户ID
	 */
	@TableField(exist = false)
	private Long userId;
	/**
	 * 池算力 低于这个值预警
	 */
	@NotNull
	@Min(0)
	@Max(1)
	private Double shareRate;
	/**
	 * 矿工活跃数 低于这个值的预警
	 */
	@NotNull
	@Min(0)
	private Integer workerNumber;

	/**
	 * 转账 预警 大于pay的值预警
	 */
	@NotNull
	private Long pay;
	@NotNull
	private Boolean block;
	/**
	 * 是否启用
	 */
	@NotNull
	private Boolean phone;
	/**
	 * 是否启用
	 */
	@NotNull
	private Boolean email;
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

	private String templateName;

	/**
	 * 生成支付单的时候告警
	 */
	private Boolean createBill;
	/**
	 * 修改密码的时候告警
	 */
	private Boolean modifyPassd;

	private Integer currencyId;
}
