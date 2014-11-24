/**
 * 
 */
package org.aprs.messageit.account;

import org.aprs.messageit.util.StringType;

/**
 * @author toshiba
 *
 */
public class MailSenderAccount extends BaseMailAccount {
	private String fromAddress;

	/**
	 * @param username
	 * @param password
	 * @param host
	 */
	public MailSenderAccount(String username, String password, String host) {
		super(username, password, host);
		
		//fromAddress automatic detection
		if(StringType.getStringType(username) == StringType.EMAILADDRESS)
			this.fromAddress = username;
		else{
			this.fromAddress = host;
			this.fromAddress = this.fromAddress.replace("smtp.", String.format("%1$s@", username));
		}
	}

	/**
	 * @param username
	 * @param password
	 * @param host
	 * @param fromAddress
	 */
	public MailSenderAccount(String username, String password, String host,
			String fromAddress) {
		super(username, password, host);
		this.fromAddress = fromAddress;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	
}
