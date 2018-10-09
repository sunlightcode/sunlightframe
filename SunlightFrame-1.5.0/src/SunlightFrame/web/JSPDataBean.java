package SunlightFrame.web;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import SunlightFrame.util.StringUtil;

/**
 * 提供对所有数据的集中管理。 包括：表单数据、会话数据、控制用数据以及一般数据
 */
public class JSPDataBean {
	private String frameHiddenHtml = "";
	private String outputHtml = "";
	private Hashtable<String, String> controlDatas = new Hashtable<String, String>();
	private Hashtable<String, String> formDatas = new Hashtable<String, String>();
	private Hashtable<String, Object> jspDatas = new Hashtable<String, Object>();
	private Vector<Hashtable<String, String>> errorDatas = new Vector<Hashtable<String, String>>();

	public String getControlData(String dataName) {
		String value = (String) this.controlDatas.get(dataName);
		return ((value == null) ? "" : value);
	}

	public Hashtable<String, String> getControlDatas() {
		return this.controlDatas;
	}

	public void setControlData(String dataName, String data) {
		this.controlDatas.put(dataName, data);
	}

	public void setControlData(Hashtable<String, String> datas) {
		setHashByHash(this.controlDatas, datas);
	}

	public String getFormData(String dataName) {
		String value = (String) this.formDatas.get(dataName);
		return ((value == null) ? "" : value);
	}

	public String getOutputFormData(String dataName) {
		String value = (String) this.formDatas.get(dataName);
		return ((value == null) ? "" : StringUtil.convertXmlChars(value));
	}

	public Hashtable<String, String> getFormDatas() {
		return this.formDatas;
	}

	void setFormData(String dataName, String data) {
		this.formDatas.put(dataName, data);
	}

	void setFormData(Hashtable<String, String> datas) {
		setHashByHash(this.formDatas, datas);
	}

	void setJSPData(String dataName, Object data) {
		this.jspDatas.put(dataName, data);
	}

	public Object getJSPData(String dataName) {
		return this.jspDatas.get(dataName);
	}

	Hashtable<String, Object> getJSPDatas() {
		return this.jspDatas;
	}

	public void addErrorData(String errorItemID, String errorMsg) {
		Hashtable<String, String> hash = new Hashtable<String, String>();
		hash.put(FrameKeys.FOCUS_ITEM, errorItemID);
		hash.put(FrameKeys.ERROR_MESSAGE, errorMsg);
		this.errorDatas.add(hash);
	}

	public Vector<Hashtable<String, String>> getErrorDatas() {
		return this.errorDatas;
	}

	public void clearErrorDatas() {
		this.errorDatas.clear();
	}

	void setOutputHtml(String html) {
		this.outputHtml = html;
	}

	public String getOutputHtml() {
		return this.outputHtml;
	}

	public String getFrameHiddenHtml() {
		return this.frameHiddenHtml;
	}

	void setFrameHiddenHtml(String hiddenHtml) {
		this.frameHiddenHtml = hiddenHtml;
	}

	public static String convertToHtml(String s) {
		return StringUtil.convertXmlChars(s);
	}

	private static void setHashByHash(Hashtable<String, String> target, Hashtable<String, String> src) {
		Enumeration<String> e = src.keys();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			target.put(name, (String) src.get(name));
		}
	}
}