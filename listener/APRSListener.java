/**
 * 
 */
package org.aprs.messageit.listener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aprs.messageit.account.APRSAccount;
import org.aprs.messageit.packages.CommandPackage;
import org.aprs.messageit.packages.DataPackage;
import org.aprs.messageit.server.MessageItServer;
import org.aprs.messageit.util.CommandType;
import org.aprs.messageit.util.ListenerCallback;
import org.aprs.messageit.util.LogRecorder;
import org.aprs.messageit.util.MessageType;



/**
 * @author toshiba
 *
 */
public class APRSListener extends BaseListener {
	private APRSAccount theAccount;
	private Socket theSocket;

	/**
	 * @param intervalTime
	 * @param uploadCallback
	 */
	public APRSListener(int intervalTime, ListenerCallback uploadCallback, 
						APRSAccount theAccount, Socket theSocket) {
		super(intervalTime, uploadCallback, theAccount.getUsername());
		this.theAccount = theAccount;
		this.theSocket = theSocket;
		if(theAccount != null){
			startMessage = String.format("APRS Listening Start : %1$s @ %2$s", theAccount.getUsername(), theAccount.getHost());
		}
	}


	public APRSAccount getTheAccount() {
		return theAccount;
	}


	public void setTheSocket(Socket theSocket) {
		this.theSocket = theSocket;
	}
	
	public void setPort(Object port){
		this.theSocket = (Socket) port;
	}
	
	public String readln() throws IOException {
		String s = new String("");
		if(theSocket == null || !theSocket.isConnected())
			throw new IOException();
		BufferedReader inReader = new BufferedReader(new InputStreamReader(theSocket.getInputStream()));
		s = inReader.readLine();
		//inReader.close();
		return s;
	}

	/* (non-Javadoc)
	 * @see org.aprs.messageit.listener.BaseListener#getDataPackage()
	 */
	@Override
	public DataPackage[] getDataPackage() {
		DataPackage[] thePackage = new DataPackage[1];
		try {
			thePackage[0] = StringToPackage(readln());
		} catch (IOException e) {//| SocketTimeoutException e) {
			// TODO Auto-generated catch block
			thePackage[0] = (new CommandPackage(CommandType.LOGIN, getName(), getName())).toDataPackage();
			e.printStackTrace();
			pause(2 * MessageItServer.APRSTimeOut);
		}
		return thePackage;
	}
	
	public static DataPackage StringToPackage(String message){
		DataPackage re = new DataPackage();
		try{
			if(message == null){
				throw(new IOException("Message Is Null."));
			}
			
			if(message.startsWith("#")){
				LogRecorder.LogRecord("Receive Message From Server.\n    " + message);
				throw(new IOException("Message Start With #, Ignored.\n    " + message));
			}
	
			//format check
			//Pattern p1 = Pattern.compile("(.+)>(.+)::(.+):(.+) (.+)");
			Pattern p2 = Pattern.compile("(.+)>(.+)::(.+):(.+) (.+)\\{([0-9]+)");
			Pattern ackPattern = Pattern.compile("(.+)>(.+)::(.+):ack([0-9]+)");
			Matcher ackMatcher = ackPattern.matcher(message);
			Matcher /*m1, */m2;
			
			if(ackMatcher.matches()){
				re.setMessageType(MessageType.COMMAND);
				re.setCommandType(CommandType.DELETE);
				re.setSender(ackMatcher.group(1));
				re.setContent(String.format("%1$d,%2$s", ackMatcher.group(4), ackMatcher.group(3)));
			} else {
				
				//m1 = p1.matcher(message);
				if(message.indexOf("{") == -1) {//m1.matches()){
					message = message + "{-1";
				}
				m2 = p2.matcher(message);
				if(!m2.matches()){
					throw(new IOException("Message Does Not Match The Format.\n    " + message));
				}
	
				re.setSender(m2.group(1));
				re.setGateway(m2.group(2));
				re.setMessageType(MessageType.fromString((m2.group(3))));
				if(re.getMessageType() == null){
					throw new Exception("Unknown Message Type.");
				}
				re.setReceiver(m2.group(4));
				re.setContent(m2.group(5));
				re.setMID(Integer.parseInt(m2.group(6)));
			}
			//LogRecorder.LogRecord(String.format("Message From %1$s Packaged Successfully.\n    %2$s", re.getSender(), message));

		} catch (Exception ex) {
			//LogRecorder.ErrRecord(ex.getMessage());
			return null;
		}
		return re;
	}


	/* (non-Javadoc)
	 * @see org.aprs.messageit.listener.BaseListener#init()
	 */
	@Override
	public boolean init() {
		return true;
	}

}
