/**
 * 
 */
package org.aprs.messageit.sender;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.aprs.messageit.account.APRSAccount;
import org.aprs.messageit.packages.DataPackage;
import org.aprs.messageit.util.MessageType;

/**
 * @author toshiba
 *
 */
public class APRSSender extends BaseSender {

	private APRSAccount theAccount;
	private Socket theSocket;
	

	/**
	 * @param theAccount
	 * @param outWriter
	 */
	public APRSSender(int intervalTimeMs, APRSAccount theAccount, Socket theSocket) {
		super(intervalTimeMs, theAccount.getUsername());
		this.theAccount = theAccount;
		this.theSocket = theSocket;
		if(theAccount != null){
			startMessage = String.format("APRS Sending Start : %1$s @ %2$s", theAccount.getUsername(), theAccount.getHost());
		}
		allowedType = MessageType.APRS;
	}
	

	public void println(String s){
		try{
			PrintWriter outWriter = new PrintWriter(theSocket.getOutputStream(), true);
			outWriter.println(s);
			//outWriter.close();
		} catch (IOException e){
			setReady(false);
			e.printStackTrace();
		}
	}
	
	public void setPort(Object port){
		this.theSocket = (Socket) port;
	}
	
	/* (non-Javadoc)
	 * @see org.aprs.messageit.sender.BaseSender#init()
	 */
	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see org.aprs.messageit.sender.BaseSender#send(org.aprs.messageit.util.DataPackage)
	 */
	@Override
	protected void send(DataPackage dataPackage) {
		if(dataPackage.getContent().length() > 67){
			dataPackage.setContent(dataPackage.getContent().substring(0, 66));
		}
		String s = String.format(":%1$s:%2$s", dataPackage.getReceiver(), dataPackage.getContent());
		if(dataPackage.getMID() == -1){
			dataPackage.setMID((int)(Math.random() * 1000));
		}
		if(dataPackage.getMID() != -1){
			s += String.format("{%1$d", dataPackage.getMID());
		}
		s = encodeToTCPIP(s, dataPackage.getSender());
		println(s);
	}
	
	private static String encodeToTCPIP(String message, String senderCallsign){
		String encodedMessage;
		encodedMessage = senderCallsign + ">MSGT,TCPIP*:" + message;
		return encodedMessage;
	}


	public APRSAccount getTheAccount() {
		return theAccount;
	}


	public void setTheSocket(Socket theSocket) {
		this.theSocket = theSocket;
	}

}
