package com.mpool.common.constant;

public final class CommConstant {
	public enum Coin {
		BTC(1);

		private Integer value;

		Coin(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

	/**
	 * 收益类型
	 * 
	 * @author chenjian2
	 *
	 */
	public enum RevenueType {

		/**
		 * 算力收益
		 */
		Share(1),
		/**
		 * 推荐人收益
		 */
		Recommend(2);

		private int value;

		RevenueType(int value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}
}
