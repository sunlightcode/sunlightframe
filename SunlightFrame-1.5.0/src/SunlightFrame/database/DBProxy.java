package SunlightFrame.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import SunlightFrame.config.AppConfig;
import SunlightFrame.exception.FrameException;

/**
 * 数据库访问代理。封装了特定数据库的访问差异。
 */
public class DBProxy {
	private static AbstractDBAccessor dbAccessor;

	/**
	 * 初始化。根据配置文件的信息，装载特定的数据库访问器。
	 * 
	 * @throws FrameException
	 */
	public static void init() throws FrameException {
		String dbType = AppConfig.getInstance().getParameterConfig().getParameter("database.type");
		if ("mysql".equalsIgnoreCase(dbType)) {
			dbAccessor = new MySqlDBAccessor();
		} else if ("oracle".equalsIgnoreCase(dbType)) {
			dbAccessor = new OracleDBAccessor();
		} else if ("sqlServer".equalsIgnoreCase(dbType)) {
			dbAccessor = new SqlServerDBAccessor();
		} else
			throw new FrameException("unsurported database : " + dbType);
	}

	public static Vector<Hashtable<String, String>> query(Connection con, String tableName) throws SQLException {
		Hashtable<String, String> keys = null;
		String extendSql = null;
		int pageIndex = 0;
		int pageNumber = 0;
		return query(con, tableName, keys, extendSql, pageIndex, pageNumber);
	}

	public static Vector<Hashtable<String, String>> query(Connection con, String tableName,
			Hashtable<String, String> keys) throws SQLException {
		String extendSql = null;
		int pageIndex = 0;
		int pageNumber = 0;
		return query(con, tableName, keys, extendSql, pageIndex, pageNumber);
	}

	public static Vector<Hashtable<String, String>> query(Connection con, String tableName, int pageIndex,
			int pageNumber) throws SQLException {
		Hashtable<String, String> keys = null;
		String extendSql = null;
		return query(con, tableName, keys, extendSql, pageIndex, pageNumber);
	}

	public static Vector<Hashtable<String, String>> query(Connection con, String tableName, String columns,
			Hashtable<String, String> keys, int pageIndex, int pageNumber) throws SQLException {
		String extendSql = null;
		return query(con, tableName, keys, extendSql, pageIndex, pageNumber);
	}

	public static Vector<Hashtable<String, String>> query(Connection con, String tableName,
			Hashtable<String, String> keys, String extendSql) throws SQLException {
		int pageIndex = 0;
		int pageNumber = 0;
		return query(con, tableName, keys, extendSql, pageIndex, pageNumber);
	}

	public static Vector<Hashtable<String, String>> query(Connection con, String tableName,
			Hashtable<String, String> keys, String extendSql, int pageIndex, int pageNumber) throws SQLException {
		return dbAccessor.query(con, tableName, keys, extendSql, pageIndex, pageNumber);
	}

	public static Vector<Hashtable<String, String>> query(Connection con, String preparedSql, Vector<String> keys)
			throws SQLException {
		return dbAccessor.query(con, preparedSql, keys);
	}

	public static Vector<Hashtable<String, String>> query(Connection con, String tableName, String preparedSql,
			Vector<String> values, int pageIndex, int pageNumber) throws SQLException {
		return dbAccessor.query(con, tableName, preparedSql, values, pageIndex, pageNumber);
	}

	public static int update(Connection con, String tableName, Hashtable<String, String> keys,
			Hashtable<String, String> values) throws SQLException {
		String extendSql = null;
		return update(con, tableName, keys, values, extendSql);
	}

	public static int update(Connection con, String tableName, Hashtable<String, String> keys,
			Hashtable<String, String> values, String extendSql) throws SQLException {
		return dbAccessor.update(con, tableName, keys, values, extendSql);
	}

	public static int update(Connection con, String tableName, String preparedSql, Vector<String> values)
			throws SQLException {
		return dbAccessor.update(con, tableName, preparedSql, values);
	}

	public static void insert(Connection con, String tableName, Hashtable<String, String> values) throws SQLException {
		dbAccessor.insert(con, tableName, values);
	}

	public static int delete(Connection con, String tableName, Hashtable<String, String> keys) throws SQLException {
		return delete(con, tableName, keys, null);
	}

	public static int delete(Connection con, String tableName, Hashtable<String, String> keys, String extendSql)
			throws SQLException {
		return dbAccessor.delete(con, tableName, keys, extendSql);
	}

	public static void createTable(Connection con, String tableName, Hashtable<?, ?> columnInfo, String[] primaryKeys)
			throws SQLException {
		dbAccessor.createTable(con, tableName, columnInfo, primaryKeys);
	}

	public static void dropTable(Connection con, String tableName) throws SQLException {
		dbAccessor.dropTable(con, tableName);
	}
}