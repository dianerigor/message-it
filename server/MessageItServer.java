/**
 *   By Chongz
 */
package org.aprs.messageit.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import org.aprs.messageit.account.APRSAccount;
import org.aprs.messageit.account.MailListenerAccount;
import org.aprs.messageit.account.MailSenderAccount;
import org.aprs.messageit.connector.APRSConnector;
import org.aprs.messageit.connector.BaseConnector;
import org.aprs.messageit.listener.APRSListener;
import org.aprs.messageit.listener.BaseListener;
import org.aprs.messageit.listener.MailListener;
import org.aprs.messageit.listener.SMSListener;
import org.aprs.messageit.packages.CommandPackage;
import org.aprs.messageit.packages.DataPackage;
import org.aprs.messageit.packages.EmailPackage;
import org.aprs.messageit.sender.APRSSender;
import org.aprs.messageit.sender.BaseSender;
import org.aprs.messageit.sender.MailSender;
import org.aprs.messageit.sender.SMSSender;
import org.aprs.messageit.util.CommandContainer;
import org.aprs.messageit.util.CommandType;
import org.aprs.messageit.util.ConnectorContainer;
import org.aprs.messageit.util.ListenerCallback;
import org.aprs.messageit.util.ListenerContainer;
import org.aprs.messageit.util.LogRecorder;
import org.aprs.messageit.util.MessageType;
import org.aprs.messageit.util.PackageContainer;
import org.aprs.messageit.util.SenderContainer;
import org.wengkai.gsm.SMSBox;


/**
 * @author toshiba
 *
 */
public class MessageItServer {

	static public String EMailManager = Messages.getString("Email-manager-callsign"); //$NON-NLS-1$
	static public int APRSTimeOut = Integer.parseInt(Messages.getString("APRS-timeout-ms")); //$NON-NLS-1$
	
	private ConnectorContainer connectorList = new ConnectorContainer();
	private ListenerContainer listenerList = new ListenerContainer();
	private SenderContainer senderList = new SenderContainer();
	
	private PackageContainer packageList = new PackageContainer();
	private CommandContainer commandList = new CommandContainer();
	
	private Dispatcher theDispatcher = new Dispatcher();
	private Executant theExecutant = new Executant();
	
	public void connectByLoginer(BaseConnector theConnector){
		theConnector.connect();
		boolean isOkey = theConnector.getLastConnectFlag();
		if(isOkey){
			BaseListener theListener = listenerList.findListener(theConnector.getName());
			if(theListener != null){
				theListener.setPort(theConnector.getPort());
				theListener.work();
			}
			BaseSender theSender = senderList.findSender(theConnector.getName());		
			if(theSender != null){
				theSender.setPort(theConnector.getPort());
				theSender.setReady(true);
			}
		}
	}
	
	public void addToPackageList(DataPackage thePackage){
		if(thePackage != null && thePackage.IsValid()){
			packageList.add(thePackage);
		}
	}
	
