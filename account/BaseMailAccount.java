/**
 *   By Chongz
 */
package org.aprs.messageit.account;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @author toshiba
 *
 */
public class BaseMailAccount extends Authenticator{
	private String username;
	private String password;
	private String host;
	
	
	/**
	 * @param username
	 * @param password
	 * @param host
	 */
	public BaseMailAccount(String username, String password, String host) {
		super();
		this.username = username;
		this.password = password;
		this.host = host;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	/* (non-Javadoc)
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(getUsername(), getPassword());
	}


}
