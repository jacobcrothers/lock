package binar.box.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import binar.box.configuration.EmailConfiguration;

@Service
public class EmailService {

	private final EmailConfiguration emailConfiguration;

	private final Environment environment;

	@Autowired
	public EmailService(EmailConfiguration emailConfiguration, Environment environment) {
		this.emailConfiguration = emailConfiguration;
		this.environment = environment;
	}

	public void sendEmail(String userEmail, String subject, String text) {
		String from = environment.getProperty("mail.from");
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(from);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);
		simpleMailMessage.setTo(userEmail);
		emailConfiguration.javaMailSender().send(simpleMailMessage);
	}
}
