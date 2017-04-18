package net.contargo.iris.config;

import net.contargo.iris.mail.service.DummyEmailService;
import net.contargo.iris.mail.service.SpringEmailService;
import net.contargo.iris.mail.service.TemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Configuration
public class EmailConfig {

    private static final String MAIL_SERVICE_PROPERTY = "mail.service";
    private static final String MAIL_SERVICE_BEAN = "mailService";

    @Bean
    @Autowired
    @ConditionalOnProperty(name = MAIL_SERVICE_PROPERTY, havingValue = "realEmailService")
    public JavaMailSender mailSender(@Value("${mail.host}") String host,
        @Value("${mail.port}") String port,
        @Value("${mail.username}") String username,
        @Value("${mail.password}") String password) {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(Integer.valueOf(port));
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        return javaMailSender;
    }


    @Bean(name = MAIL_SERVICE_BEAN)
    @Autowired
    @ConditionalOnProperty(name = MAIL_SERVICE_PROPERTY, havingValue = "realEmailService")
    public SpringEmailService realEmailService(TemplateService templateService, JavaMailSender mailSender,
        @Value("${mail.from}") String senderAddress) {

        return new SpringEmailService(templateService, mailSender, senderAddress);
    }


    @Bean(name = MAIL_SERVICE_BEAN)
    @Autowired
    @ConditionalOnProperty(name = MAIL_SERVICE_PROPERTY, havingValue = "dummyEmailService", matchIfMissing = true)
    public DummyEmailService dummyEmailService(TemplateService templateService,
        @Value("${mail.from}") String senderAddress) {

        return new DummyEmailService(templateService, senderAddress);
    }
}
