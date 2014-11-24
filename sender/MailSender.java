/**
 * 
 */
package org.aprs.messageit.sender;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.aprs.messageit.account.MailSenderAccount;
import org.aprs.messageit.packages.DataPackage;
import org.aprs.messageit.packages.EmailPackage;
import org.aprs.messageit.util.LogRecorder;
import org.aprs.messageit.util.MessageType;


/**
 * @author toshiba
 *
 */
public class MailSender extends BaseSender {
	private MailSenderAccount theAccount;
	/* (non-Javadoc)
	 * @see org.aprs.messageit.sender.BaseSender#init()
	 */
	@Override
	public boolean init() {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see org.aprs.messageit.sender.BaseSender#send(org.aprs.messageit.packages.DataPackage)
	 */
	@Override
	protected void send(DataPackage dataPackage) {
		try{
			EmailPackage thePackage = new EmailPackage(dataPackage);
			Properties mailProps = new Properties();
			mailProps.put("mail.smtp.host", theAccount.getHost());
			mailProps.put("mail.smtp.auth","true");
			Session mailSession = Session.getInstance(mailProps, theAccount);
			MimeMessage message = new MimeMessage(mailSession);
			message.setFrom(new InternetAddress(theAccount.getFromAddress()));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(thePackage.getRecevier(),false));
			message.setSubject(thePackage.getSubject());
			message.setText(thePackage.getContent());
			Transport.send(message);
		} catch (AddressException e){
			LogRecorder.ErrRecord(e.getMessage());
		} catch (MessagingException e){
			LogRecorder.ErrRecord(e.getMessage());
		}
	}

	/**
	 * @param theAccount
	 */
	public MailSender(int intervalTimeMs, MailSenderAccount theAccount) {
		super(intervalTimeMs, theAccount.getUsername());
		this.theAccount = theAccount;
		if(theAccount != null){
			startMessage = String.format("E-Mail Sending Start : %1$s @ %2$s", theAccount.getUsername(), theAccount.getHost());
		}
		allowedType = MessageType.EMAIL;
	}
	
	public void setPort(Object port){
		
	}

}
