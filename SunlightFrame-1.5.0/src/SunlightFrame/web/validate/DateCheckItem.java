/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web.validate;

import java.text.SimpleDateFormat;

import SunlightFrame.exception.FrameException;

/**
 * 日期数据校验项，用来校验用户输入是否是一个合法的日期。
 * 
 */
public class DateCheckItem extends CheckItem {
	/**
	 * 构造方法
	 * 
	 * @param itemID
	 *            表单元素ID。
	 * @param valueName
	 *            表单元素的名称。
	 * @param isNecessage
	 *            该表单元素是否是必填项。
	 */
	public DateCheckItem(String itemID, String valueName, boolean isNecessage) {
		this.itemID = itemID;
		this.itemName = valueName;
		this.isNecessary = isNecessage;
	}

	/**
	 * 得到校验结果信息。 无误的情况下返回空串，否则返回错误信息。
	 * 
	 * @param value
	 *            待校验数据。
	 * @return 校验结果信息。
	 * @throws FrameException
	 */
	public String getCheckResultMessage(String value) throws FrameException {
		String message = "";

		if (value.equals("") && !isNecessary) {
			return "";
		}

		boolean checkResult = true;
		try {
			if (value.length() == 10) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				format.parseObject(value);
			} else {
				checkResult = false;
			}
		} catch (Exception e) {
			checkResult = false;
		}
		if (!checkResult) {
			message = getErrorMessage();
		}
		return message;
	}

	public static boolean isDate(String s) {
		try {
			if (s.length() == 10) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				format.parseObject(s);
				return true;
			} else {
			}
		} catch (Exception e) {
		}
		return false;
	}
}