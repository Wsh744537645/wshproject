package com.mpool.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class UUIDUtil {
	public static String generateUUIDUpperCase() {
		return UUID.randomUUID().toString().replace("-", "").toUpperCase();
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String generateNumber(int length) {
		String sources = "0123456789"; // 加上一些字母，就可以生成pc站的验证码了
		Random rand = new Random();
		StringBuffer flag = new StringBuffer();
		for (int j = 0; j < length; j++) {
			flag.append(sources.charAt(rand.nextInt(9)) + "");
		}
		return flag.toString();
	}

	private static DateFormat format = new SimpleDateFormat("YYMMddHH");
	private static DateFormat format3 = new SimpleDateFormat("yyyyMMdd");
	private static DateFormat format44 = new SimpleDateFormat("yyyy-MM-dd");
	public static int generateYYMMDDHH() {
		String format2 = format.format(new Date());
		return Integer.parseInt(format2);
	}
	public static String generateyyyyMMdd() {
		String format4 = format3.format(new Date());
		return format4;
	}

	public static String generateyyyy_MM_dd() {
		String format4 = format44.format(new Date());
		return format4;
	}
}
