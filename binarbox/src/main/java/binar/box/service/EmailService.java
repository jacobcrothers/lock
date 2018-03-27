package binar.box.service;

import binar.box.configuration.EmailConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Created by Timis Nicu Alexandru on 27-Mar-18.
 */
@Service
public class EmailService {

    @Autowired
    private EmailConfiguration emailConfiguration;

    @Autowired
    private Environment environment;


    public void sendRequestResetPasswordEmail(String userEmail, String resetPasswordToken) {
        String from = environment.getProperty("mail.from");
        String subject = environment.getProperty("mail.subject");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(resetPasswordToken);
        simpleMailMessage.setTo(userEmail);
        emailConfiguration.javaMailSender().send(simpleMailMessage);
    }
}
