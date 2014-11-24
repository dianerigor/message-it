/**
 *   By Chongz
 */
package org.aprs.messageit.account;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.aprs.messageit.util.StringType;

/**
 * @author toshiba
 * 
 */
public class UserAccount {
	private String callsign = null;
	private String emailAddress = null;
	private String smsNumber = null;
	private LinkedList<String> whiteList = new LinkedList<String>();
	
	public String getCallsign() {
		return callsign;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public String getSmsNumber() {
		return smsNumber;
	}
	public boolean isInWhiteList(String number){
		return whiteList.contains(number.trim());
	}
	
	public static String findCallsign(String type, String value){
		String cal = null;
		File accountFlorder = new File("account");
		String [] files = accountFlorder.list();
		for(String s : files){
			if(checkAccount(s, type, value)){
				int a = ".dat".length();
				cal = s.substring(0, s.length() - a);
				break;
			}
		}
		return cal;
	}
	
	private static boolean checkAccount(String fileName, String type, String value){
		boolean ifFind = false;
		try {
			BufferedReader inReader = new BufferedReader(new FileReader("account\\" + fileName));
			String aline;
			while((aline = inReader.readLine()) != null){
				String sss = String.format("%1$s=%2$s", type.trim(), value.trim());
				if(aline.trim().equals(sss)){
					ifFind = true;
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			ifFind = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return ifFind;
	}
	
	public boolean loadFromFile(String name){
		boolean ret = true;
		String path = String.format("account\\%1$s.dat", name);
		this.whiteList.clear();
		this.smsNumber = null;
		this.emailAddress = null;
		this.callsign = null;
		
		try {
			BufferedReader inReader = new BufferedReader(new FileReader(path));
			this.callsign = name;
			String aline;
			while((aline = inReader.readLine()) != null){
				String [] para = aline.split("=");
				switch(para[0]){
					case "SMS-number":
						if(StringType.getStringType(para[1]) == StringType.NUMBER){
							this.smsNumber = para[1];
						}
						break;
					case "Email-address":
						if(StringType.getStringType(para[1]) == StringType.EMAILADDRESS){
							this.emailAddress = para[1];
						}
						break;
					case "white-list":
						if(StringType.getStringType(para[1]) == StringType.NUMBER){
							this.whiteList.add(para[1]);
						}
						break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			callsign = null;
			ret = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return ret;		
	}
}
