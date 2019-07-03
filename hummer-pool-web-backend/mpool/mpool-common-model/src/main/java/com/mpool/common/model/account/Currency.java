package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(TABLE_PREFIX + "currency")
@Data
public class Currency {
	@TableId
	private Integer id;

	private String type;

	private boolean enable = false;

	private String unit;

	private Integer payModel;
}
