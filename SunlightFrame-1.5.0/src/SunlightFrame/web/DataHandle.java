package SunlightFrame.web;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import SunlightFrame.config.AppConfig;
import SunlightFrame.util.DateTimeUtil;
import SunlightFrame.util.StringUtil;

public class DataHandle {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Vector<File> uploadFiles = new Vector<File>();
	private String outputPage = "";
	private JSPDataBean dataBean;
	private Vector<Cookie> cookiesToBeSaved = new Vector<Cookie>();

	DataHandle() {
		this.dataBean = new JSPDataBean();
	}

	void parse(HttpServletRequest request) throws Exception {
		this.request = request;
		request.setAttribute("JSPDataBean", this.dataBean);

		request.setCharacterEncoding("UTF-8");
		if ((request.getContentType() != null)
				&& (request.getContentType().toLowerCase().indexOf("multipart/form-data") >= 0)) {
			parseUploadFile();
		} else
			initFormData();
	}

	private void initFormData() {
		Hashtable<String, String> formData = this.dataBean.getFormDatas();
		Iterator<?> ite = this.request.getParameterMap().keySet().iterator();
		while (ite.hasNext()) {
			String parameterName = ite.next().toString();
			String parameter = this.request.getParameter(parameterName);
			if (parameter != null)
				formData.put(parameterName, parameter.trim());
		}
	}

	private void parseUploadFile() throws Exception {
		Hashtable<String, String> formData = this.dataBean.getFormDatas();
		String uploadDir = AppConfig.getInstance().getApplicationRoot() + File.separator + "upload" + File.separator;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(uploadDir + "tmp"));
		factory.setSizeThreshold(4194304);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		List<?> items = upload.parseRequest(this.request);

		Iterator<?> i = items.iterator();
		while (i.hasNext()) {
			FileItem fi = (FileItem) i.next();
			if (!(fi.isFormField()))
				if (!(fi.getName().equals(""))) {
					File file = new File(uploadDir + getNewFileName(fi.getName()));
					fi.write(file);
					String fileName = fi.getName();
					if ((fileName.toLowerCase().endsWith(".jsp")) || (fileName.toLowerCase().endsWith(".bat"))
							|| (fileName.toLowerCase().endsWith(".exe")))
						file.delete();
					else
						this.uploadFiles.add(file);
				}

				else
					formData.put(fi.getFieldName(), fi.getString("UTF-8").trim());
		}
	}

	private String getNewFileName(String originalFullName) throws Exception {
		int dotIndex = originalFullName.lastIndexOf(46);
		String fileName = StringUtil.getRandomString(10);
		return fileName + DateTimeUtil.getCurrentDateTime().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "")
				+ originalFullName.substring(dotIndex);
	}

	JSPDataBean getJSPDataBean() {
		return this.dataBean;
	}

	Vector<File> getUploadFiles() {
		return this.uploadFiles;
	}

	Object getSessionData(String name) {
		return this.request.getSession().getAttribute(name);
	}

	void setSessionData(String name, Object data) {
		this.request.getSession().setAttribute(name, data);
	}

	void removeSessionData(String name) {
		this.request.getSession().removeAttribute(name);
	}

	String getOutputPage() {
		return this.outputPage;
	}

	void setOutputPage(String outputPage) {
		this.outputPage = outputPage;
	}

	HttpServletRequest getRequest() {
		return this.request;
	}

	Vector<Cookie> getCookiesToBeSaved() {
		return this.cookiesToBeSaved;
	}

	void setCookie(Cookie cookie) {
		this.cookiesToBeSaved.add(cookie);
	}

	void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	HttpServletResponse getResponse() {
		return this.response;
	}
}