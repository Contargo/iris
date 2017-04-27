package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.mail.service.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import java.lang.invoke.MethodHandles;

import java.util.HashMap;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class AddressMailService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final EmailService emailService;

    public AddressMailService(EmailService emailService) {

        this.emailService = emailService;
    }

    public void sendSuccessMail(String recipient, String filename, InputStream csv) {

        LOG.info("Generating static address import success report for {} (sending to {})", filename, recipient);

        HashMap<String, String> data = new HashMap<>();
        data.put("username", recipient);
        data.put("csvfilename", filename);

        String attachmentName = "address-import-" + recipient.replace("@", "-at-").replaceAll("\\.", "-") + "-"
            + now().format(ISO_LOCAL_DATE) + ".csv";

        emailService.sendWithAttachment(recipient, "Static Address Import - Report", "address-upload.ftl", data, csv,
            attachmentName);
    }


    public void sendErrorMail(String recipient, String filename) {

        LOG.info("Generating static address import error report for {} (sending to {})", filename, recipient);

        HashMap<String, String> data = new HashMap<>();
        data.put("username", recipient);
        data.put("csvfilename", filename);

        emailService.send(recipient, "Static Address Import - ERROR", "address-upload-failed.ftl", data);
    }
}
