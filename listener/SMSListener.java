/**
 *   By Chongz
 */
package org.aprs.messageit.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import gnu.io.SerialPort;

import org.aprs.messageit.packages.CommandPackage;
import org.aprs.messageit.packages.DataPackage;
import org.aprs.messageit.packages.SMSPackage;
import org.aprs.messageit.util.CommandType;
import org.aprs.messageit.util.ListenerCallback;
import org.aprs.messageit.util.LogRecorder;
import org.wengkai.gsm.SMSBox;
import org.wengkai.gsm.SmartMessage;

/**
 * @author toshiba
 *
 */
public class SMSListener extends BaseListener {
	//final static int intervalTime = 18000;
	//private SerialPort thePort;
	//private static final String URC_SMS = "+CMT:";

	private SMSBox theBox = null;
	
	/**
	 * @param intervalTime
	 * @param uploadCallback
	 * @param theBox
	 */
	public SMSListener(ListenerCallback uploadCallback, SMSBox theBox, String name) {
		super(1000, uploadCallback, name);
		this.theBox = theBox;
		if(name != null){
			startMessage = String.format("SMS Listening Start @ %1$s.", name);
		}
	}
	
	public void start() throws Exception{
		if(init()){
			LogRecorder.LogRecord(startMessage);
			theBox.addSMSListener(new org.wengkai.gsm.SMSListener(){
				public void smsRcvd(SmartMessage msg){
					uploadDataPackage(new SMSPackage(msg).toDataPackage());
				}
			});
		} else {
			throw new Exception("Unable To Start Listening.");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.aprs.messageit.listener.BaseListener#init()
	 */
	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setPort(Object port){
		this.theBox = (SMSBox) port;
	}
	
	/*public String readln() throws IOException {
		String s = new String("");
		if(thePort == null)// || !thePort.)
			throw new IOException();
		BufferedReader inReader = new BufferedReader(new InputStreamReader(thePort.getInputStream()));
		s = inReader.readLine();
		while(!s.startsWith(URC_SMS)){
			s = inReader.readLine();
		}
		s = inReader.readLine();
		//inReader.close();
		return s;
	}*/
	
	/* (non-Javadoc)
	 * @see org.aprs.messageit.listener.BaseListener#getDataPackage()
	 */
	@Override
	public DataPackage[] getDataPackage() {
//		DataPackage[] thePackage = new DataPackage[1];
//		try {
//			String sms = readln();
//			if(sms == null){
//				thePackage[0] = null;
//			} else {
//				thePackage[0] = new SMSPackage(sms).toDataPackage();
//			}			
//		} catch (IOException e) {//| SocketTimeoutException e) {
//			// TODO Auto-generated catch block
//			/*thePackage[0] = (new CommandPackage(CommandType.LOGIN, getName(), getName())).toDataPackage();
//			e.printStackTrace();
//			pause(2 * Integer.parseInt("60000"));*/
//		}
//		return thePackage;
		return null;
	}

}
