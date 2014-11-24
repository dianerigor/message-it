/**
 * 
 */
package org.aprs.messageit.util;

/**
 * @author toshiba
 *
 */
public enum CommandType {
	SEND("S"), DELETE("D"), ACK("A"), REJ("R"), LOGIN("L");
	
	private final String commandType;

	/**
	 * @param commandType
	 */
	private CommandType(String commandType) {
		this.commandType = commandType;
	}

	public String getCommandType() {
		return commandType;
	}
	
}
