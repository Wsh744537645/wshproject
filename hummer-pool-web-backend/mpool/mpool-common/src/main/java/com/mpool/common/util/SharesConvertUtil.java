package com.mpool.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author chenjian2 算力单位转换
 */
public class SharesConvertUtil {
	private static String dobuleToString(double d) {
		if (d % 1 == 0) {
			return String.valueOf(d).split("\\.")[0];
		} else {
			return String.valueOf(d);
		}
	}

	/**
	 * @param gh 转 EH
	 * @return
	 */
	public static String convertEH(BigDecimal th) {
		return dobuleToString(th.divide(new BigDecimal(Math.pow(10, 9))).setScale(2, RoundingMode.DOWN).doubleValue())
				+ "EH/s";
	}

	/**
	 * @param gh 转 PH
	 * @return
	 */
	public static String convertPH(BigDecimal th) {
		return dobuleToString(th.divide(new BigDecimal(Math.pow(10, 6))).setScale(2, RoundingMode.DOWN).doubleValue())
				+ "PH/s";
	}

	/**
	 * @param gh 转 TH
	 * @return
	 */
	public static String convertGH(BigDecimal th) {
		return dobuleToString(
				th.multiply(new BigDecimal(Math.pow(10, 3))).setScale(2, RoundingMode.DOWN).doubleValue()) + "GH/s";
	}

	public static void main(String[] args) {
		BigDecimal th = new BigDecimal("552960007280");
		System.out.println(convertPH(th));
	}
}
