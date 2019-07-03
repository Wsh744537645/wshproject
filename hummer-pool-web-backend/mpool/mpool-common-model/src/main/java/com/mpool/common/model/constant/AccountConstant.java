package com.mpool.common.model.constant;

public class AccountConstant {
	/**
	 * 用户Id
	 */
	public static final int USERID_LENGTH = 11;

	/**
	 * 钱包ID
	 */
	public static final int WALLETID_LENGTH = 64;

	/**
	 * 矿工ID
	 */
	public static final int WORKERID_LENGTH = 20;

	/**
	 * 货币精度
	 */
	public static final int DOUBLE_SCALE = 6;

	public static final String TABLE_PREFIX = "account_";

	/**
	 * 矿工状态
	 * 
	 * @author chenjian2
	 *
	 */
	public enum WorkerStatus {
		active(0), offline(2), inactive(1), all(null);
		private Integer status;

		private WorkerStatus(Integer status) {
			this.status = status;
		}

		public Integer getStatus() {
			return status;
		}
	}
}
