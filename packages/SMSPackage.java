/**
 *   By Chongz
 */
package org.aprs.messageit.packages;

import java.io.File;

import org.aprs.messageit.account.UserAccount;
import org.aprs.messageit.server.Messages;
import org.aprs.messageit.util.CommandType;
import org.aprs.messageit.util.MessageType;
import org.wengkai.gsm.SmartMessage;

/**
 * @author toshiba
 *
 */
public class SMSPackage extends TransferablePackage {
	private String sender;
	private String receiver;
	private String content;
	
	public SMSPackage(DataPackage dataPackage){
		super();
		parsePackage(dataPackage);
	}
	
	public SMSPackage(SmartMessage sms){
		super();
		parseSmartMessage(sms);
	}
	
	private void parseSmartMessage(SmartMessage sms){		
		String[] cons = new String(sms.getContent()).split("\n");
		int len = cons.length;
		
		String receiver = cons[0];
		if(receiver.length() > 9){
			receiver = receiver.substring(0, 8);
		}
		this.receiver = receiver;
		
		int t = cons[len-1].indexOf("DE:");
		String sender = null;
		if(t == -1){
			sender = UserAccount.findCallsign("SMS-number", sms.getPhone());
		} else {
			len--;
			sender = cons[cons.length-1].substring(t + "DE:".length());
			if(sender.length() > 9){
				sender = sender.substring(0, 8);
			}
		}
		this.sender = sender;
		
		String content = new String();
		for(int i = 1; i < len; ++i){
			content += cons[i];
		}
		this.content = content;
	}
	
	@Override
	public void parsePackage(DataPackage thePackage) {
		sender = thePackage.getSender();
		receiver = thePackage.getReceiver();
		content = thePackage.getContent();
	}

	public SmartMessage toSmartMessage() {
		SmartMessage sms = new SmartMessage();
		sms.setPhone(receiver);
		sms.setContent(content);
		return sms;
	}
	
	
	@Override
	public DataPackage toDataPackage() {
		DataPackage thePackage = new DataPackage();
		thePackage.setMessageType(MessageType.APRS);
		thePackage.setCommandType(CommandType.SEND);
		if(sender == null || receiver == null){
			thePackage.setInvalid();
		}
		thePackage.setSender(sender);
		thePackage.setReceiver(receiver);
		thePackage.setContent(content);
		thePackage.setGateway(Messages.getString("SMS-port"));
		return thePackage;
	}
	
	//public SmartMessage toSmartMessage)(){}
}
