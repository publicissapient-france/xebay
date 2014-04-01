package fr.xebia.xebay.domain;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    public void sendKey(String email, String key) {

        Properties properties = System.getProperties();
        String username = properties.getProperty("mail.username");
        String password = properties.getProperty("mail.password");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Xebay API Key");
            message.setText(String.format("Dear Xebian,\n\nHere is your API Key: %s\n\nUse it in peace.", key));
            Transport.send(message);

        } catch (MessagingException e) {
            throw new BidException(e.getMessage());
        }
    }
}
