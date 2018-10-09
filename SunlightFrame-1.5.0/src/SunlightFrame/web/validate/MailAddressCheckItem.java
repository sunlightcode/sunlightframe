/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web.validate;

import java.util.regex.Pattern;

import SunlightFrame.exception.FrameException;

/**
 * 邮件地址校验项，用来校验用户输入是否是一个合法的邮件地址。
 * 
 */
public class MailAddressCheckItem extends CheckItem {
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
	public MailAddressCheckItem(String itemID, String valueName, boolean isNecessage) {
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

		boolean checkResult = Pattern.matches("[a-zA-Z_.\\-0-9]+?@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
				value);

		if (!checkResult) {
			message = getErrorMessage();
		}
		return message;
	}

	public static boolean isMailAddress(String s) {
		return Pattern.matches("[a-zA-Z_.\\-0-9]+?@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", s);
	}

	public static void main(String[] args) {
		System.out.println(isMailAddress("rebornsteven@sina.com"));
	}
}