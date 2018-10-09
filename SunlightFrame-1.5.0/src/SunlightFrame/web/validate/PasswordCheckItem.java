/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web.validate;

import SunlightFrame.exception.FrameException;

/**
 * 密码校验项，用来校验用户是否正确的输入了密码。 密码必须是普通字符，确认密码和密码输入的数据必须一致。
 * 
 */
public class PasswordCheckItem extends NormalCharCheckItem {
	private String confirmValue;

	/**
	 * 构造方法。
	 * 
	 * @param itemID
	 *            表单元素的ID
	 * @param confirmValue
	 *            密码的确认值。
	 * @param valueName
	 *            表单元素的名称。
	 */
	public PasswordCheckItem(String itemID, String confirmValue, String valueName) {
		super(itemID, valueName, true);
		this.itemID = itemID;
		this.itemName = valueName;
		this.confirmValue = confirmValue;
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
		boolean isLegal = false;
		if (value.equals(confirmValue) && value.matches("^[\\p{Alnum}!-_@#$%^&]{6,20}$")) {
			isLegal = true;
		}

		if (!isLegal) {
			message = getErrorMessage();
		}

		return message;
	}

	public static void main(String[] args) {
		System.out.println("!!!!!!1231111111111111111111111111"
				.matches("(?!^(\\d+|[a-zA-Z]+|[~!@#$%^&*?]+)$)^[\\w~!@#$%\\^&*?]{6,18}"));
	}
}