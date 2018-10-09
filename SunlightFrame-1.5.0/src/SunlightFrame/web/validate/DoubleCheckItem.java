/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web.validate;

import SunlightFrame.exception.FrameException;

/**
 * 浮点数数据校验项，用来校验用户输入是否是一个合法的浮点数。
 * 
 */
public class DoubleCheckItem extends CheckItem {

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
	public DoubleCheckItem(String itemID, String valueName, boolean isNecessary) {
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
			Double.parseDouble(value);
		} catch (Exception e) {
			checkResult = false;
		}
		if (!checkResult) {
			message = getErrorMessage();
		}
		return message;
	}

	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
}
