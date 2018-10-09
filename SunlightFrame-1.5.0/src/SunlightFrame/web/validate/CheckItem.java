/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web.validate;

import SunlightFrame.config.AppMessagesConfig;
import SunlightFrame.exception.FrameException;

/**
 * 数据验证项，用来校验用户的输入是否合法。
 * 
 */
public abstract class CheckItem {
	protected String itemID;
	protected String itemName;
	protected boolean isNecessary;

	/**
	 * 得到校验结果信息。 无误的情况下返回空串，否则返回错误信息。
	 * 
	 * @param value
	 *            待校验数据。
	 * @return 校验结果信息。
	 * @throws FrameException
	 */
	public abstract String getCheckResultMessage(String value) throws FrameException;

	/**
	 * 表单元素名称，用于生成错误提示信息。
	 * 
	 * @return 表单元素名称。
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * 表单元素ID。
	 * 
	 * @return 表单元素ID。
	 */
	public String getItemID() {
		return itemID;
	}

	String getErrorMessage() throws FrameException {
		String[] details = { itemName };
		String messageName = "check.normalError";
		return AppMessagesConfig.makeMessage(messageName, details);
	}
}