	private void init(){
	
		LogRecorder.SetOutputPath(Messages.getString("normallog-filename"), Messages.getString("errorlog-filename"), Messages.getString("messagelog-filename")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		LogRecorder.LogRecord("----------MessageIt Start----------"); //$NON-NLS-1$
		
		try {
			System.setErr(new PrintStream("erre.log")); //$NON-NLS-1$
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		APRSAccount ac1 = new APRSAccount(Messages.getString("APRS-server-host"), Integer.parseInt(Messages.getString("APRS-account-password")), Messages.getString("APRS-account-callsign"), Messages.getString("APRS-server-port")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		MailListenerAccount mlc1 = new MailListenerAccount(Messages.getString("Email-receive-account-username"), Messages.getString("Email-receive-account-password"), Messages.getString("Email-receive-server-host"), Messages.getString("Email-receive-server-prop")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		MailSenderAccount msc1 = new MailSenderAccount(Messages.getString("Email-send-account-username"), Messages.getString("Email-send-account-password"), Messages.getString("Email-send-account-host")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		APRSConnector connector1 = new APRSConnector(ac1);
		//loginer1.login();
		connectorList.add(connector1);
		
		ListenerCallback listenerCallback = new ListenerCallback(){
			public void uploadDataPackage(DataPackage thePackage){
				addToPackageList(thePackage);
			}
		};
		
		APRSListener al = new APRSListener(0, listenerCallback, ac1, null);
		listenerList.add(al);
		
		APRSSender as = new APRSSender(Integer.parseInt(Messages.getString("APRS-send-internal")), ac1, null); //$NON-NLS-1$
		senderList.add(as);
		
		MailListener ml = new MailListener(Integer.parseInt(Messages.getString("Email-listen-internal")), listenerCallback, mlc1); //$NON-NLS-1$
		listenerList.add(ml);
		
		MailSender ms = new MailSender(Integer.parseInt(Messages.getString("Email-send-internal")), msc1); //$NON-NLS-1$
		senderList.add(ms);
		
		String smsPortName = Messages.getString("SMS-port"); //$NON-NLS-1$
		String smsBaud = Messages.getString("SMS-baud"); //$NON-NLS-1$
		SMSBox theBox = SMSBox.getSMSBox();
		try {
			theBox.open(smsPortName, Integer.parseInt(smsBaud));
			
			SMSListener sl = new SMSListener(listenerCallback, theBox, smsPortName);
			listenerList.add(sl);
			
			SMSSender ss = new SMSSender(theBox, smsPortName);
			senderList.add(ss);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//LogRecorder.LogRecord("GSM Box Inited.");

		
	}
	
	public void start() throws Exception{
		init();
		theDispatcher.start();
		theExecutant.start();
		for(BaseListener bl : listenerList){
			try {
				bl.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(BaseSender bs : senderList){
			try {
				bs.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args){
		MessageItServer theServer = new MessageItServer();
		try {
			theServer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true){
			String aline = System.console().readLine();
		}
	}
	
	private class Dispatcher{
		private int intervalTimeMs = 100;
		private DispatchThread dispatchingThread;
		public void start(){
			LogRecorder.LogRecord("Dispatching Start"); //$NON-NLS-1$
			dispatchingThread = new DispatchThread();
			dispatchingThread.start();
		}
		private void addToSender(DataPackage thePackage){
			for(BaseSender s : senderList){
				if(s.isReady()){
					s.add(thePackage);
				}
			}
		}
		private void addToExecutant(DataPackage thePackage){
			commandList.add(new CommandPackage(thePackage));
		}
		private void tryAck(DataPackage thePackage){
			if(thePackage != null && thePackage.IsValid() && thePackage.getMID() != -1){
				DataPackage ackPackage = new DataPackage();
				ackPackage.setMessageType(MessageType.COMMAND);
				ackPackage.setCommandType(CommandType.ACK);
				ackPackage.setContent(String.format("%1$d,%2$s,%3$s", thePackage.getMID(), thePackage.getSender(), "MSGT")); //$NON-NLS-1$ //$NON-NLS-2$
				addToSender(ackPackage);
			}
		}
		private class DispatchThread extends Thread {
			private boolean isAlive = true;
			public void kill(){
				isAlive = false;
			}
			public void run(){
				while(isAlive){
					try{
						if(!packageList.isEmpty()){
							DataPackage d = packageList.pop();
							if(d != null && d.IsValid()){
								switch(d.getMessageType()){
									case SMS:
									case EMAIL:
									case APRS:
										tryAck(d);
										addToSender(d);
										break;
									case COMMAND:
										addToExecutant(d);
										break;
								}
							}
						}
						Thread.sleep(intervalTimeMs);
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}

	}
	
	private class Executant{
		private int intervalTimeMs = 100;
		private ExecutantThread ExecutingThread;
		
		public void start(){
			LogRecorder.LogRecord("Executing Start"); //$NON-NLS-1$
			ExecutingThread = new ExecutantThread();
			ExecutingThread.start();
		}
		
		private int deleteUndispatchedPackage(String para){
			int count = 0;
			String [] paras = para.split(","); //$NON-NLS-1$
			int tMID = Integer.parseInt(paras[0]);
			String sender = paras[1];
			for(DataPackage thePackage : packageList){
				if(thePackage.getMID() == tMID && thePackage.getSender() == sender){
					LogRecorder.LogRecord(String.format("Package Removed : \n    %1$s", thePackage.toString())); //$NON-NLS-1$
					packageList.remove(thePackage);
					count++;
				}
			}
			return count;
		}
		
		private void ack(String para){
			String [] paras = para.split(","); //$NON-NLS-1$
			DataPackage ackPackage = new DataPackage();
			ackPackage.setMessageType(MessageType.APRS);
			ackPackage.setMID(-1);
			ackPackage.setContent(String.format("ack%1$03d", Integer.parseInt(paras[0]))); //$NON-NLS-1$
			ackPackage.setReceiver(paras[1]);
			ackPackage.setSender(paras[2]);
			addToPackageList(ackPackage);
		}
		
		private void rej(String para){
			String [] paras = para.split(","); //$NON-NLS-1$
			DataPackage ackPackage = new DataPackage();
			ackPackage.setMessageType(MessageType.APRS);
			ackPackage.setMID(-1);
			ackPackage.setContent(String.format("rej%1$03d", Integer.parseInt(paras[0]))); //$NON-NLS-1$
			ackPackage.setReceiver(paras[1]);
			ackPackage.setSender(paras[2]);
			addToPackageList(ackPackage);
		}
		
		private void login(String name){
			for(BaseConnector connector: connectorList){
				if(name == connector.getName()){
					connectByLoginer(connector);
					break;
				}
			}
		}
		
		private class ExecutantThread extends Thread {
			private boolean isAlive = true;
			public void kill(){
				isAlive = false;
			}
			public void run(){
				while(isAlive){
					try{
						if(!commandList.isEmpty()){
							CommandPackage d = commandList.pop();
							switch(d.getCommandType()){
								case DELETE:
									deleteUndispatchedPackage(d.getParameter());
									break;
								case ACK:
									ack(d.getParameter());
									break;
								case REJ:
									rej(d.getParameter());
									break;
								case LOGIN:
									login(d.getParameter());
									break;
							}
						}
						Thread.sleep(intervalTimeMs);
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}

	}

	
}
