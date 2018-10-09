/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import SunlightFrame.exception.FrameException;

/**
 * 用户提示信息。 对应于message.properties配置文件。
 */
public class AppMessagesConfig {
	private String messageFilePath;
	private Hashtable<String, String> props = new Hashtable<String, String>();

	AppMessagesConfig() {

	}

	void init(String messageFilePath) throws Exception {
		this.messageFilePath = messageFilePath;
		load();
	}

	private void load() throws Exception {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(messageFilePath), "UTF-8"));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (!line.trim().equals("") && !line.substring(0, 1).equals("#")) {
				int delimIndex = line.indexOf("=");
				props.put(line.substring(0, delimIndex).trim(), line.substring(delimIndex + 1).trim());
			}
		}
		reader.close();
	}

	private String getMessage(String messageName) {
		String value = props.get(messageName);
		if (value != null) {
			value.trim();
		}
		return value;
	}

	/**
	 * 生成用户提示信息。
	 * 
	 * @param messageName
	 *            信息名。
	 * @param details
	 *            信息参数值。
	 * @return 生成的提示信息。
	 * @throws FrameException
	 */
	public static String makeMessage(String messageName, String[] details) throws FrameException {
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			try {
				AppConfig.getInstance().getMessagesConfig().load();
			} catch (Exception e) {
				throw new FrameException(e);
			}
		}
		String message = AppConfig.getInstance().getMessagesConfig().getMessage(messageName);
		if (message == null) {
			throw new FrameException("no message:" + messageName + " found");
		}
		for (int i = 0; i < details.length; i++) {
			message = message.replace("?", details[i]);
		}
		return message;
	}
}
