/**
 * 
 */
package org.aprs.messageit.connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.aprs.messageit.account.APRSAccount;
import org.aprs.messageit.server.MessageItServer;
import org.aprs.messageit.util.LogRecorder;

/**
 * @author toshiba
 *
 */
public class APRSConnector extends BaseConnector {
	private APRSAccount theAccount;
	private Socket theSocket;
	
	
	
	/**
	 * @param theAccount
	 */
	public APRSConnector(APRSAccount theAccount) {
		super(theAccount.getUsername());
		this.theAccount = theAccount;
	}

	public Object getPort(){
		return theSocket;
	}
	
	/* (non-Javadoc)
	 * @see org.aprs.messageit.connector.BaseConnector#connect()
	 */
	@Override
	public void connect() {
		lastConnectFlag = true;
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
			lastConnectFlag = false;
		}
	}

	public Socket getTheSocket() {
		return theSocket;
	}
}
