package SunlightFrame.config;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import SunlightFrame.util.StringUtil;

public class QueryDataList {
	private String name;
	private String columns;
	private Vector<QueryCondition> conditions = new Vector<QueryCondition>();
	private int pageNumber;
	private String conditionRelation;
	private boolean isQueryWithNoCondition = true;

	QueryDataList(String name, String columns, String conditionRelation, int pageNumber,
			Vector<QueryCondition> conditions, boolean isQueryWithNoCondition) {
		this.name = name;
		this.columns = columns;
		this.conditions = conditions;
		this.pageNumber = pageNumber;
		this.conditionRelation = conditionRelation;
		this.isQueryWithNoCondition = isQueryWithNoCondition;
	}

	public String getName() {
		return this.name;
	}

	public Vector<QueryCondition> getListConditions() {
		return this.conditions;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public boolean isQueryWithNoCondition() {
		return this.isQueryWithNoCondition;
	}

	public String getPreparedSql(Hashtable<?, ?> keys, Vector<String> values) {
		values.clear();
		String sql = getPreparedSql(keys, values, this.columns);
		if ((values.size() == 0) && (!(this.isQueryWithNoCondition)))
			sql = "";

		return sql;
	}

	public String getPreparedCountSql(Hashtable<?, ?> keys, Vector<String> values) {
		values.clear();
		String sql = getPreparedSql(keys, values, "count(*) as COUNT");
		if ((values.size() == 0) && (!(this.isQueryWithNoCondition)))
			sql = "";

		return sql;
	}

	public String getPreparedSql(Hashtable<?, ?> keys, Vector<String> values, String fields) {
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select " + fields + " from " + this.name + " ");

		for (int i = 0; i < this.conditions.size(); ++i) {
			QueryCondition condition = (QueryCondition) this.conditions.get(i);
			String expression = condition.getExpression();
			String expressionValue = getExpressionValue(expression, keys).toString();
			if ((!(expressionValue.equals(""))) && (!(expressionValue.equals("%%")))) {
				sqlBuf.append(this.conditionRelation + " " + condition.getName() + " " + condition.getOperator()
						+ ((condition.getOperator().equals("in")) ? " (?,?,?,?,?,?,?,?,?,?) " : " ? "));
				if (condition.getOperator().equals("in")) {
					values.addAll(splitInSqlValue(expressionValue));
				} else
					values.add(expressionValue);
			}

		}

		String sql = sqlBuf.toString();
		sql = sql.replaceFirst(" " + this.conditionRelation + " ", " where ");
		return sql;
	}

	private Vector<String> splitInSqlValue(String value) {
		Vector<String> values = new Vector<String>();
		String[] ss = StringUtil.split(value, " ,");
		for (int i = 0; i < 10; ++i)
			if (i < ss.length) {
				values.add(ss[i]);
			} else
				values.add("NON");

		return values;
	}

	static QueryDataList fromXmlNode(Node node) {
		Vector<QueryCondition> conditions = new Vector<QueryCondition>();
		Element element = (Element) node;
		String name = element.getAttribute("name").trim();
		String columns = ((element.getAttribute("columns") == null) || (element.getAttribute("columns").equals("")))
				? "*" : element.getAttribute("columns").trim();
		String conditionRelation = element.getAttribute("conditionRelation").trim();
		int pageNumber = Integer.parseInt(element.getAttribute("pageNumber"));
		boolean isQueryWithNoCondition = true;
		if (element.getAttribute("isQueryWithNoCondition") != null) {
			isQueryWithNoCondition = Boolean.parseBoolean(element.getAttribute("isQueryWithNoCondition"));
		}

		NodeList nodes = element.getElementsByTagName("condition");
		for (int i = 0; i < nodes.getLength(); ++i) {
			QueryCondition condition = QueryCondition.fromXmlNode(nodes.item(i));
			conditions.add(condition);
		}
		return new QueryDataList(name, columns, conditionRelation, pageNumber, conditions, isQueryWithNoCondition);
	}

	public String getConditionRelation() {
		return this.conditionRelation;
	}

	private static String getExpressionValue(String expression, Hashtable<?, ?> varHash) {
		Enumeration<?> e = varHash.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = (String) varHash.get(key);
			expression = expression.replaceAll("\\$" + key + "\\$", value);
		}
		if (expression.indexOf("$") >= 0)
			return "";

		return expression;
	}
}