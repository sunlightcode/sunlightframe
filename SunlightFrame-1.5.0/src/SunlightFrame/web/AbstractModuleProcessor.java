package SunlightFrame.web;

import java.io.File;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import SunlightFrame.config.AppConfig;
import SunlightFrame.config.AppMessagesConfig;
import SunlightFrame.config.Constant;
import SunlightFrame.config.Module;
import SunlightFrame.config.QueryDataList;
import SunlightFrame.database.DBConnectionPool;
import SunlightFrame.database.DBProxy;
import SunlightFrame.database.IndexGenerater;
import SunlightFrame.log.AppLogger;
import SunlightFrame.util.StringUtil;
import SunlightFrame.util.XMLUtil;

/**
 * 系统模块的处理器。一个模块对应于一个处理器。
 * 
 */
public abstract class AbstractModuleProcessor {
	public static int DATA_TYPE_TABLE = 1;
	public static int DATA_TYPE_VIEW = 2;
	private Connection con = null;
	private Module module;
	private DataHandle dataHandle;

	/**
	 * 构造方法，子类的构造方法必须调用super(module, dataHandle, con)
	 * 
	 * @param module
	 *            用户动作模块。
	 * @param dataHandle
	 *            用户提交数据句柄。
	 * @param con
	 *            数据库连接。
	 */
	public AbstractModuleProcessor(Module module, DataHandle dataHandle) {
		this.dataHandle = dataHandle;
		this.module = module;
	}

	final void doAction() throws Exception {
		Method method;
		String action = getFormData("action");
		boolean checkResult = false;

		Class<?> c = super.getClass();
		try {
			method = c.getMethod(action + "ActionCheck", new Class[0]);
			checkResult = ((Boolean) method.invoke(this, new Object[0])).booleanValue();
		} catch (NoSuchMethodException e) {
			checkResult = true;
		}

		if (checkResult) {
			startProcess();
			method = c.getMethod(action + "Action", new Class[0]);
			method.invoke(this, new Object[0]);
		}

		if (this.dataHandle.getOutputPage().equals("")) {
			makeView();
			setFrameHiddenHtml(makeHiddenHtml());
			this.dataHandle.setOutputPage(this.module.getOutput());
			endProcess();
		}
	}

	private String makeHiddenHtml() {
		StringBuffer htmlBuf = new StringBuffer();
		htmlBuf.append(
				"<input type=\"hidden\" name=\"module\" id=\"module\" value=\"" + this.module.getName() + "\"/>\n");
		htmlBuf.append(
				"<input type=\"hidden\" name=\"action\" id=\"action\" value=\"" + getFormData("action") + "\"/>\n");
		setControlData("IS_USER_LOGINED", Boolean.toString(isUserLogined()));
		htmlBuf.append("<input type=\"hidden\" name=\"isUserLogined\" id=\"isUserLogined\" value=\"" + isUserLogined()
				+ "\"/>\n");
		return htmlBuf.toString();
	}

	/**
	 * 在连接会话中设定登录的用户信息。
	 * 
	 * @param info
	 *            用户信息
	 */
	public void setLoginedUserInfo(Hashtable<String, String> info) {
		setSessionData("LOGIN_USER", info);
	}

	/**
	 * 从连接会话中获得登录的用户信息
	 * 
	 * @return 用户信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Hashtable<String, String> getLoginedUserInfo() {
		return ((Hashtable) getSessionData("LOGIN_USER"));
	}

	/**
	 * 用户退出时，删除连接会话中的用户信息
	 */
	public void removeLoginedUserInfo() {
		removeSessionData("LOGIN_USER");
	}

	/**
	 * 判定当前用户是否登录
	 * 
	 * @return "true" 用户已登录 "false" 用户未登录
	 */
	public boolean isUserLogined() {
		return (getLoginedUserInfo() != null);
	}

	/**
	 * 获得查询结果集，并将数据转化为xml格式。
	 * 
	 * @param listName
	 *            结果集名称，对应于配置文件中queryDataList的name属性。
	 * @param keys
	 *            条件值，用于根据queryDataList中定义的condition，动态的生成查询条件。
	 * @return xml数据，格式如下：&lt;dataList
	 *         name="listName"&gt;&lt;row&gt;...&lt;/row&gt;&lt;/dataList&gt;
	 * @throws Exception
	 */
	public final String makeQueryDataListXML(String listName, Hashtable<String, String> keys) throws Exception {
		Vector<Hashtable<String, String>> queryResults = getDataListDatas(listName, keys);
		return makeDataListXml(listName, queryResults);
	}

