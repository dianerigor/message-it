/**
 * 
 */
package org.aprs.messageit.account;

/**
 * @author toshiba
 * 
 */
public class MailListenerAccount extends BaseMailAccount {
	private String protocol;

	/**
	 * @param username
	 * @param password
	 * @param host
	 */
	public MailListenerAccount(String username, String password, String host) {
		super(username, password, host);
		
		//protocol automatic detection
		if(host.indexOf("imap") != -1)
			this.protocol = "imap";
		else
			this.protocol = "pop3";
	}

	/**
	 * @param username
	 * @param password
	 * @param host
	 * @param protocol
	 */
	public MailListenerAccount(String username, String password, String host,
			String protocol) {
		super(username, password, host);
		this.protocol = protocol;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
