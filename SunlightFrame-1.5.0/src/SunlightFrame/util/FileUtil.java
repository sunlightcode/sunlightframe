/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import SunlightFrame.log.AppLogger;

/**
 * 文件操作工具
 */
public class FileUtil {
	/**
	 * 复制文件
	 * 
	 * @param from
	 *            源文件路径
	 * @param to
	 *            目标文件路径
	 * @throws Exception
	 */
	public static void copy(String from, String to) throws Exception {
		int BUFF_SIZE = 100000;
		byte[] buffer = new byte[BUFF_SIZE];
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);
			while (true) {
				synchronized (buffer) {
					int amountRead = in.read(buffer);
					if (amountRead == -1) {
						break;
					}
					out.write(buffer, 0, amountRead);
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	public static void moveFile(File f, String dirPath) {
		// f.renameTo(new File(dirPath + File.separator + f.getName()));
		try {
			FileUtils.copyFileToDirectory(f, new File(dirPath));
			f.delete();
		} catch (IOException e) {
			AppLogger.getInstance().errorLog("copy File " + f.getName() + " error", e);
		}
	}

	public static void deleteFile(String filePath) {
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
	}
}
