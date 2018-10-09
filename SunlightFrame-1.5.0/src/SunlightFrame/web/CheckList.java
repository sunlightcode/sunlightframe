/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web;

import java.util.Vector;

import SunlightFrame.exception.FrameException;
import SunlightFrame.web.validate.CheckItem;

/**
 * 表单数据录入校验队列。
 * 
 */
public class CheckList {
	private Vector<CheckItem> items = new Vector<CheckItem>();
	private JSPDataBean dataBean;

	CheckList(JSPDataBean dataBean) {
		this.dataBean = dataBean;
	}

	/**
	 * 添加一个校验项到校验队列中。
	 * 
	 * @param item
	 *            待添加的校验项。
	 */
	public void addCheckItem(CheckItem item) {
		items.add(item);
	}

	/**
	 * 校验队列开始逐个校验。
	 * 
	 * @return 如果队列中校验到某一项用户输入不正确则返回false，否则返回ture。
	 * @throws FrameException
	 */
	public boolean check() throws FrameException {
		for (int i = 0; i < items.size(); i++) {
			CheckItem item = items.get(i);
			String checkMessage = item.getCheckResultMessage(dataBean.getFormData(item.getItemID()));
			if (!checkMessage.equals("")) {
				dataBean.setControlData(FrameKeys.FOCUS_ITEM, item.getItemID());
				dataBean.setControlData(FrameKeys.ERROR_MESSAGE, checkMessage);
				return false;
			}
		}
		return true;
	}

	/**
	 * 校验队列开始逐个校验。
	 * 
	 * @return 如果队列中校验到某一项用户输入不正确则返回false，否则返回ture。
	 * @throws FrameException
	 */
	public boolean check2() throws FrameException {
		boolean result = true;
		for (int i = 0; i < items.size(); i++) {
			CheckItem item = items.get(i);
			String checkMessage = item.getCheckResultMessage(dataBean.getFormData(item.getItemID()));
			if (!checkMessage.equals("")) {
				dataBean.addErrorData(item.getItemID(), checkMessage);
				result = false;
			}
		}
		return result;
	}
}
