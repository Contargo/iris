package net.contargo.iris.mail.service;

import org.apache.commons.io.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.mail.MailPreparationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.lang.invoke.MethodHandles;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.util.Map;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class DummyEmailService implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TemplateService templateService;
    private final String from;

    public DummyEmailService(TemplateService templateService, String from) {

        this.templateService = templateService;
        this.from = from;
    }

    @Override
    public void send(String to, String subject, String templateName, Map<String, String> data) {

        String content = templateService.createTemplate(templateName, data);

        sendMail(to, from, subject, content);
    }


    @Override
    public void sendWithAttachment(String to, String subject, String templateName, Map<String, String> data,
        InputStream attachment, String attachmentName) {

        try {
            File tempFile = File.createTempFile("DummyEmailService-Attachment", attachmentName);
            Files.copy(attachment, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            String content = templateService.createTemplate(templateName, data);
            String attachmentNotification = "\n### ATTACHMENT: " + tempFile.getAbsolutePath() + " ###\n";

            sendMail(to, from, subject, content + attachmentNotification);
        } catch (IOException | MailPreparationException e) {
            LOG.error("Could not create dummy email from {} to {} with subject {}", from, to, subject, e);
        } finally {
            IOUtils.closeQuietly(attachment);
        }
    }


    private static void sendMail(String to, String from, String subject, String content) {

        LOG.info("Sending e-mail:");
        LOG.info("\tto = {}", to);
        LOG.info("\tfrom = {}", from);
        LOG.info("\tsubject = {}", subject);
        LOG.info("\tcontent = {}", content);
    }
}
