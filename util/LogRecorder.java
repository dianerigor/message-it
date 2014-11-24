package org.aprs.messageit.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class LogRecorder {
	
	private static String logOutputPath = null;
	private static String errOutputPath = null;
	private static String msgOutputPath = null;
	
	public static void SetOutputPath(String lop, String eop, String mop){
		logOutputPath = lop;
		errOutputPath = eop;
		msgOutputPath = mop;
	}
	
	public static void LogRecord(String msg){
		try {
			PrintWriter logOutputWriter = new PrintWriter(new FileOutputStream(logOutputPath, true));
			logOutputWriter.println(String.format("%1$s : %2$s", new Date().toString(), msg));
			logOutputWriter.close();
		} catch (FileNotFoundException e) {
			if(logOutputPath != null)
				System.out.println(e.getMessage());
		}
		System.out.println(String.format("%1$s : %2$s", new Date().toString(), msg));
	}
	
	public static void ErrRecord(String msg){
		try {
			PrintWriter errOutputWriter = new PrintWriter(new FileOutputStream(errOutputPath, true));
			errOutputWriter.println(String.format("%1$s : %2$s", new Date().toString(), msg));
			errOutputWriter.close();
		} catch (FileNotFoundException e) {
			if(errOutputPath != null)
				System.out.println(e.getMessage());
		}
		System.out.println(String.format("%1$s : %2$s", new Date().toString(), msg));
	}
	
	public static void MsgRecord(String msg){
		try {
			PrintWriter msgOutputWriter = new PrintWriter(new FileOutputStream(msgOutputPath, true));
			msgOutputWriter.println(String.format("%1$s : %2$s", new Date().toString(), msg));
			msgOutputWriter.close();
		} catch (FileNotFoundException e) {
			if(msgOutputPath != null)
				System.out.println(e.getMessage());
		}
		System.out.println(msg);
	}
}
