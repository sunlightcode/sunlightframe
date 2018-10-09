package SunlightFrame.database;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.management.RuntimeErrorException;

import SunlightFrame.log.AppLogger;
import SunlightFrame.util.StringUtil;

public abstract class AbstractDBAccessor {
	abstract Vector<Hashtable<String, String>> query(Connection paramConnection, String paramString1,
			Hashtable<String, String> paramHashtable, String paramString2, int paramInt1, int paramInt2)
			throws SQLException;

	abstract Vector<Hashtable<String, String>> query(Connection paramConnection, String paramString1,
			String paramString2, Vector<String> paramVector, int paramInt1, int paramInt2) throws SQLException;

	Vector<Hashtable<String, String>> query(Connection con, String preparedSql, Vector<String> keyValues)
			throws SQLException {
		AppLogger.getInstance().debugLog("execute query:" + preparedSql);
		PreparedStatement statement = con.prepareStatement(preparedSql);

		for (int i = 0; i < keyValues.size(); ++i) {
			statement.setString(i + 1, (String) keyValues.get(i));
		}

		Vector<Hashtable<String, String>> results = new Vector<Hashtable<String, String>>();
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery();
			FieldInfo[] fields = getQueryResultColumns(resultSet);
			while (resultSet.next()) {
				Hashtable<String, String> rowData = new Hashtable<String, String>();
				for (int i = 0; i < fields.length; ++i) {
					Object value = resultSet.getObject(fields[i].getName());
					if (fields[i].getTypeName().equalsIgnoreCase("clob")) {
						Clob cvalue = (Clob) value;
						rowData.put(fields[i].getName(), (value == null) ? ""
								: cvalue.getSubString(-7993874001254416383L, (int) cvalue.length()));
					} else {
						rowData.put(fields[i].getName(), (value == null) ? "" : value.toString());
					}
				}
				results.add(rowData);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (Exception localException1) {
				}
		}
		statement.close();
		return results;
	}

	Vector<Hashtable<String, String>> query(Connection con, String tableName, Hashtable<String, String> keys,
			String extendSql) throws SQLException {
		String[] tableAndColumns = getTableAndColumns(tableName);
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("select " + tableAndColumns[1] + " from " + tableAndColumns[0] + " ");
		sqlBuf.append(makeWhereSql(keys, extendSql));
		String sql = sqlBuf.toString();

		Vector<String> keyValues = new Vector<String>();

		if (keys != null) {
			for (Iterator<String> iter = keys.keySet().iterator(); iter.hasNext();)
				keyValues.add((String) keys.get(iter.next()));

		}

		return query(con, sql, keyValues);
	}

	int update(Connection con, String tableName, Hashtable<String, String> keys, Hashtable<String, String> values,
			String extendSql) throws SQLException {
		FieldInfo[] fields = EntityCache.getInstance().getEntityInfo(con, tableName).getFieldInfo();
		Vector<String> valuedColumn = new Vector<String>();
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("update " + tableName + " ");
		int index = 1;
		for (int i = 0; i < fields.length; ++i)
			if (values.get(fields[i].getName()) != null) {
				valuedColumn.add(fields[i].getName());
				if (index == 1)
					sqlBuf.append("set " + fields[i].getName() + " = ? ");
				else
					sqlBuf.append(", " + fields[i].getName() + " = ? ");

				++index;
			}

		sqlBuf.append(makeWhereSql(keys, extendSql));
		String sql = sqlBuf.toString();
		AppLogger.getInstance().debugLog("execute update:" + sql);
		PreparedStatement statement = con.prepareStatement(sql);

		index = 1;
		for (int i = 0; i < valuedColumn.size(); ++i) {
			statement.setString(index, ("".equals(values.get(valuedColumn.get(i)))) ? null
					: String.valueOf(values.get(valuedColumn.get(i))));
			++index;
		}
		if (keys != null)
			for (Iterator<String> iter = keys.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				statement.setString(index, (String) keys.get(key));
				++index;
			}

		int number = statement.executeUpdate();
		statement.close();
		return number;
	}

