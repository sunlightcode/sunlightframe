/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SunlightFrame.config.AppConfig;
import SunlightFrame.exception.FrameException;

/**
 * 电话号码数据校验项，用来校验用户输入是否是一个合法的电话号码。
 * 
 */
public class MobileCheckItem extends CheckItem {
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
	public MobileCheckItem(String itemID, String valueName, boolean isNecessage) {
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

		boolean checkResult = isMobile(value);

		if (!checkResult) {
			message = getErrorMessage();
		}
		return message;
	}

	// public static boolean isMobile(String s) {
	// boolean checkResult = true;
	// if (s.length() != 11 || !s.startsWith("1")) {
	// checkResult = false;
	// }
	// else {
	// try {
	// Long.parseLong(s);
	// } catch (Exception e) {
	// checkResult = false;
	// }
	// }
	// return checkResult;
	// }

	/**
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通） 177
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobile(String mobiles) {
		String mobileRegx = AppConfig.getInstance().getParameterConfig().getParameter("mobileRegx");
		if (mobileRegx == null || mobileRegx.equals("")) {
			mobileRegx = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(177))\\d{8}$";
		}
		Pattern p = Pattern.compile(mobileRegx);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static void main(String[] args) {
		System.out.println(isMobile("17701034567"));
	}
}