	/**
	 * 获得查询结果集。
	 * 
	 * @param listName
	 *            结果集名称，对应于配置文件中queryDataList的name属性。
	 * @param keys
	 *            条件值，用于根据queryDataList中定义的condition，动态的生成查询条件。
	 * @return 查询结果集。
	 * @throws Exception
	 */
	public final Vector<Hashtable<String, String>> getDataListDatas(String listName, Hashtable<String, String> keys)
			throws Exception {
		QueryDataList datalist = AppConfig.getInstance().getQueryDataListConfig().getQueryDataList(listName);
		int pageIndex = 1;
		String pageIndexString = getFormData("pageIndex");
		if (!(pageIndexString.equals("")))
			pageIndex = Integer.parseInt(pageIndexString);

		Vector<String> preparedValues = new Vector<String>();
		String preparedSql = datalist.getPreparedSql(keys, preparedValues);
		Vector<Hashtable<String, String>> queryResults = new Vector<Hashtable<String, String>>();
		if (!(preparedSql.equals("")))
			queryResults = DBProxy.query(getConnection(), datalist.getName(), preparedSql, preparedValues,
					pageIndex - 1, datalist.getPageNumber());

		return queryResults;
	}

	public final int getDataListCount(String listName, Hashtable<String, String> keys) throws Exception {
		QueryDataList datalist = AppConfig.getInstance().getQueryDataListConfig().getQueryDataList(listName);
		Vector<String> preparedValues = new Vector<String>();
		String preparedSql = datalist.getPreparedCountSql(keys, preparedValues);
		int count = 0;
		if (!(preparedSql.equals("")))
			count = Integer.parseInt(
					(String) ((Hashtable<?, ?>) DBProxy.query(getConnection(), preparedSql, preparedValues).get(0))
							.get("COUNT"));

		return count;
	}

	public Vector<Hashtable<String, String>> getDatalistQuery(String listName, Hashtable<String, String> keys,
			String fields, String resultView) throws Exception {
		QueryDataList datalist = AppConfig.getInstance().getQueryDataListConfig().getQueryDataList(listName);
		int pageIndex = 1;
		String pageIndexString = getFormData("pageIndex");
		if (!(pageIndexString.equals("")))
			pageIndex = Integer.parseInt(pageIndexString);

		Vector<String> preparedValues = new Vector<String>();
		String preparedSql = datalist.getPreparedSql(keys, preparedValues, fields);
		Vector<Hashtable<String, String>> queryResults = new Vector<Hashtable<String, String>>();
		if (!(preparedSql.equals("")))
			queryResults = DBProxy.query(getConnection(), resultView, preparedSql, preparedValues, pageIndex - 1,
					datalist.getPageNumber());

		return queryResults;
	}

	/**
	 * 将结果集数据转化为xml格式
	 * 
	 * @param listName
	 *            结果集名称
	 * @param v
	 *            结果集数据
	 * @return xml数据，格式如下：&lt;dataList
	 *         name="listName"&gt;&lt;row&gt;...&lt;/row&gt;&lt;/dataList&gt;
	 */
	public static final String makeDataListXml(String listName, Vector<Hashtable<String, String>> v) {
		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append("<dataList name=\"" + listName + "\">\n");
		Iterator<Hashtable<String, String>> iter = v.iterator();
		while (iter.hasNext()) {
			Hashtable<?, ?> hash = (Hashtable<?, ?>) iter.next();
			xmlBuf.append("<row>\n");
			Iterator<?> keyIter = hash.keySet().iterator();
			while (keyIter.hasNext()) {
				String key = convertXmlChars((String) keyIter.next());
				String value = convertXmlChars((String) hash.get(key));
				xmlBuf.append("<" + key + ">");
				xmlBuf.append(value);
				xmlBuf.append("</" + key + ">\n");
			}
			xmlBuf.append("</row>\n");
		}
		xmlBuf.append("</dataList>\n");
		return xmlBuf.toString();
	}

