/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.database;

/**
 * 数据库字段的元数据信息。
 * 
 */
public class FieldInfo {
	private String name;
	private String typeName;
	private int maxLength;
	private boolean isNullValid;
	private boolean isPrimaryKey = false;

	public FieldInfo(String name, String typeName, String maxLength, String isNullValid) {
		this.name = name;
		this.typeName = typeName;
		this.maxLength = Integer.parseInt(maxLength);
		if ("NO".equalsIgnoreCase(isNullValid)) {
			this.isNullValid = false;
		} else {
			this.isNullValid = true;
		}
	}

	public String getName() {
		return name;
	}

	public String getTypeName() {
		return typeName;
	}

	int getMaxLength() {
		return maxLength;
	}

	boolean isNullValid() {
		return isNullValid;
	}

	void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	boolean isPrimaryKey() {
		return isPrimaryKey;
	}
}
