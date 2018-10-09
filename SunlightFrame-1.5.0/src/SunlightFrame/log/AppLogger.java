/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.log;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import SunlightFrame.config.AppConfig;
import SunlightFrame.util.DateTimeUtil;
import SunlightFrame.util.SimpleFileWriter;

/**
 * 系统日志，用来记录系统运行的相关信息。
 * 
 */
public class AppLogger {
	private final static int DEBUG_TYPE = 1;

	private final static int INFO_TYPE = 2;

	private final static int ERROR_TYPE = 3;

	private static AppLogger logger = new AppLogger();

	private String logDir = null;

	private AppLogger() {

	}

	/**
	 * 得到唯一的系统日志实例。
	 * 
	 * @return 系统日志实例。
	 */
	public static AppLogger getInstance() {
		return logger;
	}

	/**
	 * 系统日志的初始化。 如果在系统运行的根目录下不存在log目录则创建之。并将所有的log文件记录到log目录中。
	 * 
	 * @param applicationDir
	 *            系统运行的根目录。
	 */
	public void init(String applicationDir) {
		String logDirPath = applicationDir + File.separator + "log";

		String logDirSet = AppConfig.getInstance().getParameterConfig().getParameter("log");
		if (logDirSet != null && !logDirSet.equals("")) {
			logDirPath = logDirSet;
		}

		File logDirectory = new File(logDirPath);
		if (!logDirectory.exists()) {
			logDirectory.mkdir();
		}
		logDir = logDirectory.getAbsolutePath();
	}

	/**
	 * 记录一般信息。
	 * 
	 * @param info
	 *            待记录的信息.
	 */
	public void infoLog(String info) {
		info = DateTimeUtil.getCurrentDateTime() + " system info:\t" + info + "\n";
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			System.out.print(info);
			return;
		}
		String logFilePath = getLogFilePath(INFO_TYPE);
		writeLog(logFilePath, info);
	}

	/**
	 * 记录错误信息。
	 * 
	 * @param info
	 *            待记录的信息。
	 */
	public void errorLog(String info) {
		info = DateTimeUtil.getCurrentDateTime() + " error:\t" + info + "\n";
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			System.out.print(info);
			return;
		}
		String logFilePath = getLogFilePath(ERROR_TYPE);
		writeLog(logFilePath, info);
	}

	/**
	 * 记录错误信息。
	 * 
	 * @param info
	 *            待记录的信息。
	 * @param exception
	 *            错误产生时的例外。
	 */
	public void errorLog(String info, Exception exception) {
		info = DateTimeUtil.getCurrentDateTime() + " error:\t" + info + "\n";
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			System.out.print(info);
			exception.printStackTrace();
			return;
		}
		String logFilePath = getLogFilePath(ERROR_TYPE);
		writeLog(logFilePath, info, exception);
	}

	/**
	 * 记录调试信息。
	 * 
	 * @param info
	 *            待记录的信息。
	 */
	public void debugLog(String info) {
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			info = DateTimeUtil.getCurrentDateTime() + " debug:\t" + info + "\n";
			System.out.print(info);
		}
	}

	/**
	 * 记录调试信息。
	 * 
	 * @param info
	 *            待记录的信息。
	 * @param exception
	 *            调试时抛出的例外。
	 */
	public void debugLog(String info, Exception exception) {
		if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
			info = DateTimeUtil.getCurrentDateTime() + " debug:\t" + info + "\n";
			System.out.print(info);
			exception.printStackTrace();
		}
	}

	private String getLogFilePath(int logType) {
		String path = logDir;
		String currentDate = DateTimeUtil.getCurrentDate().replace("/", "");
		switch (logType) {
		case DEBUG_TYPE:
			path += File.separator + "debug" + currentDate;
			break;
		case ERROR_TYPE:
			path += File.separator + "error" + currentDate;
			break;
		case INFO_TYPE:
			path += File.separator + "info" + currentDate;
			break;
		default:
			break;
		}
		return path += ".log";
	}

	private void writeLog(String logFilePath, String info) {
		try {
			SimpleFileWriter writer = new SimpleFileWriter(logFilePath);
			writer.setEncoding("UTF-8");
			writer.setWriteWay(true);
			writer.write(info);
		} catch (Exception e) {
			System.err.println("can't log info to path:" + logFilePath);
			e.printStackTrace();
		}
	}

	private void writeLog(String logFilePath, String info, Exception exception) {
		try {
			SimpleFileWriter writer = new SimpleFileWriter(logFilePath);
			writer.setEncoding("UTF-8");
			writer.setWriteWay(true);
			writer.write(info);
			StringWriter sw = new StringWriter();
			exception.printStackTrace(new PrintWriter(sw));
			writer.write("\r\n" + sw);
		} catch (Exception e) {
			System.err.println("can't log info to path:" + logFilePath);
			e.printStackTrace();
		}
	}

}
