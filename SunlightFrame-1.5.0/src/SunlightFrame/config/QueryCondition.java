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
 * 查询条件定义。对应于配置文件中的condition。
 * 
 */
public class QueryCondition {
	private String name;
	private String expression;
	private String operator;

	QueryCondition(String name, String expression, String operator) {
		this.name = name;
		this.expression = expression;
		this.operator = operator;
	}

	static QueryCondition fromXmlNode(Node node) {
		Element element = (Element) node;
		String name = element.getAttribute("name").trim();
		String expression = element.getAttribute("expression").trim();
		String operator = element.getAttribute("operator").trim();
		return new QueryCondition(name, expression, operator);
	}

	/**
	 * 获得查询条件的表达式。
	 * 
	 * @return 查询条件的表达式。
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * 获得查询比较符号。
	 * 
	 * @return 查询比较符号。
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 获得查询条件的字段名。
	 * 
	 * @return 查询条件的字段名。
	 */
	public String getName() {
		return name;
	}

}
