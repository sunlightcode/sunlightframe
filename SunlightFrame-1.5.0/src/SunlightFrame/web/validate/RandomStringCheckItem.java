/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web.validate;

import SunlightFrame.exception.FrameException;

/**
 * 验证码校验项，用来校验用户是否填了验证码。
 * 
 */
public class RandomStringCheckItem extends CheckItem {
	private String randomString;

	/**
	 * 构造方法
	 * 
	 * @param itemID
	 *            表单元素ID。
	 * @param valueName
	 *            表单元素的名称。
	 */
	public RandomStringCheckItem(String itemID, String valueName, String randomString) {
		this.itemID = itemID;
		this.itemName = valueName;
		this.randomString = randomString;
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
		if (!value.equalsIgnoreCase(randomString)) {
			return getErrorMessage();
		} else {
			return "";
		}
	}
}