/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.config;

import java.io.File;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import SunlightFrame.log.AppLogger;
import SunlightFrame.util.XMLUtil;

/**
 * 数据常量定义。 对应于application.xml配置文件中的constants部分。
 */
public class AppConstantConfig {
	private TreeMap<String, Constant> constantMap = new TreeMap<String, Constant>();
	private String configFilePath;

	AppConstantConfig() {

	}

	void init(String constantFilePath) throws Exception {
		configFilePath = constantFilePath;
		initConstantHash();
	}

	private void initConstantHash() throws Exception {
		constantMap.clear();
		Document doc = XMLUtil.createDocument(new File(configFilePath));
		NodeList nodes = doc.getElementsByTagName("constant");
		for (int i = 0; i < nodes.getLength(); i++) {
			Constant constant = getConstant(nodes.item(i));
			constantMap.put(constant.getName(), constant);
		}
	}

	/**
	 * 得到一个数据常量。
	 * 
	 * @param constantName
	 *            常量名。
	 * @return 一个数据常量。
	 */
	public Constant getConstant(String constantName) {
		reInit();
		return (Constant) constantMap.get(constantName);
	}

	private void reInit() {
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			try {
				initConstantHash();
			} catch (Exception e) {
				AppLogger.getInstance().debugLog("reInit error when config constant!", e);
			}
		}
	}

	private Constant getConstant(Node node) throws Exception {
		return Constant.fromXmlNode(node);
	}
}
