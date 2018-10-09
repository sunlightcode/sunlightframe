/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web.validate;

import SunlightFrame.exception.FrameException;

/**
 * 价格数据校验项，用来校验用户输入是否是一个合法的浮点数。
 * 
 */
public class PriceCheckItem extends CheckItem {

	/**
	 * 构造方法
	 * 
	 * @param itemID
	 *            表单元素ID。
	 * @param valueName
	 *            表单元素的名称。
	 * @param isNecessary
	 *            该表单元素是否是必填项。
	 */
	public PriceCheckItem(String itemID, String valueName, boolean isNecessary) {
		this.itemID = itemID;
		this.itemName = valueName;
		this.isNecessary = isNecessary;
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
			double d = Double.parseDouble(value);
			if (d >= 0) {
				int dotIndex = value.indexOf(".");
				if (dotIndex > 0 && value.length() - dotIndex - 1 > 2) {
					checkResult = false;
				}
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

	public static boolean isPrice(String s) {
		try {
			double d = Double.parseDouble(s);
			if (d >= 0) {
				int dotIndex = s.indexOf(".");
				if (dotIndex < 0) {
					return true;
				} else if (dotIndex > 0 && s.length() - dotIndex - 1 <= 2) {
					return true;
				}
			} else {
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static void main(String[] args) {

		System.out.println(isPrice("1"));
	}
}
