/**
 * 
 */
package org.aprs.messageit.packages;

import org.aprs.messageit.server.MessageItServer;
import org.aprs.messageit.util.MessageType;

/**
 * @author toshiba
 * 
 */
public class EmailPackage extends TransferablePackage {
	private String subject;
	private String content;
	private String receiver;
	private String sender;
	private boolean isValid = false;

	/**
	 * Construct from DataPackage
	 * 
	 * @param dataPackage
	 * @throws Exception
	 */
	public EmailPackage(DataPackage dataPackage){
		super();
		parsePackage(dataPackage);
	}
	
	public void parsePackage(DataPackage dataPackage){
		try{
			if (dataPackage.getMessageType() != MessageType.EMAIL)
				throw new Exception("Package Is Not An Email.");
			this.subject = "APRS Message From " + dataPackage.getSender();
			this.content = String
					.format("%1$s\n-------------------\nMessageIt APRS To Email Geteway\nManager Callsign %2$s\n",
							dataPackage.getContent(), MessageItServer.EMailManager);
			this.receiver = dataPackage.getReceiver();
			this.sender = dataPackage.getSender();
			this.setValid(true);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public DataPackage toDataPackage() {
		try{
			if (!this.IsValid())
				throw new Exception("Email Package Is Invalid.");
			DataPackage dataPackage = new DataPackage();
			dataPackage.setSender(sender);
			dataPackage.setReceiver(receiver);
			dataPackage.setContent(content);
			return dataPackage;
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRecevier() {
		return receiver;
	}

	public void setRecevier(String recevier) {
		this.receiver = recevier;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public boolean IsValid() {
		return isValid;
	}

	public void setValid(boolean v) {
		this.isValid = v;
	}

}
