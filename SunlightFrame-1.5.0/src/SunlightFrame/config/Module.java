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
 * 系统模块定义，对应于配置文件中的定义的一个module。
 */
public class Module {
	private String name;
	private String output;

	Module(String name, String output) {
		this.name = name;
		this.output = output;
	}

	/**
	 * 得到模块名。
	 * 
	 * @return 模块名。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 得到模块的输出页面。
	 * 
	 * @return 模块的输出页面。
	 */
	public String getOutput() {
		return output;
	}

	static Module fromXmlNode(Node node) {
		Element element = (Element) node;
		String name = element.getAttribute("name").trim();
		String output = element.getAttribute("output").trim();

		return new Module(name, output);
	}
}
