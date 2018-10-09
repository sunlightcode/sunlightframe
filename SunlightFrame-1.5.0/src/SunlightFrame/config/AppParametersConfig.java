/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.config;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * 系统参数信息。 对应于config.properties配置文件。
 * 
 */
public class AppParametersConfig {
	private boolean isDebugModelRunning = false;
	private Properties prop = new Properties();

	AppParametersConfig() {

	}

	void init(String parameterFilePath) throws Exception {
		prop.load(new FileInputStream(parameterFilePath));

		// setDebugModel
		String isDebugModel = getParameter("isDebugModel");
		if (isDebugModel != null && !isDebugModel.equals("")) {
			isDebugModelRunning = Boolean.valueOf(isDebugModel).booleanValue();
		}
	}

	/**
	 * 检测当前系统是否以调试模式运行。
	 * 
	 * @return 如果以调试模式运行则返回"true"，否则返回"false"。
	 */
	public boolean isDebugModel() {
		return isDebugModelRunning;
	}

	/**
	 * 过得系统参数。
	 * 
	 * @param parameterName
	 *            参数名。
	 * @return 参数值。
	 */
	public String getParameter(String parameterName) {
		String value = prop.getProperty(parameterName);
		if (value != null) {
			value.trim();
		}
		return value;
	}

	public String toString() {
		StringBuffer sbf = new StringBuffer();
		Iterator<Object> iter = prop.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			String value = getParameter(key);
			sbf.append(key + ":\t" + value + "\r\n");
		}

		return sbf.toString();
	}
}
