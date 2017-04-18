package net.contargo.iris.mail.service;

import java.util.Map;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface TemplateService {

    /**
     * Creates a template.
     *
     * @param  templateName  the name of the template
     * @param  data  the template parameters, may be {@code null}
     *
     * @return  the template with all variable content filled with {@code data}
     */
    String createTemplate(String templateName, Map<String, ?> data);
}