	/**
	 * 将一组数据值转化为xml格式数据。
	 * 
	 * @param dataName
	 *            数据名
	 * @param datas
	 *            数据值
	 * @return xml数据，格式如下：&lt;dataList
	 *         name="dataNames"&gt;&lt;row&gt;...&lt;/row&gt;&lt;/dataList&gt;
	 */
	protected final String makeDataListXml(String dataName, String[] datas) {
		Vector<Hashtable<String, String>> pageNameVector = new Vector<Hashtable<String, String>>();
		for (int i = 0; i < datas.length; ++i) {
			Hashtable<String, String> tmp = new Hashtable<String, String>();
			tmp.put(dataName, datas[i]);
			pageNameVector.add(tmp);
		}
		return makeDataListXml(dataName + "s", pageNameVector);
	}

	/**
	 * 得到由xml数据和xsl合成的用于输出的html内容。
	 * 
	 * @param dataListXMLs
	 *            一组xml数据，包括form数据，control数据，查询结果集数据。
	 * @param constantNames
	 *            合成时候要用到的常量数据名。
	 * @throws Exception
	 */
	public final void makeOutPutHtml(Vector<String> dataListXMLs, Vector<String> constantNames) throws Exception {
		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append("<document>\n");
		xmlBuf.append(makeXmlFromFormData());
		xmlBuf.append(makeXmlFromControlData());

		for (int i = 0; i < dataListXMLs.size(); ++i) {
			xmlBuf.append((String) dataListXMLs.get(i));
		}

		for (int i = 0; i < constantNames.size(); ++i) {
			xmlBuf.append(getConstantXML(getConnection(), (String) constantNames.get(i)));
		}

		xmlBuf.append("</document>");
		String xml = xmlBuf.toString();
		AppLogger.getInstance().debugLog("the output xml is:\n" + xml);

		String xsl = AppConfig.getInstance().getApplicationRoot() + File.separator + "xsl" + File.separator
				+ this.module.getName() + ".xsl";
		String html = "";
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			html = XMLUtil.combineXSL(xml, xsl);
		} else {
			html = XMLUtil.combineXSLWithCache(xml, xsl);
		}

