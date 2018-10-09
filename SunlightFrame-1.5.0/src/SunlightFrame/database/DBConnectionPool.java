/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.database;

/**
 * 数据库连接池，用于对数据库连接进行管理。
 * 
 */
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp.BasicDataSource;

import SunlightFrame.config.AppConfig;
import SunlightFrame.config.AppParametersConfig;

public class DBConnectionPool {
	private static DBConnectionPool pool = new DBConnectionPool();

	private BasicDataSource datasource;

	private DBConnectionPool() {
		AppParametersConfig parameters = AppConfig.getInstance().getParameterConfig();
		String driverName = parameters.getParameter("database.driverName");
		String url = parameters.getParameter("database.url");
		String userName = parameters.getParameter("database.userName");
		String password = parameters.getParameter("database.password");
		String initConnString = parameters.getParameter("database.initConn");
		String maxActiveString = parameters.getParameter("database.maxActive");
		String maxIdleString = parameters.getParameter("database.maxIdle");
		String maxWaitString = parameters.getParameter("database.maxWait");

		String validationQueryTimeout = parameters.getParameter("database.validationQueryTimeout") == null ? ""
				: parameters.getParameter("database.validationQueryTimeout");
		String timeBetweenEvictionRunsMillisStr = parameters
				.getParameter("database.timeBetweenEvictionRunsMillis") == null ? ""
						: parameters.getParameter("database.timeBetweenEvictionRunsMillis");
		String minEvictableIdleTimeMillisStr = parameters.getParameter("database.minEvictableIdleTimeMillis") == null
				? "" : parameters.getParameter("database.minEvictableIdleTimeMillis");
		String numTestsPerEvictionRunStr = parameters.getParameter("database.numTestsPerEvictionRun") == null ? ""
				: parameters.getParameter("database.numTestsPerEvictionRun");
		String removeAbandonedStr = parameters.getParameter("database.removeAbandoned") == null ? ""
				: parameters.getParameter("database.removeAbandoned");
		String removeAbandonedTimeoutStr = parameters.getParameter("database.removeAbandonedTimeout") == null ? ""
				: parameters.getParameter("database.removeAbandonedTimeout");

		datasource = getDataSource(driverName, url, userName, password, initConnString, maxActiveString, maxIdleString,
				maxWaitString, validationQueryTimeout, timeBetweenEvictionRunsMillisStr, minEvictableIdleTimeMillisStr,
				numTestsPerEvictionRunStr, removeAbandonedStr, removeAbandonedTimeoutStr);
	}

	private BasicDataSource getDataSource(String driverName, String url, String userName, String password,
			String initConnString, String maxActiveString, String maxIdleString, String maxWaitString,
			String validationQueryTimeoutStr, String timeBetweenEvictionRunsMillisStr,
			String minEvictableIdleTimeMillisStr, String numTestsPerEvictionRunStr, String removeAbandonedStr,
			String removeAbandonedTimeoutStr) {

		int maxActive = 20;
		int maxIdle = 1;
		int maxWait = 120;

		if (!maxActiveString.equals("")) {
			maxActive = Integer.parseInt(maxActiveString);
		}
		if (!maxIdleString.equals("")) {
			maxIdle = Integer.parseInt(maxIdleString);
		}
		if (!maxWaitString.equals("")) {
			maxWait = Integer.parseInt(maxWaitString);
		}

		// if (!validationQueryTimeoutStr.equals("")) {
		// validationQueryTimeout = Integer.parseInt(validationQueryTimeoutStr);
		// }
		//
		// if (!timeBetweenEvictionRunsMillisStr.equals("")) {
		// timeBetweenEvictionRunsMillis =
		// Integer.parseInt(timeBetweenEvictionRunsMillisStr);
		// }
		// if (!minEvictableIdleTimeMillisStr.equals("")) {
		// minEvictableIdleTimeMillis =
		// Integer.parseInt(minEvictableIdleTimeMillisStr);
		// }
		// if (!numTestsPerEvictionRunStr.equals("")) {
		// numTestsPerEvictionRun = Integer.parseInt(numTestsPerEvictionRunStr);
		// }
		// if (!removeAbandonedStr.equals("")) {
		// removeAbandoned = Boolean.valueOf(removeAbandonedStr);
		// }
		// if (!removeAbandonedTimeoutStr.equals("")) {
		// removeAbandonedTimeout = Integer.parseInt(removeAbandonedTimeoutStr);
		// }

		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(driverName);
		source.setUrl(url);
		source.setUsername(userName);
		source.setPassword(password);
		source.setMaxActive(maxActive);
		source.setMaxIdle(maxIdle);
		source.setMaxWait(maxWait);

		// source.setInitialSize(initConn);
		// source.setMaxActive(maxActive);
		// source.setMaxIdle(maxIdle);
		// source.setMaxWait(maxWait);
		// source.setValidationQuery("SELECT 1");
		// source.setTestOnBorrow(false);
		// source.setTestWhileIdle(true);
		// // 查询验证的ＳＱＬ的执行超时时间
		// source.setValidationQueryTimeout(validationQueryTimeout);
		//
		// source.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		//
		// // 超过多少时间闲置不用的ＩＤＬＥ连接被关闭
		// source.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		//
		// // 一次检查的闲置连接个数
		// source.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
		//
		// // 检查被占用的连接是否没有被正常关闭
		// source.setRemoveAbandoned(removeAbandoned);
		//
		// source.setRemoveAbandonedTimeout(removeAbandonedTimeout);
		// // 打印非法链接的信息
		// source.setLogAbandoned(true);

		return source;
	}

	/**
	 * 获得唯一的数据库连接池实例。
	 * 
	 * @return 数据库连接池实例。
	 */
	public static DBConnectionPool getInstance() {
		return pool;
	}

	/**
	 * 从数据库连接池得到一个数据库连接。
	 * 
	 * @return 一个数据库连接。
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return datasource.getConnection();
	}

	/**
	 * 释放一个数据库连接。
	 * 
	 * @param connection
	 *            要释放的数据库连接。
	 * @throws SQLException
	 */
	public void freeConnection(Connection connection) throws SQLException {
		connection.close();
	}

	/**
	 * 获得数据源连接状态
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String getStatus() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("number_active:" + datasource.getNumActive() + "\t" + "number_idle:" + datasource.getNumIdle());
		return sbf.toString();
	}
}
