/**
 * 
 */
package org.aprs.messageit.packages;

import org.aprs.messageit.util.CommandType;
import org.aprs.messageit.util.MessageType;

/**
 * @author toshiba
 *
 */
public class CommandPackage extends TransferablePackage{
	private CommandType commandType;
	private String sender;
	private String parameter;
	
	public CommandPackage(DataPackage thePackage){
		super();
		parsePackage(thePackage);
	}
	

	
	/**
	 * @param commandType
	 * @param sender
	 * @param parameter
	 */
	public CommandPackage(CommandType commandType, String sender,
			String parameter) {
		super();
		this.commandType = commandType;
		this.sender = sender;
		this.parameter = parameter;
	}



	public CommandType getCommandType() {
		return commandType;
	}

	public String getSender() {
		return sender;
	}

	public String getParameter() {
		return parameter;
	}

	@Override
	public void parsePackage(DataPackage thePackage) {
		commandType = thePackage.getCommandType();
		sender = thePackage.getSender();
		parameter = thePackage.getContent();
	}

	@Override
	public DataPackage toDataPackage() {
		DataPackage thePackage = new DataPackage();
		thePackage.setMessageType(MessageType.COMMAND);
		thePackage.setCommandType(commandType);
		thePackage.setSender(sender);
		thePackage.setContent(parameter);
		return thePackage;
	}
	
}
