/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import SunlightFrame.config.AppConfig;
import SunlightFrame.config.AppParametersConfig;
import SunlightFrame.log.AppLogger;

/**
 * 邮件发送器。
 * 
 */
public class Mailer {
	private Mailer() {

	}

	/**
	 * 发送一封简单邮件。
	 * 
	 * @param from
	 *            送信人mail地址。
	 * @param to
	 *            收信人mail地址。
	 * @param subject
	 *            邮件主题。
	 * @param body
	 *            邮件内容。
	 */
	public static boolean sendSimpleMail(String to, String subject, String body) {
		AppParametersConfig parameters = AppConfig.getInstance().getParameterConfig();

		String smtpHost = parameters.getParameter("mailServer.smtpHost");
		String userName = parameters.getParameter("mailServer.userName");
		String password = parameters.getParameter("mailServer.password");

		return sendSimpleMail(smtpHost, userName, password, "", to, subject, body);
	}

	/**
	 * 发送一封简单邮件。
	 * 
	 * @param smtpHost
	 *            邮件服务器地址
	 * @param userName
	 *            帐户名
	 * @param password
	 *            密码
	 * @param from
	 *            送信人mail地址。
	 * @param to
	 *            收信人mail地址。
	 * @param subject
	 *            邮件主题。
	 * @param body
	 *            邮件内容。
	 */
	public static boolean sendSimpleMail(String smtpHost, String userName, String password, String userNick, String to,
			String subject, String body) {
		try {
			Session mailSession = getMailSession(smtpHost, userName, password);
			MimeMessage message = new MimeMessage(mailSession);
			InternetAddress fromAddress = new InternetAddress(userName);
			if (userNick != null && !userNick.equals("")) {
				fromAddress.setPersonal(userNick);
			}
			message.setFrom(fromAddress);
			InternetAddress[] address = new InternetAddress[] { new InternetAddress(to) };
			message.setRecipients(Message.RecipientType.TO, address);
			message.setSubject(subject, "GB2312");

			message.setContent(body, "text/plain");
			message.setText(body, "GB2312");
			Transport.send(message);
			return true;
		} catch (Exception e) {
			AppLogger.getInstance().errorLog("Send mail to " + to + " failed! ", e);
			return false;
		}
	}

	/**
	 * 发送一封HTML邮件。
	 * 
	 * @param from
	 *            送信人mail地址。
	 * @param to
	 *            收信人mail地址。
	 * @param subject
	 *            邮件主题。
	 * @param body
	 *            邮件内容。
	 */
	public static boolean sendHtmlMail(String to, String subject, String body) {
		AppParametersConfig parameters = AppConfig.getInstance().getParameterConfig();

		String smtpHost = parameters.getParameter("mailServer.smtpHost");
		String userName = parameters.getParameter("mailServer.userName");
		String password = parameters.getParameter("mailServer.password");

		return sendHtmlMail(smtpHost, userName, password, "", to, subject, body);
	}

	/**
	 * 发送一封HTML邮件。
	 * 
	 * @param smtpHost
	 *            邮件服务器地址
	 * @param userName
	 *            帐户名
	 * @param password
	 *            密码
	 * @param userNick
	 *            发信人显示的nick。
	 * @param to
	 *            收信人mail地址。
	 * @param subject
	 *            邮件主题。
	 * @param body
	 *            邮件内容。
	 */
	public static boolean sendHtmlMail(String smtpHost, String userName, String password, String userNick, String to,
			String subject, String body) {
		try {
			Session mailSession = getMailSession(smtpHost, userName, password);
			MimeMessage message = new MimeMessage(mailSession);
			InternetAddress fromAddress = new InternetAddress(userName);
			if (userNick != null && !userNick.equals("")) {
				fromAddress.setPersonal(userNick);
			}
			message.setFrom(fromAddress);
			InternetAddress[] address = new InternetAddress[] { new InternetAddress(to) };
			message.setRecipients(Message.RecipientType.TO, address);
			message.setSubject(subject, "utf-8");

			MimeMultipart mainPart = new MimeMultipart();
			MimeBodyPart html = new MimeBodyPart();
			html.setContent(body, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);

			message.setContent(mainPart);
			Transport.send(message);
			return true;
		} catch (Exception e) {
			AppLogger.getInstance().errorLog("Send mail to " + to + " failed! ", e);
			return false;
		}
	}

	private static Session getMailSession(String smtpHost, String userName, String password) {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.auth", "true");
		props.put("mail.mime.charset", "GB2312");

		SMTPAuthenticator authenticator = new SMTPAuthenticator(userName, password);
		return Session.getInstance(props, authenticator);
	}
}
