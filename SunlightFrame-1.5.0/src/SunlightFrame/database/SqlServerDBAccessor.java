package SunlightFrame.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * SqlServer数据库的访问器。
 * 
 */
public class SqlServerDBAccessor extends AbstractDBAccessor {
	Vector<Hashtable<String, String>> query(Connection con, String tableName, Hashtable<String, String> keys,
			String extendSql, int pageIndex, int pageNumber) throws SQLException {
		Vector<String> values = new Vector<String>();
		return query(con, tableName, makePreparedSql(tableName, keys, extendSql, values), values, pageIndex,
				pageNumber);
	}

	private String makePreparedSql(String tableName, Hashtable<String, String> keys, String extendSql,
			Vector<String> values) throws SQLException {
		StringBuffer buf = new StringBuffer();
		String[] tableAndColumns = getTableAndColumns(tableName);
		buf.append("select " + tableAndColumns[1] + " from " + tableAndColumns[0] + " ");
		int index = 1;
		if (keys != null)
			for (Iterator<String> iter = keys.keySet().iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				if (index == 1)
					buf.append("where " + key + " = ? ");
				else
					buf.append("and " + key + " = ? ");

				values.add((String) keys.get(key));
				++index;
			}

		if (extendSql != null)
			buf.append(" " + extendSql);

		return buf.toString();
	}

	Vector<Hashtable<String, String>> query(Connection con, String tableName, String preparedSql, Vector<String> values,
			int pageIndex, int pageNumber) throws SQLException {
		if (pageNumber != 0) {
			String orderBySql = "order by " + getPrimaryKeys(con, tableName);
			int index = preparedSql.indexOf("order by");
			if (index > 0) {
				orderBySql = preparedSql.substring(index);
				preparedSql = preparedSql.substring(0, index);
			}

			int fromIndex = pageIndex * pageNumber + 1;
			int toIndex = pageIndex * pageNumber + pageNumber;

			int selectIndex = preparedSql.indexOf("select");

			preparedSql = preparedSql.substring(selectIndex + 6);

			preparedSql = "select row_number() over (" + orderBySql + ") as rownumber," + preparedSql;

			preparedSql = "select * from (" + preparedSql + ") _myResults where rownumber between " + fromIndex
					+ " and " + toIndex;
		}
		return query(con, preparedSql, values);
	}

	private String getPrimaryKeys(Connection con, String tableName) throws SQLException {
		if (tableName.indexOf("_") > 0)
			tableName = tableName.substring(0, tableName.indexOf("_"));

		FieldInfo[] fields = EntityCache.getInstance().getEntityInfo(con, tableName).getFieldInfo();
		String primaryKey = "";
		for (int i = 0; i < fields.length; ++i)
			if (fields[i].isPrimaryKey())
				primaryKey = primaryKey + "+" + fields[i].getName();

		if (primaryKey.indexOf("+") >= 0)
			primaryKey = primaryKey.substring(1);

		if (primaryKey.indexOf("+") >= 0)
			primaryKey = "(" + primaryKey + ")";

		return primaryKey;
	}
}