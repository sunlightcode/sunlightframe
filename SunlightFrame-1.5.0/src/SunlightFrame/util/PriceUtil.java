/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import java.text.DecimalFormat;

/**
 * 价格操作工具
 */
public class PriceUtil {

	public PriceUtil() {
	}

	public static String formatPrice(String s) {
		if (s.equals("")) {
			return s;
		}

		if (!isPrice(s)) {
			return s;
		}

		if (!isNormalPrice(s)) {
			DecimalFormat format = new DecimalFormat("0.00");
			double priceDouble = Double.parseDouble(s);
			s = format.format(priceDouble);
		}

		int i = s.indexOf(".");
		if (i > 0) {
			s = (new StringBuilder(String.valueOf(s))).append("00").toString();
			return s.substring(0, i + 3);
		} else {
			return (new StringBuilder(String.valueOf(s))).append(".00").toString();
		}
	}

	public static String formatPrice(String s, boolean isReplaceZero, int number) {
		if (isReplaceZero) {
			return formatPrice(s, 0);
		} else {
			return formatPrice(s, number);
		}
	}

	public static String formatPrice(String s, int number) {
		if (s.equals("")) {
			return s;
		}

		String res = "";
		DecimalFormat format = new DecimalFormat("0.00");
		double priceDouble = Double.parseDouble(s);
		s = format.format(priceDouble);

		int i = s.indexOf(".");
		if (i > 0) {
			s = (new StringBuilder(String.valueOf(s))).append("00").toString();
			res = s.substring(0, i + 3);
		} else {
			res = (new StringBuilder(String.valueOf(s))).append(".00").toString();
		}

		int pointIndex = res.indexOf(".");

		if (number == 0) {
			res = res.substring(0, pointIndex);
		} else if (number == 1) {
			res = res.substring(0, pointIndex + 2);
		} else if (number == 2) {
			res = res.substring(0, pointIndex + 3);
		}

		return res;

	}

	public static boolean isNormalPrice(String price) {
		return !(price.indexOf("+") != -1 || price.indexOf("-") != -1 || price.indexOf("E") != -1
				|| price.indexOf("e") != -1);
	}

	public static boolean isPrice(String price) {
		boolean isPrice = false;
		try {
			Float.parseFloat(price);
			isPrice = true;
		} catch (Exception e) {
		}
		return isPrice;
	}

	public static String minusPrice(String s, String s1) {
		s = formatPrice(s);
		s1 = formatPrice(s1);
		double d = (double) (Long.parseLong(s.replaceAll("\\.", "")) - Long.parseLong(s1.replaceAll("\\.", ""))) / 100D;
		return formatPrice((new StringBuilder(String.valueOf(d))).toString());
	}

	public static String plusPrice(String s, String s1) {
		s = formatPrice(s);
		s1 = formatPrice(s1);
		double d = (double) (Long.parseLong(s.replaceAll("\\.", "")) + Long.parseLong(s1.replaceAll("\\.", ""))) / 100D;
		return formatPrice((new StringBuilder(String.valueOf(d))).toString());
	}

	public static String multiPrice(String s, int i) {
		s = formatPrice(s);
		if (s.equals("")) {
			return "";
		}
		double d = (double) (Long.parseLong(s.replaceAll("\\.", "")) * i) / 100D;
		return formatPrice((new StringBuilder(String.valueOf(d))).toString());
	}
}
