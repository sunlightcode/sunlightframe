/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.config;

import java.io.File;

import SunlightFrame.database.DBProxy;
import SunlightFrame.database.EntityCache;
import SunlightFrame.web.ConstantCache;

/**
 * 解析配置文件，得到系统全部配置信息。 包括：系统参数信息、模块和操作定义，查询结果集定义，数据常量定义、系统用字符串提示信息。
 *
 */
public class AppConfig {
	private AppConstantConfig constantConfig = new AppConstantConfig();
	private AppParametersConfig parameterConfig = new AppParametersConfig();
	private AppMessagesConfig messageConfig = new AppMessagesConfig();
	private AppQueryDataListConfig queryDataListConfig = new AppQueryDataListConfig();
	private AppModuleConfig moduleConfig = new AppModuleConfig();
	private static AppConfig config = new AppConfig();
	private String applicationRoot;

	private AppConfig() {
	}

	/**
	 * 初始化，解析各种配置文件，得到所有的配置信息。
	 * 
	 * @param applicationRoot
	 *            系统运行的根目录。
	 * @throws Exception
	 */
	public void init(String applicationRoot) throws Exception {
		this.applicationRoot = applicationRoot;

		String parameterFilePath = applicationRoot + File.separator + "WEB-INF" + File.separator + "config"
				+ File.separator + "config.properties";
		String messageFilePath = applicationRoot + File.separator + "WEB-INF" + File.separator + "config"
				+ File.separator + "message.properties";

		String applicationXMLPath = applicationRoot + File.separator + "WEB-INF" + File.separator + "config"
				+ File.separator + "application.xml";

		parameterConfig.init(parameterFilePath);
		messageConfig.init(messageFilePath);

		moduleConfig.init(applicationXMLPath);
		queryDataListConfig.init(applicationXMLPath);
		constantConfig.init(applicationXMLPath);

		DBProxy.init();
	}

	public void reload() throws Exception {
		ConstantCache.getInstance().clear();
		EntityCache.getInstance().clear();
		init(applicationRoot);
	}

	/**
	 * 获得配置信息对象。
	 * 
	 * @return 配置信息对象。
	 */
	public static AppConfig getInstance() {
		return config;
	}

	/**
	 * 得到系统运行的根目录。
	 * 
	 * @return 系统运行的根目录。
	 */
	public String getApplicationRoot() {
		return applicationRoot;
	}

	/**
	 * 查询结果集定义。 对应于application.xml配置文件中的queryDataLists部分。
	 * 
	 * @return 查询结果集定义。
	 */
	public AppQueryDataListConfig getQueryDataListConfig() {
		return queryDataListConfig;
	}

	/**
	 * 数据常量定义。 对应于application.xml配置文件中的constants部分。
	 * 
	 * @return 数据常量定义。
	 */
	public AppConstantConfig getConstantConfig() {
		return constantConfig;
	}

	/**
	 * 模块和操作定义。 对应于application.xml配置文件中的modules部分。
	 * 
	 * @return 模块和操作定义。
	 */
	public AppModuleConfig getModuleConfig() {
		return moduleConfig;
	}

	/**
	 * 系统参数信息。 对应于config.properties配置文件。
	 * 
	 * @return 系统参数信息。
	 */
	public AppParametersConfig getParameterConfig() {
		return parameterConfig;
	}

	/**
	 * 用户提示信息。 对应于message.properties配置文件。
	 * 
	 * @return 系统用字符串提示信息。
	 */
	public AppMessagesConfig getMessagesConfig() {
		return messageConfig;
	}

}
