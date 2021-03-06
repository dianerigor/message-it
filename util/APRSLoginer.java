/**
 * 
 */
package org.aprs.messageit.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import org.aprs.messageit.account.APRSAccount;
import org.aprs.messageit.server.MessageItServer;

/**
 * @author toshiba
 *
 */
public class APRSLoginer {
	private APRSAccount theAccount;
	private Socket theSocket;
	private boolean lastLoginFlag = false;
	/**
	 * @param theAccount
	 */
	public APRSLoginer(APRSAccount theAccount) {
		super();
		this.theAccount = theAccount;
	}
	
	public APRSAccount getTheAccount() {
		return theAccount;
	}

	public boolean getLastLoginFlag(){
		return lastLoginFlag;
	}
	
	public boolean login(){
		lastLoginFlag = true;
		PrintWriter outWriter = null;
		BufferedReader inReader = null;
		try {
			int timeOut = MessageItServer.APRSTimeOut;
			theSocket = new Socket();
			theSocket.connect(new InetSocketAddress(theAccount.getHost(), theAccount.getPort()), timeOut);
			theSocket.setSoTimeout(timeOut);
			outWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(theSocket.getOutputStream())), true);  
			inReader = new BufferedReader(new InputStreamReader(theSocket.getInputStream()));
			if(!inReader.readLine().startsWith(String.format("# javAPRSSrvr"))){
				theSocket.close();
				throw new Exception("Unable Connect To APRS Server.");
			}
			String loginText = String.format("user %1$s pass %2$s vers Chatter_t515 1.0", theAccount.getUsername(), theAccount.getPassword());
			outWriter.println(loginText);
			if(!inReader.readLine().startsWith(String.format("# logresp %1$s verified", theAccount.getUsername()))){
				theSocket.close();
				throw new Exception("Unable To Verify Call Sign/Password.");
			}
			outWriter.println("#filter t/m");
			LogRecorder.LogRecord(String.format("APRS Login : %1$s @ %2$s", theAccount.getUsername(), theAccount.getHost()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			lastLoginFlag = false;
		}
		return lastLoginFlag;
	}

	public Socket getTheSocket() {
		return theSocket;
	}
	
}
