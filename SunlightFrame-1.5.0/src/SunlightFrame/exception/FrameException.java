/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.exception;

/**
 * SimpleWebFrame框架内自定义的各种异常。
 * 
 */
public class FrameException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造方法。
	 * 
	 * @param e
	 *            待包装的例外。
	 */
	public FrameException(Exception e) {
		super(e);
	}

	/**
	 * 构造方法。
	 * 
	 * @param message
	 *            例外信息。
	 * @param e
	 *            待包装的例外。
	 */
	public FrameException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * 构造方法。
	 * 
	 * @param message
	 *            例外信息。
	 */
	public FrameException(String message) {
		super("Frame exception : " + message);
	}
}
