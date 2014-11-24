/**
 *   By Chongz
 */
package org.aprs.messageit.sender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import gnu.io.SerialPort;

import org.aprs.messageit.account.UserAccount;
import org.aprs.messageit.packages.DataPackage;
import org.aprs.messageit.packages.SMSPackage;
import org.aprs.messageit.util.LogRecorder;
import org.aprs.messageit.util.MessageType;
import org.wengkai.gsm.SMSBox;

/**
 * @author toshiba
 *
 */
public class SMSSender extends BaseSender {
	private SMSBox theBox = null;
	
	/**
	 * @param intervalTimeMs
	 * @param theBox
	 */
	public SMSSender(SMSBox theBox, String name) {
		super(1000, name);
		this.theBox = theBox;
		if(name != null){
			startMessage = String.format("SMS Sending Start @ %1$s.", name);
		}
		allowedType = MessageType.SMS;
	}

	public void start() throws Exception{
		if(init()){
			LogRecorder.LogRecord(startMessage);
			setReady(true);
		} else {
			setReady(false);
			throw new Exception("Unable To Start Sending.");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.aprs.messageit.sender.BaseSender#init()
	 */
	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public void add(DataPackage dataPackage){
		if(dataPackage.IsValid() && dataPackage.getMessageType() == allowedType){
			UserAccount theAccount = new UserAccount();
			if(theAccount.loadFromFile(dataPackage.getSender())){
				if(theAccount.isInWhiteList(dataPackage.getReceiver())){
					send(dataPackage);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.aprs.messageit.sender.BaseSender#send(org.aprs.messageit.packages.DataPackage)
	 */
	@Override
	protected void send(DataPackage dataPackage) {
		theBox.send(new SMSPackage(dataPackage).toSmartMessage());
		LogRecorder.LogRecord("Message Sent : \n    " + dataPackage.toString());
	}
	
	public void setPort(Object port){
		theBox = (SMSBox) port;
	}

}
