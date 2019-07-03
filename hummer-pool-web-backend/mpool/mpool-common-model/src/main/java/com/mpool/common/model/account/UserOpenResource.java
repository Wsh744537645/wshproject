package com.mpool.common.model.account;

import static com.mpool.common.model.constant.AccountConstant.TABLE_PREFIX;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户分享自己连接
 * 
 * @author chenjian2
 *
 */
@TableName(TABLE_PREFIX + "user_open_resource")
@Data
public class UserOpenResource {
	/**
	 * 用户ID
	 */

	private Long userId;
	/**
	 * 资源id key
	 */
	@TableId(type = IdType.INPUT)
	private String resId;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 过期时间
	 */
	private Date expiryTime;

	/**
	 * 币种
	 */
	private String currencyName;
}
