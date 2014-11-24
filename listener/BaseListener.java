/**
 *   By Chongz
 */
package org.aprs.messageit.listener;

import java.util.LinkedList;

import org.aprs.messageit.packages.DataPackage;
import org.aprs.messageit.util.ListenerCallback;
import org.aprs.messageit.util.LogRecorder;
import org.aprs.messageit.util.MessageType;


/**
 * @author toshiba
 *
 */
public abstract class BaseListener {

	private int intervalTime = 10;
	private ListenThread listeningThread;
	private ListenerCallback uploadCallback;
	protected String startMessage = new String("Listening Start");
	protected String name;
	
	/**
	 * @param intervalTime
	 * @param outputList
	 */
	protected BaseListener(int intervalTime, ListenerCallback uploadCallback, String name) {
		super();
		this.intervalTime = intervalTime;
		this.uploadCallback = uploadCallback;
		this.name = name;
	}
	
	public abstract boolean init();
	
	//Start listening in a new thread
	public void start() throws Exception{
		if(init()){
			LogRecorder.LogRecord(startMessage);
			listeningThread = new ListenThread();
			listeningThread.start();
		} else {
			throw new Exception("Unable To Start Listening.");
		}
	}
	
	public void close(){
		listeningThread.kill();
	}
	
	public void pause(int timeMs) {
		listeningThread.pause(timeMs);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void work() {
		listeningThread.work();
		//listeningThread.run();
	}

	public abstract void setPort(Object port);
	
	protected void uploadDataPackage(DataPackage p){
		synchronized (uploadCallback) {
			uploadCallback.uploadDataPackage(p);
		}
	}
	
	public abstract DataPackage[] getDataPackage();
	
	private class ListenThread extends Thread {
		private boolean isAlive = true;
		private boolean isPause = false;

		public void kill() {
			isAlive = false;
			isPause = true;
			interrupt();
		}
		
		public void pause(final int timeMs){
			//isAlive = false;
			isPause = true;
			Thread timer = new Thread(){
				public void run(){
					try {
						Thread.sleep(timeMs);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					work();
				}
			};
			timer.start();
		}
		
		public void work(){
			//isAlive = true;
			isPause = false;
		}
		
		public void run() {
			while(isAlive){
				if(!isPause){
					try{
						DataPackage[] dataPackage = getDataPackage();
						if(dataPackage != null){
							for(DataPackage thePackage : dataPackage){
								if(thePackage != null && thePackage.IsValid()){
									uploadDataPackage(thePackage);
									LogRecorder.LogRecord("Message Received : \n    " + thePackage.toString());
								}
							}
						}
						Thread.sleep(intervalTime);					
					} catch (InterruptedException e){
						LogRecorder.ErrRecord(e.getMessage());
					}
				}
			}
		}
	}
}
