package net.contargo.iris.mail.service;

import java.io.InputStream;

import java.util.Map;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface EmailService {

    /**
     * Sends an e-mail.
     *
     * @param  to  the recipient address
     * @param  subject  the e-mail subject
     * @param  templateName  the template that should be used
     * @param  data  template data, keyed by template variable names
     * @param  attachment  attachment content
     * @param  attachmentName  name of the attachment
     */
    void sendWithAttachment(String to, String subject, String templateName, Map<String, ?> data,
        InputStream attachment, String attachmentName);
}
