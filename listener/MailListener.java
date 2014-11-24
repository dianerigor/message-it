/**
 * 
 */
package org.aprs.messageit.listener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import org.aprs.messageit.account.APRSAccount;
import org.aprs.messageit.account.MailListenerAccount;
import org.aprs.messageit.account.UserAccount;
import org.aprs.messageit.packages.DataPackage;
import org.aprs.messageit.util.CommandType;
import org.aprs.messageit.util.ListenerCallback;
import org.aprs.messageit.util.LogRecorder;
import org.aprs.messageit.util.MessageType;


/**
 * @author toshiba
 *
 */
public class MailListener extends BaseListener {

	MailListenerAccount theAccount;
	
	

	/**
	 * @param intervalTime
	 * @param uploadCallback
	 */
	public MailListener(int intervalTime, ListenerCallback uploadCallback, 
						MailListenerAccount theAccount) {
		super(intervalTime, uploadCallback, theAccount.getUsername());
		this.theAccount = theAccount;
		if(theAccount != null){
			startMessage = String.format("E-Mail Listening Start : %1$s @ %2$s", theAccount.getUsername(), theAccount.getHost());
		}
	}

	
	public void setPort(Object port){
		
	}

	/* (non-Javadoc)
	 * @see org.aprs.messageit.listener.BaseListener#getDataPackage()
	 */
	@Override
	public DataPackage[] getDataPackage() {
		Store store = null;
		Folder folder = null;
		LinkedList<DataPackage> re = new LinkedList<DataPackage>();
		try {
			Properties props = System.getProperties();
			props.setProperty("mail.store.protocol", theAccount.getProtocol());
			props.setProperty("mail.imap.host", theAccount.getHost()); 

			Session session = Session.getInstance(props);

			store = session.getStore(theAccount.getProtocol());
			store.connect(theAccount.getHost(), theAccount.getUsername(), theAccount.getPassword());

			folder = store.getDefaultFolder();	//get root folder of the mailbox
			if (folder == null) {
				throw new Exception("No Default Folder");
			}
			folder = folder.getFolder("INBOX"); {
				if (folder == null) throw new Exception("No IMAP INBOX");
			}
			
			folder.open(Folder.READ_WRITE);  //will set SEEN flag automaticly

			int unreadCount = folder.getUnreadMessageCount();
			Message[] msgs = folder.getMessages();	//get all messages
			int msgsCount = msgs.length;
			Message[] unreadMessages = new Message[unreadCount];
			
			//get unread messages
			for(int i = 0; i < msgsCount && unreadCount > 0; i++){
				if(!msgs[i].getFlags().contains(Flags.Flag.SEEN)){
					unreadCount--;
					unreadMessages[unreadCount] = msgs[i];
				}
			}
			
			for(Message currentMessage : unreadMessages){	//check every unread message
				//currentMessage.setFlag(Flags.Flag.SEEN, true);
				DataPackage thePackage = new DataPackage();
				thePackage.setMessageType(MessageType.APRS);
				thePackage.setCommandType(CommandType.SEND);
				thePackage.setReceiver(currentMessage.getSubject());
				String[] con = decodeMessage(currentMessage);
				/*if(con.length < 2){
					//currentMessage.setFlag(Flags.Flag.DELETED, true);
					//currentMessage.saveChanges();
					continue;
				}*/
				thePackage.setContent(con[0]);
				int t = con[con.length-1].indexOf("DE:");
				String sender;
				if(t == -1){
					String add = currentMessage.getFrom()[0].toString();
					int t1, t2;
					t1 = add.indexOf("<");
					t2 = add.indexOf(">");
					String from = add.substring(t1+1, t2);
					
					sender = UserAccount.findCallsign("Email-address", from);
				} else {
					sender = con[con.length-1].substring(t + "DE:".length());
				}
				if(sender.length() > 9){
					sender = sender.substring(0, 8);
				}
				thePackage.setSender(sender);
				thePackage.setGateway(this.theAccount.getHost());
				//currentMessage.setFlag(Flags.Flag.ANSWERED, true);
				//currentMessage.saveChanges();
				re.add(thePackage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			/*try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		} finally {
			try{
				if (folder != null) folder.close(true);	//close folder
				if (store != null) store.close();			//close store
			}catch (Exception ex2) {
				ex2.printStackTrace();
			}
		}		
		if(!re.isEmpty()){
			DataPackage[] d = new DataPackage[re.size()];
			return re.toArray(d);
		} else return null;
	}
	
	
	/**
	 * decode the given email message to a MSG message
	 * @param message message to be decoded
	 * @return decoded message if valid, null otherwise
	 */
	private static String decodeString(String s){
		String decodedString = null;
		
		decodedString = s;
		return decodedString;
	}
	private static String[] decodeMessage(Message message){
		try{
			Part messagePart = message;	//treat message as abstract Part
			Object content = messagePart.getContent();
			if (content instanceof Multipart){
				messagePart = ((Multipart)content).getBodyPart(0);	//for multipart message, get firt part
				//System.out.println("[ Multipart Message ]");
			}
			
			String contentType = messagePart.getContentType();
			
			if (contentType.startsWith("text/plain")){	//only service plain text messages
				InputStream is = messagePart.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				ArrayList<String> contentLines = new ArrayList<String>();  //save non-empty lines in the message
				ArrayList<String> decodedMessage = new ArrayList<String>();
				String thisLine = reader.readLine();
				String tempMessage = null;
				while (thisLine != null){
					if(!thisLine.trim().equals("")){  //remove the blanks at both ends
						contentLines.add(thisLine);
					}
					thisLine = reader.readLine();
				}
				for(String s : contentLines){
					tempMessage = decodeString(s);
					if(tempMessage != null)
						decodedMessage.add(decodeString(s));
				}
				String[] se = new String[decodedMessage.size()];
				decodedMessage.toArray(se);
				return se;
			}
		}catch (Exception e){
			LogRecorder.ErrRecord(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see org.aprs.messageit.listener.BaseListener#init()
	 */
	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		return true;
	}

}
