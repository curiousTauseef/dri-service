package randomtests;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailTest {

	public static void main(String[] args) {
		String from = "dri@vph.cyfronet.pl";
		String to = "kstyrc@gmail.com";
		String subject = "DRI mail test";
		String text = "DRI mail test";
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "kinga.cyf-kr.edu.pl");
		//props.put("mail.smtp.port", "465");
		
		Session mailSession = Session.getDefaultInstance(props, null);
		Message message = new MimeMessage(mailSession);
		
		InternetAddress fromAddress = null;
		InternetAddress toAddress = null;
		
		try {
			fromAddress = new InternetAddress(from);
			toAddress = new InternetAddress(to);
		
		} catch (AddressException e) {
			e.printStackTrace();
		}
		
		try {
			message.setFrom(fromAddress);
			message.setRecipient(RecipientType.TO, toAddress);
			message.setSubject(subject);
			message.setText(text);
			
			System.out.print("Sending...");
			Transport.send(message);
			System.out.println("Sent!");
		
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
