package binar.box.configuration;

import binar.box.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * Created by Timis Nicu Alexandru on 27-Mar-18.
 */
@Configuration
public class EmailConfiguration {

    @Resource
    private Environment environment;


    @Bean
    public JavaMailSenderImpl javaMailSender() {
        String starttls = environment.getProperty("mail.starttls");
        String host = environment.getProperty("mail.host");
        String port = environment.getProperty("mail.port");
        String username = environment.getProperty("mail.username");
        String password = environment.getProperty("mail.password");
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();
        mailProperties.put(Constants.MAIL_SMTP_STARTTLS_ENABLE, starttls);
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(host);
        mailSender.setPort(Integer.valueOf(port));
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        return mailSender;
    }

}