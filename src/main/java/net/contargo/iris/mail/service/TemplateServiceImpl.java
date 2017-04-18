package net.contargo.iris.mail.service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.mail.MailPreparationException;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;

import java.lang.invoke.MethodHandles;

import java.util.Map;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TemplateServiceImpl implements TemplateService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Configuration configuration;

    public TemplateServiceImpl(Configuration configuration) {

        this.configuration = configuration;
    }

    @Override
    public String createTemplate(String templateName, Map<String, ?> data) {

        LOG.debug("Creating freemarker template '{}'", templateName);

        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate(templateName), data);
        } catch (IOException | TemplateException e) {
            throw new MailPreparationException("Creating template '" + templateName + "' unsuccessful", e);
        }
    }
}
