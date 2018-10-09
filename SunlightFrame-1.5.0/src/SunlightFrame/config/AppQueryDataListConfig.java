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
 * 查询结果集定义。 对应于application.xml配置文件中的queryDataLists部分。
 * 
 */
public class AppQueryDataListConfig {
	private TreeMap<String, QueryDataList> listMap = new TreeMap<String, QueryDataList>();

	private String configFilePath;

	AppQueryDataListConfig() {

	}

	void init(String constantFilePath) throws Exception {
		configFilePath = constantFilePath;
		initListHash();
	}

	private void initListHash() throws Exception {
		listMap.clear();
		Document doc = XMLUtil.createDocument(new File(configFilePath));
		NodeList nodes = doc.getElementsByTagName("queryDataList");
		for (int i = 0; i < nodes.getLength(); i++) {
			QueryDataList list = getQueryDataList(nodes.item(i));
			listMap.put(list.getName(), list);
		}
	}

	/**
	 * 得到一个查询结果集定义。
	 * 
	 * @param listName
	 *            查询结果集名称。
	 * @return 查询结果集定义。
	 */
	public QueryDataList getQueryDataList(String listName) {
		reInit();
		return (QueryDataList) listMap.get(listName);
	}

	private void reInit() {
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			try {
				initListHash();
			} catch (Exception e) {
				AppLogger.getInstance().debugLog("reInit error when config queryDataList!", e);
			}
		}
	}

	private QueryDataList getQueryDataList(Node node) throws Exception {
		return QueryDataList.fromXmlNode(node);
	}
}
