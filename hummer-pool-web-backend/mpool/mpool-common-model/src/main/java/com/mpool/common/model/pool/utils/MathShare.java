package com.mpool.common.model.pool.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 算力计算公式
 */
public class MathShare {

	public static int currencyId = 1; //当前币种,启动服务时候赋值一次

	private static final Logger log = LoggerFactory.getLogger(MathShare.class);

	public static final int DAY = 24 * 60 * 60;

	private static final int HOUR = 60 * 60;

	/**
	 *
	 */
	private static final int MINUTE = 60 * 60;

	/**
	 * 小数点后面多少位
	 */
	private static final int SCALE = 3;

	/**
	 * 不四舍五入
	 */
	private static final int ROUNDING_MODE = BigDecimal.ROUND_FLOOR;

	/**
	 * pow(2,32)
	 */
	public static final double pow2 = Math.pow(2, 32);

	/**
	 * pow(10,12)
	 */
	public static final double pow10 = Math.pow(10, 12);

	private static final double zec_pow2 = Math.pow(2, 13);
	private static final double zec_pow10 = Math.pow(10, 3);


	private static String getShareString(Long share, int shareWindow){
		String plainString = "0";
		try {
			if (share != null && share != 0) {
				if(currencyId == 0 || currencyId == 1) {
					//btc
					plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(pow2))
							.divide(BigDecimal.valueOf(shareWindow), SCALE, ROUNDING_MODE)
							.divide(BigDecimal.valueOf(pow10), SCALE, ROUNDING_MODE).toPlainString();
				}else if(currencyId == 2){
					//zec
					plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(zec_pow2))
							.divide(BigDecimal.valueOf(shareWindow), SCALE, ROUNDING_MODE)
							.divide(BigDecimal.valueOf(zec_pow10), SCALE, ROUNDING_MODE).toPlainString();
				}
				else{
					log.error("Math share error, currency not exist, currencyId={}", currencyId);
				}
			}
		} catch (Exception e) {
			log.error("Math share error, currencyId={}, e={}", currencyId, e);
		}

		return plainString;
	}

	private static Double getShareDouble(Long share, int shareWindow){
		Double plainString = 0d;
		try {
			if (share != null && share != 0) {
				if(currencyId == 0 || currencyId == 1) {
					//btc
					plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(pow2))
							.divide(BigDecimal.valueOf(shareWindow), SCALE, ROUNDING_MODE)
							.divide(BigDecimal.valueOf(pow10), SCALE, ROUNDING_MODE).doubleValue();
				}else if(currencyId == 2){
					//zec
					plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(zec_pow2))
							.divide(BigDecimal.valueOf(shareWindow), SCALE, ROUNDING_MODE)
							.divide(BigDecimal.valueOf(zec_pow10), SCALE, ROUNDING_MODE).doubleValue();
				}else{
					log.error("Math share error, currency not exist, currencyId={}", currencyId);
				}
			}
		} catch (Exception e) {
			log.error("Math share error, currencyId={}, e={}", currencyId, e);
		}

		return plainString;
	}

	/**
	 * sql:SELECT (2191879204480 *  POW(2,32)) / (24*60*60) /POW(10,12)
	 * @param share
	 * @return
	 */
	public static String MathShareDay(Long share) {
		return getShareString(share, DAY);
	}

	public static String MathShareHour(Long share) {
//		String plainString = "0";
//		try {
//			if (share != null && share != 0) {
//				plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(pow2))
//						.divide(BigDecimal.valueOf(HOUR), SCALE, ROUNDING_MODE)
//						.divide(BigDecimal.valueOf(pow10), SCALE, ROUNDING_MODE).toPlainString();
//			}
//		} catch (Exception e) {
//			log.error("Math share error, currencyId={}, e={}", currencyId, e);
//		}
		return getShareString(share, HOUR);
	}

	public static String MathShareMinute(Long share) {
//		String plainString = "0";
//		try {
//			if (share != null && share != 0) {
//				plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(pow2))
//						.divide(BigDecimal.valueOf(MINUTE), SCALE, ROUNDING_MODE)
//						.divide(BigDecimal.valueOf(pow10), SCALE, ROUNDING_MODE).toPlainString();
//			}
//		} catch (Exception e) {
//			log.error("Math share error, currencyId={}, e={}", currencyId, e);
//		}
		return getShareString(share, MINUTE);
	}

	public static double MathShareMinuteDouble(Long share) {
//		double plainString = 0;
//		try {
//			if (share != null && share != 0) {
//				plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(pow2))
//						.divide(BigDecimal.valueOf(MINUTE), SCALE, ROUNDING_MODE)
//						.divide(BigDecimal.valueOf(pow10), SCALE, ROUNDING_MODE).doubleValue();
//			}
//		} catch (Exception e) {
//			log.error("Math share error", e);
//		}
		return getShareDouble(share, MINUTE);
	}

	public static double MathShareHourDouble(Long share) {
//		double plainString = 0;
//		try {
//			if (share != null && share != 0) {
//				plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(pow2))
//						.divide(BigDecimal.valueOf(HOUR), SCALE, ROUNDING_MODE)
//						.divide(BigDecimal.valueOf(pow10), SCALE, ROUNDING_MODE).doubleValue();
//			}
//		} catch (Exception e) {
//			log.error("Math share error", e);
//		}
		return getShareDouble(share, HOUR);
	}

	public static double MathShareDayDouble(Long share) {
//		double plainString = 0;
//		try {
//			if (share != null && share != 0) {
//				plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(pow2))
//						.divide(BigDecimal.valueOf(DAY), SCALE, ROUNDING_MODE)
//						.divide(BigDecimal.valueOf(pow10), SCALE, ROUNDING_MODE).doubleValue();
//			}
//		} catch (Exception e) {
//			log.error("Math share error", e);
//		}
		return getShareDouble(share, DAY);
	}

	public static String mathShare(Long share, int shareWindow) {
//		String plainString = "0";
//		try {
//			if (share != null && share != 0) {
//				plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(pow2))
//						.divide(BigDecimal.valueOf(shareWindow), SCALE, ROUNDING_MODE)
//						.divide(BigDecimal.valueOf(pow10), SCALE, ROUNDING_MODE).toPlainString();
//			}
//		} catch (Exception e) {
//			log.error("Math share error", e);
//		}
		return getShareString(share, shareWindow);
	}

	public static Double mathShareDouble(Long share, int shareWindow) {
//		Double plainString = 0d;
//		try {
//			if (share != null && share != 0) {
//				plainString = BigDecimal.valueOf(share).multiply(BigDecimal.valueOf(pow2))
//						.divide(BigDecimal.valueOf(shareWindow), SCALE, ROUNDING_MODE)
//						.divide(BigDecimal.valueOf(pow10), SCALE, ROUNDING_MODE).doubleValue();
//			}
//		} catch (Exception e) {
//			log.error("Math share error", e);
//		}
		return getShareDouble(share, shareWindow);
	}
}
