/**
 * SunlightFrame
 *    
 * (C) Copyright 光链科技
 * 本内容仅限于光链科技授权使用，未经授权不得用于商业用途。谢谢合作！
 */
package SunlightFrame.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * SMTP权限验证。
 */
class SMTPAuthenticator extends Authenticator {
	private String username;

	private String password;

	SMTPAuthenticator(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.password);
	}
}
