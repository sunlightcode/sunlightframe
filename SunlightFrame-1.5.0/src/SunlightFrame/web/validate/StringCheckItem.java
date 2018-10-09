/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web.validate;

import SunlightFrame.exception.FrameException;

/**
 * 必输入数据校验项，用来校验用户是否填了必输入的数据。
 * 
 */
public class StringCheckItem extends CheckItem {
	private int minLength = 0;
	private int maxLength = 1000000;

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
	public StringCheckItem(String itemID, String valueName, boolean isNecessage) {
		this.itemID = itemID;
		this.itemName = valueName;
		this.isNecessary = isNecessage;
	}

	/**
	 * 构造方法
	 * 
	 * @param itemID
	 *            表单元素ID。
	 * @param valueName
	 *            表单元素的名称。
	 * @param isNecessage
	 *            该表单元素是否是必填项。
	 * @param maxLength
	 *            字符串的最大长度限制。
	 */
	public StringCheckItem(String itemID, String valueName, boolean isNecessage, int maxLength) {
		this.itemID = itemID;
		this.itemName = valueName;
		this.isNecessary = isNecessage;
		this.maxLength = maxLength;
	}

	/**
	 * 构造方法
	 * 
	 * @param itemID
	 *            表单元素ID。
	 * @param valueName
	 *            表单元素的名称。
	 * @param isNecessage
	 *            该表单元素是否是必填项。
	 * @param maxLength
	 *            字符串的最大长度限制。
	 * @param minLength
	 *            字符串的最小长度限制。
	 */
	public StringCheckItem(String itemID, String valueName, boolean isNecessage, int minLength, int maxLength) {
		this.itemID = itemID;
		this.itemName = valueName;
		this.isNecessary = isNecessage;
		this.maxLength = maxLength;
		this.minLength = minLength;
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
		if (value.equals("") && isNecessary) {
			return getErrorMessage();
		} else if (value.length() < minLength || value.length() > maxLength) {
			return getErrorMessage();
		} else {
			return "";
		}
	}
}