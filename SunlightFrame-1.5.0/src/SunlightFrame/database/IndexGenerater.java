package SunlightFrame.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import SunlightFrame.config.AppConfig;

/**
 * 数据表主键值生成器。用于产生一个整型的数据表主键值。
 * 
 */
public class IndexGenerater {
	public static final String INDEX_TABLE_NAME = "INDEXNUMBERS";
	public static final String TABLE_NAME_FIELD = "TABLENAME";
	public static final String INDEX_NUMBER_FIELD = "INDEXNUMBER";

	/**
	 * 得到一个新的数据表主键值
	 * 
	 * @param tableName
	 *            数据表表名。
	 * @param con
	 *            数据库连接。
	 * @return 新的数据表主键值。
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static synchronized String getTableIndex(String tableName, Connection con) throws SQLException {
		Hashtable keys = new Hashtable();
		keys.put("TABLENAME", tableName);
		Vector<?> results = DBProxy.query(con, "INDEXNUMBERS", keys, null);
		int index = 1000;
		if (results.size() > 0) {
			Hashtable<?, ?> hash = (Hashtable<?, ?>) results.get(0);
			index = Integer.parseInt(hash.get("INDEXNUMBER").toString());
			updateTableIndex(tableName, index + 1, con);
		} else {
			insertTableIndex(tableName, index + 1, con);
		}
		if (AppConfig.getInstance().getParameterConfig().getParameter("database.type").equalsIgnoreCase("oracle"))
			con.commit();

		return Integer.toString(index);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void insertTableIndex(String tableName, int index, Connection con) throws SQLException {
		Hashtable values = new Hashtable();
		values.put("TABLENAME", tableName);
		values.put("INDEXNUMBER", index);
		DBProxy.insert(con, "INDEXNUMBERS", values);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void updateTableIndex(String tableName, int index, Connection con) throws SQLException {
		Hashtable keys = new Hashtable();
		Hashtable values = new Hashtable();
		keys.put("TABLENAME", tableName);
		values.put("INDEXNUMBER", index);
		DBProxy.update(con, "INDEXNUMBERS", keys, values);
	}
}