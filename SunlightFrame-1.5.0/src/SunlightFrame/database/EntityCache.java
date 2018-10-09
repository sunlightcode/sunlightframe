/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import SunlightFrame.config.AppConfig;

/**
 * 数据表元数据的缓存。
 * 
 */
public class EntityCache {
	private static EntityCache cache = new EntityCache();

	private Hashtable<String, EntityInfo> entityHash = new Hashtable<String, EntityInfo>();

	private EntityCache() {

	}

	public static EntityCache getInstance() {
		return cache;
	}

	public void clear() {
		entityHash.clear();
	}

	public EntityInfo getEntityInfo(Connection con, String tableName) throws SQLException {
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			return loadEntityInfo(con, tableName);
		}

		EntityInfo info = null;

		info = (EntityInfo) entityHash.get(tableName);
		if (info == null) {
			info = loadEntityInfo(con, tableName);
			entityHash.put(tableName, info);
		}
		return info;
	}

	private static EntityInfo loadEntityInfo(Connection con, String tableName) throws SQLException {
		// String dbType = AppConfig.getInstance().getParameterConfig()
		// .getParameter("database.type");
		// if ("mysql".equalsIgnoreCase(dbType)) {
		// return getMysqlEntityInfo(con, tableName);
		// } else if ("oracle".equalsIgnoreCase(dbType)) {
		// return getOracleEntityInfo(con, tableName);
		// } else if ("sqlServer".equalsIgnoreCase(dbType)) {
		// return getSqlServerEntityInfo(con, tableName);
		// } else {
		// throw new SQLException("unsurported database : " + dbType);
		// }

		String preparedSql = "select * from " + tableName + " where 1 != 1";

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = con.prepareStatement(preparedSql);
			resultSet = statement.executeQuery();
			FieldInfo[] fields = getQueryResultColumns(resultSet);
			return new EntityInfo(tableName, fields);
		} catch (SQLException e) {
			throw e;
		} finally {
			if (resultSet != null) {
				try {
					resultSet.close();
				} catch (Exception e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static FieldInfo[] getQueryResultColumns(ResultSet rs) throws SQLException {
		ResultSetMetaData rsm = rs.getMetaData();
		Vector<FieldInfo> v = new Vector<FieldInfo>();
		int columnCount = rsm.getColumnCount();
		for (int i = 0; i < columnCount; ++i) {
			String name = rsm.getColumnName(i + 1);
			String typeName = rsm.getColumnTypeName(i + 1);
			String length = "100";
			String isNullValid = "";
			v.add(new FieldInfo(name, typeName, length, isNullValid));
		}
		FieldInfo[] fields = v.toArray(new FieldInfo[v.size()]);

		return fields;
	}
}
