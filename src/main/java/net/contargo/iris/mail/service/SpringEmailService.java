package net.contargo.iris.mail.service;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ByteArrayResource;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.io.InputStream;

import java.lang.invoke.MethodHandles;

import java.util.Map;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class SpringEmailService implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TemplateService templateService;
    private final JavaMailSender mailSender;
    private final String from;

    public SpringEmailService(TemplateService templateService, JavaMailSender mailSender, String from) {

        this.templateService = templateService;
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void send(String to, String subject, String templateName, Map<String, String> data) {

        LOG.debug("Sending email from {} to {} with subject {}", from, to, subject);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setFrom(from);
        mailMessage.setSubject(subject);
        mailMessage.setText(templateService.createTemplate(templateName, data));

        mailSender.send(mailMessage);
    }


    @Override
    public void sendWithAttachment(String to, String subject, String templateName, Map<String, String> data,
        InputStream attachment, String attachmentName) {

        LOG.debug("Sending email from {} to {} with subject {}", from, to, subject);

        MimeMessagePreparator messagePreparator = message -> {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(templateService.createTemplate(templateName, data));
            helper.addAttachment(attachmentName, new ByteArrayResource(IOUtils.toByteArray(attachment)));
        };
        mailSender.send(messagePreparator);

        IOUtils.closeQuietly(attachment);
    }
}
