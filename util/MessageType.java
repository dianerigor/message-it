/**
 * 
 */
package org.aprs.messageit.util;

/**
 * @author toshiba
 * 
 */
public enum MessageType {
	INVALID("INVALID"), SMS("SMS"), EMAIL("EMAIL"), APRS("APRS"), COMMAND("COMMAND"), SYSTEM("SYSTEM");

	private final String messageType;

	/**
	 * @param messageType
	 */
	private MessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageType() {
		return messageType;
	}
	
	public static MessageType fromString(String text) {
		if (text != null) {
			text.trim();
			for (MessageType b : MessageType.values()) {
				if (text.equalsIgnoreCase(b.messageType)) {
					return b;
				}
			}
	    }
	    return null;
	}

}
