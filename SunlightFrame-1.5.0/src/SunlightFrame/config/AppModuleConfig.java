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
import org.w3c.dom.NodeList;

import SunlightFrame.log.AppLogger;
import SunlightFrame.util.XMLUtil;

/**
 * 模块和操作定义。 对应于application.xml配置文件中的modules部分。
 * 
 */
public class AppModuleConfig {
	private TreeMap<String, Module> moduleMap = new TreeMap<String, Module>();

	private String moduleFilePath;

	AppModuleConfig() {

	}

	void init(String moduleFilePath) throws Exception {
		this.moduleFilePath = moduleFilePath;
		initModuleHash();
	}

	private void initModuleHash() throws Exception {
		moduleMap.clear();
		Document doc = XMLUtil.createDocument(new File(moduleFilePath));
		NodeList nodes = doc.getElementsByTagName("module");
		for (int i = 0; i < nodes.getLength(); i++) {
			Module module = Module.fromXmlNode(nodes.item(i));
			moduleMap.put(module.getName(), module);
		}
	}

	/**
	 * 得到系统模块信息。
	 * 
	 * @param moduleName
	 *            模块名.
	 * @return 模块信息.
	 */
	public Module getModule(String moduleName) {
		reInit();
		return (Module) moduleMap.get(moduleName);
	}

	private void reInit() {
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			try {
				initModuleHash();
			} catch (Exception e) {
				AppLogger.getInstance().debugLog("reInit error when config module!", e);
			}
		}
	}
}
