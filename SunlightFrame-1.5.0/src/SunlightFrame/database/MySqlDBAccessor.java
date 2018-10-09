package SunlightFrame.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

/**
 * mysql数据库的访问器。
 * 
 */
public class MySqlDBAccessor extends AbstractDBAccessor {
	/**
	 * 查询数据。
	 * 
	 * @param con
	 *            数据库连接。
	 * @param tableName
	 *            表名。
	 * @param keys
	 *            键值。
	 * @param extendSql
	 *            拓展的sql语句，如order by等等。
	 * @param pageIndex
	 *            分页的页序号。
	 * @param pageNumber
	 *            每页的数据条数。
	 * @return 查询的结果集。
	 * @throws SQLException
	 */
	Vector<Hashtable<String, String>> query(Connection con, String tableName, Hashtable<String, String> keys,
			String extendSql, int pageIndex, int pageNumber) throws SQLException {
		if (pageNumber != 0)
			if (extendSql != null) {
				extendSql = extendSql + " limit " + (pageIndex * pageNumber) + ", " + pageNumber;
			} else
				extendSql = " limit " + (pageIndex * pageNumber) + ", " + pageNumber;

		return query(con, tableName, keys, extendSql);
	}

	Vector<Hashtable<String, String>> query(Connection con, String tableName, String preparedSql, Vector<String> values,
			int pageIndex, int pageNumber) throws SQLException {
		if (pageNumber != 0)
			preparedSql = preparedSql + " limit " + (pageIndex * pageNumber) + ", " + pageNumber;

		return query(con, preparedSql, values);
	}
}