		setOutputHtml(html);
	}

	private String makeXmlFromFormData() {
		Hashtable<?, ?> formData = getFormDatas();
		StringBuffer xmlBuf = new StringBuffer();
		Iterator<?> iter = formData.keySet().iterator();
		while (iter.hasNext()) {
			String key = convertXmlChars((String) iter.next());
			String value = convertXmlChars(((String) formData.get(key)).toString());
			xmlBuf.append("<" + key + ">");
			xmlBuf.append(value);
			xmlBuf.append("</" + key + ">\n");
		}
		return xmlBuf.toString();
	}

	private String makeXmlFromControlData() {
		Hashtable<?, ?> controlData = getControlDatas();
		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append("<controlData>\n");
		Iterator<?> iter = controlData.keySet().iterator();
		while (iter.hasNext()) {
			String key = convertXmlChars((String) iter.next());
			String value = convertXmlChars(((String) controlData.get(key)).toString());
			xmlBuf.append("<" + key + ">");
			xmlBuf.append(value);
			xmlBuf.append("</" + key + ">\n");
		}
		xmlBuf.append("</controlData>\n");
		return xmlBuf.toString();
	}

	/**
	 * 从数据库中得到常量数据转化为xml格式数据。
	 * 
	 * @param con
	 *            数据库连接。
	 * @param constantName
	 *            常量名，对应于配置文件中constant的name属性。
	 * @return xml数据，格式如下：&lt;constant
	 *         name="constantName"&gt;&lt;row&gt;...&lt;/row&gt;&lt;/constant&
	 *         gt;
	 * @throws SQLException
	 */
	public static final String getConstantXML(Connection con, String constantName) throws SQLException {
		Constant constant = AppConfig.getInstance().getConstantConfig().getConstant(constantName);

		Vector<?> v = getConstantValues(con, constantName);

		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append("<constant name=\"" + constant.getName() + "\">\n");
		for (int i = 0; i < v.size(); ++i) {
			Hashtable<?, ?> hash = (Hashtable<?, ?>) v.get(i);
			xmlBuf.append("<row>");

			xmlBuf.append("<id>"
					+ convertXmlChars((String) hash
							.get(new StringBuilder(String.valueOf(constant.getName())).append("ID").toString()))
					+ "</id>");
			xmlBuf.append("<name>"
					+ convertXmlChars((String) hash
							.get(new StringBuilder(String.valueOf(constant.getName())).append("Name").toString()))
					+ "</name>");
			if ((constant.getRelatedFieldName() != null) && (!(constant.getRelatedFieldName().equals(""))))
				xmlBuf.append("<relatedId>" + convertXmlChars((String) hash.get(constant.getRelatedFieldName()))
						+ "</relatedId>");

			xmlBuf.append("</row>\n");
		}
		xmlBuf.append("</constant>\n");
		return xmlBuf.toString();
	}

	/**
	 * 得到数据库中的常量数据。
	 * 
	 * @param con
	 *            数据库连接。
	 * @param constantName
	 *            常量名。
	 * @return 常量数据集合。
	 * @throws SQLException
	 */
	public static final Vector<Hashtable<String, String>> getConstantValues(Connection con, String constantName)
			throws SQLException {
		Vector<Hashtable<String, String>> v = ConstantCache.getInstance().getConstantValues(constantName);
		if (v == null) {
			v = DBProxy.query(con, constantName);
			ConstantCache.getInstance().setConstantValues(constantName, v);
		}

		return v;
	}

	/**
	 * 将页面定向到另一模块的另一个动作上，并防止用户刷新。
	 * 
	 * @param url
	 *            重定向到的url
	 * @param moduleName
	 *            重定向的模块名。
	 * @param actionName
	 *            重定向的操作名。
	 * @throws Exception
	 */
	public final void dispatchWithUnrefresh(String url, String moduleName, String actionName) throws Exception {
		setControlData(FrameKeys.JUMP_TO_URL, url);
		setControlData(FrameKeys.JUMP_TO_MODULE, moduleName);
		setControlData(FrameKeys.JUMP_TO_ACTION, actionName);
		dispatch("jumpPage.jsp");
	}

	/**
	 * 将处理后的输出页面重定向到另一个页面
	 * 
	 * @param fileName
	 *            待重定向的页面明朝。
	 */
	public final void dispatch(String fileName) {
		setFrameHiddenHtml(makeHiddenHtml());
		this.dataHandle.setOutputPage(fileName);
	}

	/**
	 * 将处理重定向到另一模块的另一个动作上。
	 * 
	 * @param moduleName
	 *            重定向的模块名。
	 * @param actionName
	 *            重定向的操作名。
	 * @throws Exception
	 */
	public final void dispatch(String moduleName, String actionName) throws Exception {
		setFormData("module", moduleName);
		setFormData("action", actionName);
		Module dispatchModule = AppConfig.getInstance().getModuleConfig().getModule(moduleName);
		AbstractModuleProcessor processor = ProcessorFactory.create(dispatchModule, this.dataHandle);
		processor.doAction();
	}

	/**
	 * message.properties配置文件中获得信息。
	 * 
	 * @param name
	 *            信息名。
	 * @return 信息内容。
	 */
	public String getMessage(String name) throws Exception {
		return AppMessagesConfig.makeMessage(name, null);
	}

	/**
	 * 得到一个数据库连接。
	 * 
	 * @return 一个数据库连接
	 */
	protected final Connection getConnection() throws Exception {
		if (this.con == null) {
			this.con = DBConnectionPool.getInstance().getConnection();
			this.con.setAutoCommit(false);
			this.dataHandle.getRequest().setAttribute("DATABASE_CONNECTION", this.con);
		}

		return this.con;
	}

	/**
	 * 得到会话数据。
	 * 
	 * @param name
	 *            数据名。
	 * @return 数据值。
	 */
	protected final Object getSessionData(String name) {
		return this.dataHandle.getSessionData(name);
	}

	/**
	 * 设置会话数据。
	 * 
	 * @param name
	 *            数据名。
	 * @param data
	 *            数据值。
	 */
	protected final void setSessionData(String name, Object data) {
		this.dataHandle.setSessionData(name, data);
	}

	/**
	 * 删除会话数据。
	 * 
	 * @param name
	 *            数据名。
	 */
	protected final void removeSessionData(String name) {
		this.dataHandle.removeSessionData(name);
	}

	/**
	 * 得到控制数据。
	 * 
	 * @param dataName
	 *            数据名。
	 * @return 数据值。
	 */
	protected final String getControlData(String dataName) {
		return this.dataHandle.getJSPDataBean().getControlData(dataName);
	}

	/**
	 * 得到全部控制数据。
	 * 
	 * @return 全部控制数据。
	 */
	protected final Hashtable<String, String> getControlDatas() {
		return this.dataHandle.getJSPDataBean().getControlDatas();
	}

	/**
	 * 设置控制数据。
	 * 
	 * @param dataName
	 *            数据名。
	 * @param data
	 *            数据值。
	 */
	protected final void setControlData(String dataName, String data) {
		this.dataHandle.getJSPDataBean().setControlData(dataName, data);
	}

	/**
	 * 设置一组控制数据。
	 * 
	 * @param datas
	 *            待设置的一组控制数据。
	 */
	protected final void setControlData(Hashtable<String, String> datas) {
		this.dataHandle.getJSPDataBean().setControlData(datas);
	}

	/**
	 * 得到用户提交的表单数据。
	 * 
	 * @param dataName
	 *            数据名。
	 * @return 数据值。
	 */
	protected final String getFormData(String dataName) {
		return this.dataHandle.getJSPDataBean().getFormData(dataName);
	}

	/**
	 * 得到用户提交的全部表单数据。
	 * 
	 * @return 全部表单数据。
	 */
	protected final Hashtable<String, String> getFormDatas() {
		return this.dataHandle.getJSPDataBean().getFormDatas();
	}

	/**
	 * 得到用户提交的全部文件。
	 * 
	 * @return 提交的全部文件。
	 */
	protected final Vector<File> getUploadFiles() {
		return this.dataHandle.getUploadFiles();
	}

	protected final void setFormData(String dataName, String data) {
		this.dataHandle.getJSPDataBean().setFormData(dataName, data);
	}

	protected final void setJSPData(String dataName, Object data) {
		this.dataHandle.getJSPDataBean().setJSPData(dataName, data);
	}

	protected final Object getJSPData(String dataName) {
		return this.dataHandle.getJSPDataBean().getJSPData(dataName);
	}

	protected final Hashtable<String, Object> getJSPDatas() {
		return this.dataHandle.getJSPDataBean().getJSPDatas();
	}

	protected final void setFormData(Hashtable<String, String> datas) {
		this.dataHandle.getJSPDataBean().setFormData(datas);
	}

	protected final void addErrorData(String errorItemID, String errorMsg) {
		this.dataHandle.getJSPDataBean().addErrorData(errorItemID, errorMsg);
	}

	protected final Vector<Hashtable<String, String>> getErrorDatas() {
		return this.dataHandle.getJSPDataBean().getErrorDatas();
	}

	protected final void clearErrorDatas() {
		this.dataHandle.getJSPDataBean().clearErrorDatas();
	}

	protected final void clearDatas(String[] dataNames) {
		for (int i = 0; i < dataNames.length; ++i)
			this.dataHandle.getJSPDataBean().setFormData(dataNames[i], "");
	}

	protected final void setOutputHtml(String html) {
		this.dataHandle.getJSPDataBean().setOutputHtml(html);
	}

	protected final String getOutputHtml() {
		return this.dataHandle.getJSPDataBean().getOutputHtml();
	}

	protected final void setErrorMessage(String errorMessage) {
		getControlDatas().put(FrameKeys.ERROR_MESSAGE, errorMessage);
	}

	protected final void setInfoMessage(String infoMessage) {
		getControlDatas().put(FrameKeys.INFO_MESSAGE, infoMessage);
	}

	protected final void setFocusItem(String itemName) {
		getControlDatas().put(FrameKeys.FOCUS_ITEM, itemName);
	}

	protected final void setDownloadFile(String fileName) {
		getControlDatas().put(FrameKeys.DOWNLOAD_FILE, fileName);
	}

	final void setFrameHiddenHtml(String hiddenHtml) {
		this.dataHandle.getJSPDataBean().setFrameHiddenHtml(hiddenHtml);
	}

	private static String convertXmlChars(String s) {
		return StringUtil.convertXmlChars(s);
	}

	protected final CheckList getChecklist() {
		return new CheckList(this.dataHandle.getJSPDataBean());
	}

	protected final String getRequestIPInfo() {
		return this.dataHandle.getRequest().getRemoteAddr();
	}

	public void endProcess() throws Exception {
	}

	public void startProcess() throws Exception {
	}

	public String getCookieData(String name) {
		Cookie[] cookies = this.dataHandle.getRequest().getCookies();
		if (cookies != null)
			for (int i = 0; i < cookies.length; ++i)
				if (cookies[i].getName().equals(name))
					return cookies[i].getValue();

		return "";
	}

	public void setCookieData(Cookie cookie) {
		this.dataHandle.setCookie(cookie);
	}

	public void suspendModuleAndAction(String module, String action) {
		setSessionData("SUSPEND_MODULE", module);
		setSessionData("SUSPEND_ACTION", action);
		setSessionData("SUSPEND_DATAS", getFormDatas());
	}

	@SuppressWarnings("unchecked")
	public void resumeModuleAndAction() throws Exception {
		String module = getSessionData("SUSPEND_MODULE").toString();
		String action = getSessionData("SUSPEND_ACTION").toString();
		Hashtable<String, String> formDatas = (Hashtable<String, String>) getSessionData("SUSPEND_DATAS");
		setFormData(formDatas);
		dispatch(module, action);
	}

	public HttpServletRequest getRequest() {
		return this.dataHandle.getRequest();
	}

	public HttpServletResponse getResponse() {
		return this.dataHandle.getResponse();
	}

	public int getSumValue(String tableName, String fieldName, String keyID, String keyValue) throws Exception {
		String preparedSql = "select sum(" + fieldName + ") as SUM from " + tableName + " where " + keyID + " = '"
				+ keyValue + "'";
		String sum = (String) ((Hashtable<?, ?>) DBProxy.query(getConnection(), preparedSql, new Vector<String>())
				.get(0)).get("SUM");
		if (sum.equals(""))
			return 0;

		return Integer.parseInt(sum);
	}

	public int getCountValue(String tableName, String fieldName, String keyID, String keyValue) throws Exception {
		String preparedSql = "select count(" + fieldName + ") as COUNT from " + tableName + " where " + keyID + " = '"
				+ keyValue + "'";
		String sum = (String) ((Hashtable<?, ?>) DBProxy.query(getConnection(), preparedSql, new Vector<String>())
				.get(0)).get("COUNT");
		if (sum.equals(""))
			return 0;

		return Integer.parseInt(sum);
	}

	public void setJumpPageInfo(int count, int pageNumber) {
		int pageCount = count / pageNumber + ((count % pageNumber > 0) ? 1 : 0);
		int pageIndex = Integer.parseInt(getFormData("pageIndex"));
		int pageFrom = 1;
		int pageTo = 5;
		if (pageCount > 5) {
			if (pageCount - pageIndex <= 2) {
				pageFrom = pageCount - 4;
			} else if (pageIndex <= 2) {
				pageFrom = 1;
			} else
				pageFrom = pageIndex - 2;

			pageTo = pageFrom + 4;
		} else {
			pageTo = pageCount;
		}
		setFormData("count", Integer.toString(count));
		setFormData("pageCount", Integer.toString(pageCount));
		setFormData("pageNumber", Integer.toString(pageNumber));
		setFormData("pageFrom", Integer.toString(pageFrom));
		setFormData("pageTo", Integer.toString(pageTo));
	}

	public int getPageCount(String dataList, Hashtable<String, String> key) throws Exception {
		int count = getDataListCount(dataList, key);
		int pageNumber = getPageNumber(dataList);
		int pageCount = count / pageNumber + ((count % pageNumber > 0) ? 1 : 0);
		return pageCount;
	}

	public int getLastPageIndex(int count, int pageNumber) {
		int pageIndex = 1;
		if (count % pageNumber == 0) {
			pageIndex = count / pageNumber;
		} else
			pageIndex = count / pageNumber + 1;

		if (pageIndex == 0)
			pageIndex = 1;

		return pageIndex;
	}

	public int getPageNumber(String dataListName) {
		return AppConfig.getInstance().getQueryDataListConfig().getQueryDataList(dataListName).getPageNumber();
	}

	public String makeSelectElementString(String selectID, Vector<Hashtable<String, String>> datas, String valueField,
			String textField, String event) throws Exception {
		StringBuffer buf = new StringBuffer();
		buf.append("<select id='" + selectID + "' name='" + selectID + "'"
				+ ((event.equals("")) ? "" : new StringBuilder(" onchange=javascript:").append(event).toString())
				+ ">");
		buf.append("<option value=''></option>");
		for (int i = 0; i < datas.size(); ++i) {
			Hashtable<?, ?> data = (Hashtable<?, ?>) datas.get(i);
			buf.append("<option value='" + ((String) data.get(valueField)) + "'"
					+ ((((String) data.get(valueField)).equals(getFormData(selectID))) ? " selected" : "") + ">"
					+ ((String) data.get(textField)) + "</option>");
		}
		buf.append("</select>");
		return buf.toString();
	}

	public String makeRadioElementString(String radioID, Vector<Hashtable<String, String>> datas, String valueField,
			String textField, String event) throws Exception {
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < datas.size(); ++i) {
			Hashtable<?, ?> data = (Hashtable<?, ?>) datas.get(i);
			boolean isChecked = false;
			if ((getFormData(radioID).equals("")) && (i == 0))
				isChecked = true;
			else if (((String) data.get(valueField)).equals(getFormData(radioID)))
				isChecked = true;

			buf.append("<input type='radio' name='" + radioID + "' id='" + radioID + "' value='"
					+ ((String) data.get(valueField)) + "'" + ((isChecked) ? " checked" : "")
					+ ((event.equals("")) ? "" : new StringBuilder(" onclick=javascript:").append(event).toString())
					+ " />" + ((String) data.get(textField)) + "&nbsp;");
		}
		return buf.toString();
	}

	public abstract void makeView() throws Exception;

	public void initPageIndex() throws Exception {
		int pageIndex;
		try {
			pageIndex = Integer.parseInt(getFormData("pageIndex"));
			if (pageIndex <= 0)
				pageIndex = 1;

			setFormData("pageIndex", Integer.toString(pageIndex));
		} catch (Exception e) {
			setFormData("pageIndex", "1");
		}
	}

	public void changeValidFlag(String tableName, String validFlag) throws Exception {
		Hashtable<String, String> key = new Hashtable<String, String>();
		key.put(tableName + "ID", getFormData(tableName + "ID"));

		Hashtable<String, String> value = new Hashtable<String, String>();
		value.put("validFlag", validFlag);

		DBProxy.update(getConnection(), tableName, key, value);
	}

	public void getData(String tableName, int dataType) throws Exception {
		String id = tableName + "ID";
		Hashtable<String, String> key = new Hashtable<String, String>();
		key.put(id, getFormData(id));

		if (dataType == DATA_TYPE_TABLE)
			setFormData((Hashtable<String, String>) DBProxy.query(getConnection(), tableName, key).get(0));
		else if (dataType == DATA_TYPE_VIEW)
			setFormData((Hashtable<String, String>) DBProxy.query(getConnection(), tableName + "_V", key).get(0));
	}

	public void deleteData(String tableName) throws Exception {
		String id = tableName + "ID";
		Hashtable<String, String> key = new Hashtable<String, String>();
		key.put(id, getFormData(id));

		DBProxy.delete(getConnection(), tableName, key);
	}

	public boolean normalImageCheck() throws Exception {
		if (getUploadFiles().size() == 0) {
			setErrorMessage("请上传一张正确的图片");
			return false;
		}
		if (!(isImageFile((File) getUploadFiles().get(0)))) {
			((File) getUploadFiles().get(0)).delete();
			setErrorMessage("请上传一张正确的图片");
			return false;
		}
		return true;
	}

	public boolean isImageFile(File f) {
		String fileName = f.getName().toLowerCase();
		if ((!(fileName.endsWith("jpeg"))) && (!(fileName.endsWith("gif"))) && (!(fileName.endsWith("png")))
				&& (!(fileName.endsWith("logo"))) && (!(fileName.endsWith("jpg")))) {
			f.delete();
			return false;
		}
		return true;
	}

	public void confirmValue(String tableName) throws Exception {
		String id = tableName + "ID";

		if (getFormData(id).equals("")) {
			setFormData(id, IndexGenerater.getTableIndex(tableName, getConnection()));
			setFormData("validFlag", "1");

			DBProxy.insert(getConnection(), tableName, getFormDatas());
		} else {
			Hashtable<String, String> key = new Hashtable<String, String>();
			key.put(id, getFormData(id));

			DBProxy.update(getConnection(), tableName, key, getFormDatas());
		}
	}
}