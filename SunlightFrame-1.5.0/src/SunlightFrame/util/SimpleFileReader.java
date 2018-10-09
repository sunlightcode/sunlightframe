/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 文件读取工具。
 * 
 */
public class SimpleFileReader {
	private String path;
	private String encoding;

	/**
	 * 构造方法。
	 * 
	 * @param path
	 *            文件路径。
	 */
	public SimpleFileReader(String path) {
		this.path = path;
	}

	/**
	 * 设置读取文件时所用的字符集。
	 * 
	 * @param encoding
	 *            读取文件时所用的字符集。
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 得到文件的内容。
	 * 
	 * @return 文件的内容。
	 * @throws Exception
	 */
	public String getContent() throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
		StringBuffer buffer = new StringBuffer();
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line + "\n");
		}
		reader.close();
		return buffer.toString();
	}
}
