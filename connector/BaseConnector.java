/**
 *   By Chongz
 */
package org.aprs.messageit.connector;

/**
 * @author toshiba
 *
 */
public abstract class BaseConnector {
	protected String name;
	protected boolean lastConnectFlag = false;
	
	/**
	 * @param name
	 */
	public BaseConnector(String name) {
		super();
		this.name = name;
	}
	public abstract Object getPort();
	public abstract void connect();
	public boolean getLastConnectFlag(){
		return lastConnectFlag;
	}
	
	public String getName(){
		return name;
	}
}
