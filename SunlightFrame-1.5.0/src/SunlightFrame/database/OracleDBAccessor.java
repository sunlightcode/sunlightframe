package SunlightFrame.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * oracle数据库的访问器。
 * 
 */
public class OracleDBAccessor extends AbstractDBAccessor {
	Vector<Hashtable<String, String>> query(Connection con, String tableName, Hashtable<String, String> keys,
			String extendSql, int pageIndex, int pageNumber) throws SQLException {
		Vector<String> values = new Vector<String>();
		return query(con, tableName, makePreparedSql(tableName, keys, extendSql, values), values, pageIndex,
				pageNumber);
	}

	private String makePreparedSql(String tableName, Hashtable<String, String> keys, String extendSql,
			Vector<String> values) {
		StringBuffer buf = new StringBuffer();
		buf.append("select * from " + tableName + " ");
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
			int fromIndex = pageIndex * pageNumber + 1;
			int toIndex = pageIndex * pageNumber + pageNumber;
			preparedSql = "select * from (select a.*, rownum rn from (" + preparedSql + ") a ) where rn between "
					+ fromIndex + " and " + toIndex;
		}
		return query(con, preparedSql, values);
	}
}