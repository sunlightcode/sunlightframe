/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.database;

/**
 * 数据库表的元数据信息。
 *
 */
public class EntityInfo {
	private String tableName = null;
	private FieldInfo[] fields = null;

	public EntityInfo(String tableName, FieldInfo[] fields) {
		this.tableName = tableName;
		this.fields = fields;
	}

	public String getTableName() {
		return tableName;
	}

	public FieldInfo[] getFieldInfo() {
		return fields;
	}
}
