/**
 * 
 */
package org.aprs.messageit.account;

/**
 * @author toshiba
 *
 */
public class APRSAccount {
	private String host;
	private int port;
	/**
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 */
	public APRSAccount(String host, int port, String username, String password) {
		super();
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	private String username;
	private String password;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
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
}
