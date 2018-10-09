/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import java.util.Collection;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * 字符串操作工具。
 * 
 */
public class StringUtil {
	/**
	 * 打散字符串。
	 * 
	 * @param targetString
	 *            待打散的字符串。
	 * @param delim
	 *            分隔符。
	 * @return 打散后得到的字符串数组。
	 */
	public static String[] split(String targetString, String delim) {
		Vector<String> results = new Vector<String>();
		StringTokenizer token = new StringTokenizer(targetString, delim);
		while (token.hasMoreElements()) {
			results.add(token.nextToken());
		}
		String[] ss = new String[results.size()];
		for (int i = 0; i < ss.length; i++) {
			ss[i] = results.get(i);
		}
		return ss;
	}

	/**
	 * 将一组字符串拼接成一个字符串。
	 * 
	 * @param targetStrings
	 *            待拼接的字符串数组。
	 * @param delim
	 *            分隔符。
	 * @return 拼接得到的字符串。
	 */
	public static String join(String[] targetStrings, String delim) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < targetStrings.length; i++) {
			if (i != 0) {
				buf.append(delim);
			}
			buf.append(targetStrings[i]);
		}
		return buf.toString();
	}

	/**
	 * 将一组字符串拼接成一个字符串。
	 * 
	 * @param c
	 *            包含了一组字符串的容器。
	 * @param delim
	 *            分隔符。
	 * @return 拼接得到的字符串。
	 */
	public static String join(Collection<String> c, String delim) {
		StringBuffer buf = new StringBuffer();
		int i = 0;
		while (c.iterator().hasNext()) {
			if (i != 0) {
				buf.append(delim);
			}
			buf.append(c.iterator().next());
			i++;
		}
		return buf.toString();
	}

	/**
	 * 将对象转换为字符串。 当对象为null的时候，输出空串。
	 * 
	 * @param s
	 *            待转换的对象。
	 * @return 转化后得到的字符串。
	 */
	public static String getStringValueIgnoreNull(Object s) {
		if (s == null) {
			return "";
		} else {
			return s.toString();
		}
	}

	/**
	 * 读字符串进行xml转义。
	 * 
	 * @param s
	 *            待转义的字符串。
	 * @return 转义后得到的字符串。
	 */
	public static String convertXmlChars(String s) {
		String[] from = { "&", "<", ">", "\"", "\'" };
		String[] to = { "&amp;", "&lt;", "&gt;", "&quot;", "&apos;" };
		for (int i = 0; i < from.length; i++) {
			s = s.replaceAll(from[i], to[i]);
		}
		return s;
	}

	/**
	 * 读XML字符串进行转义。
	 * 
	 * @param s
	 *            待转义的字符串。
	 * @return 转义后得到的字符串。
	 */
	public static String convertFromXmlChars(String s) {
		String[] to = { "&", "<", ">", "\"", "\'", " " };
		String[] from = { "&amp;", "&lt;", "&gt;", "&quot;", "&apos;", "&nbsp;" };
		for (int i = 0; i < from.length; i++) {
			s = s.replaceAll(from[i], to[i]);
		}
		return s;
	}

	/**
	 * 得到一个指定长度的随机字符串
	 * 
	 * @return 随机字符串
	 */

	public static String getRandomString(int length) {
		String str = "23456789ABCDEFGHIJKLMNPQRSTUVWXYZ";
		String s = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			String rand = str.charAt(random.nextInt(str.length())) + "";
			s += rand;
		}
		return s;
	}

	public static String limitStringLength(String s, int length) {
		if (s.length() > length) {
			return s.substring(0, length) + "...";
		}
		return s;
	}
}
