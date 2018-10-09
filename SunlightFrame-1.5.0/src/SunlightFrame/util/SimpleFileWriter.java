/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * 文件写工具。
 * 
 */
public class SimpleFileWriter {
	private String path;

	private boolean append = false;

	private String encoding;

	/**
	 * 构造方法。
	 * 
	 * @param path
	 *            文件路径。
	 */
	public SimpleFileWriter(String path) {
		this.path = path;
	}

	/**
	 * 设置文件的写方式。
	 * 
	 * @param append
	 *            为"true"时以追加的方式写入，为"false"时以覆盖的方式写入。
	 */
	public void setWriteWay(boolean append) {
		this.append = append;
	}

	/**
	 * 设置写文件时所用的字符集。
	 * 
	 * @param encoding
	 *            写文件时所用的字符集。
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * 向文件写入内容。如果不设置append的话，默认以覆盖的方式写入。
	 * 
	 * @param s
	 * @throws Exception
	 */
	public void write(String s) throws Exception {
		FileOutputStream os = null;
		OutputStreamWriter osw = null;

		if (append) {
			os = new FileOutputStream(path, append);
		} else {
			os = new FileOutputStream(path);
		}

		if (encoding != null) {
			osw = new OutputStreamWriter(os, encoding);
		} else {
			osw = new OutputStreamWriter(os);
		}
		BufferedWriter writer = new BufferedWriter(osw);
		writer.write(s);
		writer.close();
	}

	public void write(InputStream inputStream) throws Exception {
		FileOutputStream os = null;
		if (append) {
			os = new FileOutputStream(path, append);
		} else {
			os = new FileOutputStream(path);
		}
		byte[] datas = new byte[1024];
		int i = 0;

		while ((i = inputStream.read(datas)) != -1) {
			os.write(datas, 0, i);
		}
		inputStream.close();
		os.close();
	}
}
