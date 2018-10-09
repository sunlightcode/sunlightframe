/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web.validate;

import SunlightFrame.exception.FrameException;

/**
 * 整数数据校验项，用来校验用户输入是否是一个合法的整数。
 * 
 */
public class IntegerCheckItem extends CheckItem {
	private int minValue = Integer.MIN_VALUE;
	private int maxValue = Integer.MAX_VALUE;

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
	public IntegerCheckItem(String itemID, String valueName, boolean isNecessary) {
		this.itemID = itemID;
		this.itemName = valueName;
		this.isNecessary = isNecessary;
	}

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
	public IntegerCheckItem(String itemID, String valueName, boolean isNecessary, int minValue) {
		this(itemID, valueName, isNecessary);
		this.minValue = minValue;
	}

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
	public IntegerCheckItem(String itemID, String valueName, boolean isNecessary, int minValue, int maxValue) {
		this(itemID, valueName, isNecessary);
		this.minValue = minValue;
		this.maxValue = maxValue;
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
			long intValue = Long.parseLong(value);
			if (intValue < minValue) {
				checkResult = false;
			}
			if (intValue > maxValue) {
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

	public static boolean isInteger(String s) {
		try {
			Long.parseLong(s);
			return true;
		} catch (Exception e) {
		}
		return false;
	}
}
