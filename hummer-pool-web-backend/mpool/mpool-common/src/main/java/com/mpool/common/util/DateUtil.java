package com.mpool.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil {

	public static String getYYYYMMddHH(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
		return format.format(date);
	}

	public static String getYYYYMMdd(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(date);
	}

	public static String getYYYYMMddHHmm(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		return format.format(date);
	}

	public static Date addHour(Date date, int i) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.HOUR, i);
		return rightNow.getTime();
	}

	public static Date addMinute(Date date, int i) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.MINUTE, i);
		return rightNow.getTime();
	}

	public static Date addSecond(Date date, int i){
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.SECOND, i);
		return rightNow.getTime();
	}

	public static Date addDay(Date date, int i) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.DAY_OF_YEAR, i);
		return rightNow.getTime();
	}

	public static void main(String[] args) {
		System.out.println(getPast30Days());

	}

	public static List<String> getPast30Days() {
		Date date = new Date();
		List<String> days = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < 30; i++) {
			date = DateUtils.addDays(date, -1);
			days.add(format.format(date));
		}
		return days;
	}
	public static List<String> getPast20Days() {
		Date date = new Date();
		List<String> days = new ArrayList<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		for (int i = 0; i < 4; i++) {
			date = DateUtils.addDays(date, -1);
			days.add(format.format(date));
		}
		return days;
	}

	public static List<String> getPast24Hour() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
		List<String> days = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			date = addHour(date, -1);
			days.add(format.format(date));
		}
		return days;
	}

	public static String getYYYYMMddHHmmss(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(date);
	}

	/**
	 * date2比date1多的天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDays(Date date1,Date date2)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1= cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if(year1 != year2)   //同一年
		{
			int timeDistance = 0 ;
			for(int i = year1 ; i < year2 ; i ++)
			{
				if(i%4==0 && i%100!=0 || i%400==0)    //闰年
				{
					timeDistance += 366;
				}
				else    //不是闰年
				{
					timeDistance += 365;
				}
			}

			return timeDistance + (day2-day1) ;
		}
		else    //不同年
		{
			System.out.println("判断day2 - day1 : " + (day2-day1));
			return day2-day1;
		}
	}

	/**
	 * 获得时间在当天已经过了多少秒
	 * @param date
	 */
	public static Integer getDayHadSecond(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		return (hour * 60 + minute) * 60 + second;
	}

	/**
	 * 获得0点的时间
	 * @param date
	 * @return
	 */
	public static Date getDaySecond0(Date date){
		return addSecond(date, 0 - getDayHadSecond(date));
	}

}
