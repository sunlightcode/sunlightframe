/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @deprecated 数据库备份与恢复工具
 */
public class DBUtil {
	public static int bakUpWinMySql(String mysqlDir, String user, String password, String database,
			String bakupFilePath) throws Exception {
		String cmd = mysqlDir + File.separator + "mysqldump.exe " + database + " -u" + user + " -p" + password
				+ " --default-character-set=utf8 --result-file=\"" + bakupFilePath + "\"";
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			return p.exitValue();
		} catch (Exception e) {
			throw e;
		}
	}

	public static int restoreWinMySql(String mysqlDir, String user, String password, String database,
			String bakupFilePath) throws Exception {
		try {
			Runtime rt = Runtime.getRuntime();
			Process child = rt.exec(
					mysqlDir + File.separator + "mysql.exe " + " -u" + user + " -p" + password + " " + database + " ");
			OutputStream out = child.getOutputStream();// 控制台的输入信息作为输出流
			String inStr;
			StringBuffer sb = new StringBuffer("");
			String outStr;
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(bakupFilePath), "utf8"));
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();

			OutputStreamWriter writer = new OutputStreamWriter(out, "utf8");
			writer.write(outStr);
			writer.flush();
			out.close();
			br.close();
			writer.close();
			child.waitFor();
			return child.exitValue();
		} catch (Exception e) {
			throw e;
		}
	}
}
