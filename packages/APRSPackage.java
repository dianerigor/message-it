/**
 * 
 */
package org.aprs.messageit.packages;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aprs.messageit.util.CommandType;
import org.aprs.messageit.util.LogRecorder;
import org.aprs.messageit.util.MessageType;
import org.aprs.messageit.util.StringType;


/**
 * @author toshiba
 *
 */
public class APRSPackage extends TransferablePackage{
	private String message;
	
	public APRSPackage(DataPackage thePackage){
		super();
		parsePackage(thePackage);
	}
	
	public void parsePackage(DataPackage dataPackage){
		try{	
			if(!dataPackage.IsValid())
				throw (new Exception("Invalid Data Package."));
			this.message = String.format("%1$s>%2$s::%3$s:%4$s{%5$03d", dataPackage.getSender(), 
					dataPackage.getGateway(), dataPackage.getReceiver(), 
					dataPackage.getContent(), dataPackage.getMID());
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public DataPackage toDataPackage(){
		try{
			DataPackage dataPackage = new DataPackage();
			if(message == null){
				throw (new IOException("Message Is Null."));
			}
			
			if(message.startsWith("#")){
				LogRecorder.LogRecord(String.format("Receive Message From Server.\n    %1$s", message));
				dataPackage.setMessageType(MessageType.SYSTEM);
				dataPackage.setContent(message.substring(1));	//Skip "#"
				return dataPackage;
				//throw(new IOException(String.format("Message Start With #, Ignored.\n    %1$s", message)));
			}
			
			//format check
			Pattern p = Pattern.compile("(.+)(>)(.+)(:)(:)(.+)(:)(.+)( )(.+)(\\{)([0-9]+)");
			Matcher m;
			
			m = p.matcher(message);
			if(!(m.matches())){
				throw(new IOException(String.format("Message Does Not Match The Format.\n    %1$s" + message)));
			}
			
			//scan from String
			Scanner sc = new Scanner(message);
			
			//part 1
			sc.useDelimiter(">");
			dataPackage.setSender(sc.next());
			sc.skip(">");
			
			//part 2
			sc.useDelimiter("::");
			dataPackage.setGateway(sc.next());
			sc.skip("::");
			
			//part 3
			sc.useDelimiter(":");
			dataPackage.setMessageType(MessageType.valueOf(sc.next()));
			sc.skip(":");
			
			//part 4
			sc.useDelimiter(" ");
			String tempPara = sc.next();
			sc.skip(" ");
			switch(dataPackage.getMessageType()){
				case EMAIL:
					switch(StringType.getStringType(tempPara)){
						case EMAILADDRESS:
							dataPackage.setReceiver(tempPara);
							dataPackage.setCommandType(CommandType.SEND);
							break;
						case COMMAND:
							dataPackage.setCommandType(CommandType.valueOf(tempPara));;
							break;
						case ABBR:
							//toAddress = MessageIt.userData.getEmailAddress(tempPara);
							break;
						default:
							throw(new IOException("Unknown Command Type."));
					}
					break;
				case SMS:
					break;
				default:
					throw(new IOException("Unknown Message Type."));
			}
			
			//part 5
			sc.useDelimiter("\\{");
			dataPackage.setContent(sc.next());
			sc.skip("\\{");
			
			//part 6
			sc.useDelimiter("");
			dataPackage.setMID(sc.nextInt());
	
			sc.close();
			
			LogRecorder.LogRecord(String.format("Message From %1$s Packaged Successfully.\n    %2$s" , dataPackage.getSender(), message));
			return dataPackage;
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