	int update(Connection con, String tableName, String preparedSql, Vector<String> keyValues) throws SQLException {
		AppLogger.getInstance().debugLog("execute update:" + preparedSql);
		PreparedStatement statement = con.prepareStatement(preparedSql);

		for (int i = 0; i < keyValues.size(); ++i) {
			statement.setString(i + 1, (String) keyValues.get(i));
		}

		int number = statement.executeUpdate();
		statement.close();
		return number;
	}

	void insert(Connection con, String tableName, Hashtable<String, String> values) throws SQLException {
		FieldInfo[] fields = EntityCache.getInstance().getEntityInfo(con, tableName).getFieldInfo();
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("insert into " + tableName + " values( ");
		for (int i = 0; i < fields.length; ++i)
			if (i == 0)
				sqlBuf.append("? ");
			else
				sqlBuf.append(", ? ");

		sqlBuf.append(")");
		String sql = sqlBuf.toString();
		AppLogger.getInstance().debugLog("execute insert:" + sql);
		PreparedStatement statement = con.prepareStatement(sql);

		for (int i = 0; i < fields.length; ++i) {
			statement.setString(i + 1,
					("".equals(values.get(fields[i].getName()))) ? null : (String) values.get(fields[i].getName()));
		}

		statement.execute();
		statement.close();
	}

	int delete(Connection con, String tableName, Hashtable<String, String> keys, String extendSql) throws SQLException {
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("delete from " + tableName + " ");
		sqlBuf.append(makeWhereSql(keys, extendSql));
		String sql = sqlBuf.toString();

		AppLogger.getInstance().debugLog("execute delete:" + sql);
		PreparedStatement statement = con.prepareStatement(sql);

		int index = 1;
		for (Iterator<String> iter = keys.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			statement.setString(index, (String) keys.get(key));
			++index;
		}

		int number = statement.executeUpdate();
		statement.close();
		return number;
	}

	void createTable(Connection con, String tableName, Hashtable<?, ?> columnInfo, String[] primaryKeys)
			throws SQLException {
		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append("create table " + tableName + "(");
		int index = 0;
		for (Iterator<?> iter = columnInfo.keySet().iterator(); iter.hasNext();) {
			++index;
			String key = (String) iter.next();
			if (index == 1) {
				sqlBuf.append(key + " " + columnInfo.get(key));
			}
			sqlBuf.append(", " + key + " " + columnInfo.get(key));
		}

		if ((primaryKeys != null) && (primaryKeys.length > 0))
			for (int i = 0; i < primaryKeys.length; ++i)
				if (i == 0)
					sqlBuf.append(", primary key(" + primaryKeys[i]);
				else
					sqlBuf.append("," + primaryKeys[i]);

		sqlBuf.append("))");
		String sql = sqlBuf.toString();
		AppLogger.getInstance().debugLog("create table:" + sql);
		Statement statement = con.createStatement();
		statement.execute(sql);
		statement.close();
	}

	void dropTable(Connection con, String tableName) throws SQLException {
		String sql = "drop table if exists " + tableName;
		AppLogger.getInstance().debugLog("drop table:" + sql);
		Statement statement = con.createStatement();
		statement.execute(sql);
		statement.close();
	}

	private String makeWhereSql(Hashtable<String, String> keys, String extendSql) {
		StringBuffer buf = new StringBuffer();
		int index = 1;
		if (keys != null)
			for (Iterator<String> iter = keys.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				if (index == 1)
					buf.append("where " + key + " = ? ");
				else
					buf.append("and " + key + " = ? ");

				++index;
			}

		if (extendSql != null)
			buf.append(" " + extendSql);

		return buf.toString();
	}

	public FieldInfo[] getQueryResultColumns(ResultSet rs) throws SQLException {
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
		FieldInfo[] fields = (FieldInfo[]) v.toArray(new FieldInfo[v.size()]);

		return fields;
	}

	public String[] getTableAndColumns(String tableAndColumnsStr) {
		String[] tableAndColumns = StringUtil.split(tableAndColumnsStr, ":");
		if (tableAndColumns.length == 0)
			throw new RuntimeErrorException(null, "Invalid table name, field name");

		if (tableAndColumns.length == 1) {
			tableAndColumns = new String[2];
			tableAndColumns[0] = tableAndColumnsStr;
			tableAndColumns[1] = "*";
		}
		return tableAndColumns;
	}
}