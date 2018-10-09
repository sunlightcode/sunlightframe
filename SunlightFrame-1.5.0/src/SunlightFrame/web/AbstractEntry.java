/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import SunlightFrame.config.AppConfig;
import SunlightFrame.config.Module;
import SunlightFrame.exception.FrameException;
import SunlightFrame.log.AppLogger;

/**
 * 浏览器访问系统服务器的入口servlet。 从用户提交的数据中提取出操作的模块以及动作，完成相应的处理，并输出html数据。
 * 
 */
public abstract class AbstractEntry extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 重载父类的方法，完成系统初始化
	 */
	public final void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			String appRoot = config.getServletContext().getRealPath("/");
			AppConfig.getInstance().init(appRoot);
			AppLogger.getInstance().init(appRoot);
			entryInit();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * 重载父类的方法
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 重载父类的方法
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DataHandle dataHandle = new DataHandle();
		try {
			try {
				dataHandle.parse(request);
				dataHandle.setResponse(response);
			} catch (Exception e) {
				throw new FrameException("Parsing request falled...", e);
			}

			ServletContext sc = getServletContext();

			// the module is not found
			String moduleName = dataHandle.getJSPDataBean().getFormData("module");
			String actionName = dataHandle.getJSPDataBean().getFormData("action");

			Module module = AppConfig.getInstance().getModuleConfig().getModule(moduleName);
			if (module == null) {
				if (AppConfig.getInstance().getParameterConfig().isDebugModel()) {
					throw new FrameException("there is no module:" + moduleName);
				} else {
					sc.getRequestDispatcher("/jsp/index.jsp").forward(request, response);
				}
			} else if (!checkPriority(moduleName, actionName, request)) {
				sc.getRequestDispatcher("/jsp/" + getPriorityErorrPage()).forward(request, response);
			} else {
				AbstractModuleProcessor processor = ProcessorFactory.create(module, dataHandle);
				processor.doAction();

				Connection con = (Connection) request.getAttribute(FrameKeys.DATABASE_CONNECTION);
				if (con != null && con.getAutoCommit() == false) {
					con.commit();
				}

				// dispatch output
				List<Cookie> cookies = dataHandle.getCookiesToBeSaved();
				for (int i = 0; i < cookies.size(); i++) {
					response.addCookie(cookies.get(i));
				}
				if (!dataHandle.getOutputPage().equals("")) {
					sc.getRequestDispatcher("/jsp/" + dataHandle.getOutputPage()).forward(request, response);
				}
			}
		} catch (Exception e) {
			try {
				Connection con = (Connection) request.getAttribute(FrameKeys.DATABASE_CONNECTION);
				if (con != null && con.getAutoCommit() == false) {
					con.rollback();
				}
			} catch (Exception ex) {
			}

			AppLogger.getInstance().errorLog(e.getMessage(), e);
			if ("true".equals(AppConfig.getInstance().getParameterConfig().getParameter("useExceptionJsp"))) {
				request.setAttribute(FrameKeys.EXCEPTION_INFO, e.getMessage());
				getServletContext().getRequestDispatcher("/jsp/exception.jsp").forward(request, response);
			} else {
				throw new ServletException(e);
			}
		} finally {
			try {
				Connection con = (Connection) request.getAttribute(FrameKeys.DATABASE_CONNECTION);
				if (con != null) {
					con.close();
				}
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 完成系统的初始化操作，诸如： 从数据库中导入常用数据到内存中。 启动系统的守候进程。
	 *
	 */
	public abstract void entryInit();

	/**
	 * 验证用户对模块和动作的操作权限。
	 * 
	 * @param moduleName
	 * @param actionName
	 * @param request
	 * @return 用户是否可以操作。
	 */
	public abstract boolean checkPriority(String moduleName, String actionName, HttpServletRequest request);

	/**
	 * 得到在用户权限验证失败的情况下，用户重定向到的错误页面。
	 * 
	 * @return 用户重定向到的错误页面名称。
	 */
	public abstract String getPriorityErorrPage();
}
