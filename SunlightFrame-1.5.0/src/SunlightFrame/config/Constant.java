/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.config;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 数据常量定义。
 * 
 */
public class Constant {
	private String name;

	private String relatedFieldName;

	Constant(String name, String relatedFieldName) {
		this.name = name;
		this.relatedFieldName = relatedFieldName;
	}

	/**
	 * 得到常量名。
	 * 
	 * @return 常量名。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 得到相关常量名。
	 * 
	 * @return 相关常量名。
	 */
	public String getRelatedFieldName() {
		return relatedFieldName;
	}

	static Constant fromXmlNode(Node node) {
		Element element = (Element) node;
		String name = element.getAttribute("name").trim();
		String relatedFieldName = element.getAttribute("relatedFieldName").trim();

		return new Constant(name, relatedFieldName);
	}
}
