/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.web;

import java.lang.reflect.Constructor;

import SunlightFrame.config.AppConfig;
import SunlightFrame.config.Module;

/**
 * 由模块名得到该模块相对应的处理器。
 * 
 */
public class ProcessorFactory {
	/**
	 * 得到处理器
	 * 
	 * @param module
	 *            模块
	 * @param dataHandle
	 *            数据句柄
	 * @param con
	 *            数据库连接
	 * @return
	 * @throws Exception
	 */
	static AbstractModuleProcessor create(Module module, DataHandle dataHandle) throws Exception {
		String moduleName = module.getName();
		String processorName = AppConfig.getInstance().getParameterConfig().getParameter("applicationName")
				+ ".processor." + moduleName.substring(0, 1).toUpperCase()
				+ moduleName.substring(1, moduleName.length()) + "Processor";

		Class<?> c = AbstractModuleProcessor.class.getClassLoader().loadClass(

				processorName);
		Constructor<?> constructor = c.getConstructor(new Class[] { Module.class, DataHandle.class });
		AbstractModuleProcessor processor = (AbstractModuleProcessor) constructor
				.newInstance(new Object[] { module, dataHandle });
		return processor;
	}
}
