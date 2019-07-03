package com.mpool.common.util;

import java.math.BigDecimal;

public class BTCUtil {
	public final static BigDecimal btcdivisor = new BigDecimal(100000000);
	public final static Integer scale = 8;

	public static String formatBTC(Long btc) {
		return BigDecimal.valueOf(btc).divide(btcdivisor, scale, BigDecimal.ROUND_DOWN).toPlainString();
	}

	public static String formatBTCNONull(Long btc) {
		if (btc == null || btc.equals(0L)) {
			return "0";
		} else {
			return BigDecimal.valueOf(btc).divide(btcdivisor, scale, BigDecimal.ROUND_DOWN).toPlainString();
		}
	}

	public static String formatBTCNONull(BigDecimal btc) {
		if (btc == null || btc.equals(0L)) {
			return "0";
		} else {
			return btc.divide(btcdivisor, scale, BigDecimal.ROUND_DOWN).toPlainString();
		}
	}

}
