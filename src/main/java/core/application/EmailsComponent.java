package core.application;

import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.resteasy.logging.impl.Log4jLogger;

import core.application.exception.ApplicationException;
import core.application.exception.ExceptionType;

@Stateless
public class EmailsComponent {

    private Log4jLogger log = new Log4jLogger(EmailsComponent.class.getCanonicalName());

    @EJB
    Config config;

    public void sendTemplate(String to) {
        String text = config.getValue("email_text");
        String subject = config.getValue("email_subject");
        if (text == null || subject == null) {
            throw new ApplicationException(ExceptionType.MISSING_EMAIL_CONFIG);
        }
        send(to, subject, text);
    }

    public void send(String to, String subject, String text) {
        String host = config.getValue("host");
        String from = config.getValue("email");
        String pass = config.getValue("pass");
        String port = config.getValue("port");
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        Transport transport = null;
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(text);
            transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (AddressException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(ExceptionType.EMAIL_NOT_SENT);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException(ExceptionType.EMAIL_NOT_SENT);
        } finally {
            try {
                transport.close();
            } catch (MessagingException e) {
                log.error("Error closing connection while sending email", e);
            }
        }
    }

}
