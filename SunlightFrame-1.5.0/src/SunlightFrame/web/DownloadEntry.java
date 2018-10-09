/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import SunlightFrame.config.AppConfig;
import SunlightFrame.log.AppLogger;

/**
 * 用来从download目录下载文件的Servlet类
 */
public class DownloadEntry extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 通过get方式下载文件，按request中filename属性下载指定的文件
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String downloadDir = AppConfig.getInstance().getApplicationRoot() + File.separator + "download"
				+ File.separator;

		String fileName = request.getParameter("fileName");
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		FileInputStream fileInputStream = null;
		try {
			// 打开指定文件的流信息
			fileInputStream = new java.io.FileInputStream(downloadDir + fileName);

			// 写出流信息
			int i;
			while ((i = fileInputStream.read()) != -1) {
				response.getOutputStream().write(i);
			}
		} catch (Exception e) {
			AppLogger.getInstance().errorLog("error happened when download file: " + fileName, e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
			}
			response.getOutputStream().close();
		}
	}

}
