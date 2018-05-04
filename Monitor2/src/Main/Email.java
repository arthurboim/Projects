package Main;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import AmazonOrders.Order_report;

public class Email {

	public String Send_report_mail(String Asin_for_order, Order_report Report, String Database_set_order_message, String Global_message)
	{        
	String from = "Arthur.boim@gmail.com";
    String pass = "b0104196";
    String[] to = { "Arthur.boim@gmail.com" }; // list of recipient email addresses
    String subject = Global_message;
    String body = "ASIN = "+Asin_for_order
    		+ "\nOrder on amazon message = "+Report.Message
    		+ "\nCode place = "+Report.Code_place+" of 15"
    		+ "\nBuyer details databae update = "+Database_set_order_message
    		+ "\nOrder info set to database message = "+Report.Database_update
    		+ "\nException = "+Report.ExceptionE
    		+ "\nPopUp Correction Address = "+Report.PopUp_Correction_Address
    		+"\n\n---------------------------------------------"
    		+ "\nBuyerName = "+Report.BuyerName
    		+ "\nBuyer Street1 = "+Report.Buyer_Street1
    		+ "\nBuyer Street2 = "+Report.Buyer_Street2
    		+ "\nCity Name = "+Report.Buyer_CityName
    		+ "\nState Or Province = "+Report.Buyer_StateOrProvince
    		+ "\nPostal Code = "+Report.Buyer_PostalCode
    		+ "\nBuyer Phone = "+Report.Buyer_Phone
    		+ "\nFba = "+Report.Fba
    		+"\n---------------------------------------------";
    	

    
    
    sendFromGMail(from, pass, to, subject, body);
    System.out.println("Email sent successfully");
	return "Email sent successfully";
	
	}
	private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
	
}
