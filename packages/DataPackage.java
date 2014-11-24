/**
 *   By Chongz
 */
package org.aprs.messageit.packages;

import org.aprs.messageit.util.CommandType;
import org.aprs.messageit.util.MessageType;

/**
 * @author toshiba
 * 
 */
public class DataPackage {
	private MessageType messageType = MessageType.INVALID;
	private CommandType commandType;

	// email address, phone number or APRS call sign
	private String receiver;
	private String sender;
	//

	private String content;
	private String gateway;
	private int MID = -1;

	@Override
	public String toString() {
		return "DataPackage [messageType=" + messageType + ", commandType="
				+ commandType + ", receiver=" + receiver + ", sender=" + sender
				+ ", content=" + content + ", gateway=" + gateway + ", MID="
				+ MID + "]";
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public CommandType getCommandType() {
		return commandType;
	}

	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public int getMID() {
		return MID;
	}

	public void setMID(int mID) {
		MID = mID;
	}
	
	public boolean IsValid(){
		if(messageType == MessageType.INVALID)
			return false;
		else return true;
	}
	
	public void setInvalid(){
		this.messageType = MessageType.INVALID;
	}

}
