/**
 *   By Chongz
 */
package org.aprs.messageit.sender;

import java.util.LinkedList;

import org.aprs.messageit.packages.DataPackage;
import org.aprs.messageit.util.LogRecorder;
import org.aprs.messageit.util.MessageType;
import org.aprs.messageit.util.PackageContainer;

/**
 * @author toshiba
 *
 */
public abstract class BaseSender {
	private int intervalTimeMs;
	private PackageContainer sendList = new PackageContainer();
	private ServerThread sendingThread;
	protected String startMessage = new String("Sending Start");
	protected MessageType allowedType;
	protected String name;
	private boolean isReady = false;
	
	/**
	 * @param intervalTimeMs
	 */
	public BaseSender(int intervalTimeMs, String name) {
		super();
		this.intervalTimeMs = intervalTimeMs;
		this.name = name;
	}

	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract boolean init();
	
	//Start sending in a new thread
	public void start() throws Exception{
		if(init()){
			LogRecorder.LogRecord(startMessage);
			sendingThread = new ServerThread();
			sendingThread.start();
			setReady(true);
		} else {
			setReady(false);
			throw new Exception("Unable To Start Sending.");
		}
	}

	public void close(){
		if(sendingThread != null){
			sendingThread.kill();
		}
	}
	
	public void add(DataPackage dataPackage){
		if(dataPackage.IsValid() && dataPackage.getMessageType() == allowedType){
			sendList.add(dataPackage);
		}
	}
	
	protected abstract void send(DataPackage dataPackage);
	public abstract void setPort(Object port);
	
	private class ServerThread extends Thread {
		private boolean isAlive = true;

		public void kill() {
			isAlive = false;
			interrupt();
		}

		public void run() {
			while(isAlive){
				try{
					if(!sendList.isEmpty()){
						DataPackage thePackage = sendList.pop();
						if(thePackage != null && thePackage.IsValid()){
							send(thePackage);
							LogRecorder.LogRecord("Message Sent : \n    " + thePackage.toString());
						}
					}			
					Thread.sleep(intervalTimeMs);
				} catch (Exception e){
					LogRecorder.ErrRecord(e.getMessage());
				}
			}
		}
	}
}
