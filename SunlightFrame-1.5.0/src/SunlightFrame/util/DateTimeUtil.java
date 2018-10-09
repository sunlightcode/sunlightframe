/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 日期与时间工具。
 */
public class DateTimeUtil {
	private DateTimeUtil() {

	}

	/**
	 * 得到当期日期。日期的格式为"yyyy-MM-dd"。
	 * 
	 * @return 当前日期。
	 */
	public static String getCurrentDate() {
		return getFormattedDate("yyyy-MM-dd");
	}

	/**
	 * 得到当期日期与时间。日期的格式为"yyyy-MM-dd HH:mm:ss"。
	 * 
	 * @return 当期日期与时间。
	 */
	public static String getCurrentDateTime() {
		return getFormattedDate("yyyy-MM-dd HH:mm:ss");
	}

	private static String getFormattedDate(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern(pattern);
		return format.format(Calendar.getInstance().getTime());
	}

	/**
	 * 根据年月日生成日期字符串
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return 日期字符串
	 */
	public static String makeDateString(String year, String month, String day) {
		return year + "-" + (month.length() == 1 ? "0" : "") + month + "-" + (day.length() == 1 ? "0" : "") + day;
	}
}
