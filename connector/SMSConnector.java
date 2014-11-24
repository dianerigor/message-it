/**
 *   By Chongz
 */
package org.aprs.messageit.connector;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.aprs.messageit.util.LogRecorder;

/**
 * @author toshiba
 *
 */
public class SMSConnector extends BaseConnector {
	private static final int TIME_OUT = 120000;		//	in ms
	private static final String APP_NAME = "MSGT";
	//private static final String URC_SMS = "+CMT:";
	
	private SerialPort port;
	private int baud;
	

	private PrintWriter outWriter;
	private BufferedReader inReader;
	
	/**
	 * @param port
	 * @param baud
	 */
	public SMSConnector(String name, int baud) {
		super(name);
		this.baud = baud;
	}

	/* (non-Javadoc)
	 * @see org.aprs.messageit.connector.BaseConnector#connect()
	 */
	@Override
	public void connect() {
		String portName = getName();
		
		try {
			CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(portName);
			port = (SerialPort) cpi.open(APP_NAME, TIME_OUT);
		
			port.setSerialPortParams(baud,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			inReader = new BufferedReader(new InputStreamReader(port.getInputStream()));
			outWriter = new PrintWriter(new OutputStreamWriter(port.getOutputStream()), true);
			config("AT");
			config("ATZ");
			config("ATE0");
			config("AT+CMGF=0");
			//config("AT+CSMS=0");
			//config("AT+CNMI=2,2,,1");
			//port.addEventListener(new SPL());
			//port.notifyOnDataAvailable(true);
			//theSender.start();
			lastConnectFlag = true;
		} catch (Exception e) {
			e.printStackTrace();
			port.close();
			//throw e;
		}
	}
	
	private void config(String cmd) throws IOException {
		while ( command(cmd) == false )
			;
	}
	
	private boolean command(String cmd) throws IOException {
		boolean ret = false;
		outWriter.println(cmd+"\r\n");
		LogRecorder.LogRecord(cmd);
		ret = true;
		while (true) {
			String rep = inReader.readLine();
			LogRecorder.LogRecord(rep);
			if ( rep.equals("OK") ) {
				ret = true;
				break;
			} else if ( rep.equals("ERROR") ) {
				ret = false;
				break;
			}/* else if ( rep.startsWith(URC_SMS) ) {
				dealSMS();
			}*/
		}
		return ret;
	}

	public Object getPort() {
		return port;
	}

}
