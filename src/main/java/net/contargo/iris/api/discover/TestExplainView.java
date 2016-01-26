package net.contargo.iris.api.discover;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import net.contargo.iris.security.UserAuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.ApplicationContextAware;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.apache.commons.lang.CharEncoding.UTF_8;

import static java.util.Collections.singletonList;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
public class TestExplainView extends MappingJackson2JsonView implements ApplicationContextAware {

    private final ObjectMapper objectMapper;

    @Value(value = "${application.version}")
    private String applicationVersion;

    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public TestExplainView(UserAuthenticationService userAuthenticationService, ObjectMapper objectMapper) {

        setContentType("text/html");
        this.userAuthenticationService = userAuthenticationService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
        HttpServletResponse response) throws IOException {

        @SuppressWarnings("unchecked")
        Map<String, Object> value = (Map<String, Object>) filterModel(model);

        StringBuilder builder = new StringBuilder();

        addGreetingSection(builder, request);

        addFormatsSection(request, builder);

        addLinkSection(builder, value);

        addJSONPrettyPrintSection(response, value, builder);

        response.getOutputStream().write(("<hr>" + applicationVersion).getBytes(UTF_8));
    }


    void addGreetingSection(StringBuilder builder, HttpServletRequest request) {

        String username = userAuthenticationService.getCurrentUser().getName();
        builder.append("<h1>Hello ").append(username).append(" from ").append(request.getRemoteHost()).append("</h1>");
        builder.append("This is a <i>RESTful</i> webservice. <br/>");
    }


    void addJSONPrettyPrintSection(HttpServletResponse response, Map<String, Object> value, StringBuilder builder)
        throws IOException {

        builder.append("<h2>JSON</h2><pre>");

        response.getOutputStream().write(builder.toString().getBytes(UTF_8));

        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        response.getOutputStream().write(writer.writeValueAsBytes(value));
        response.getOutputStream().write("</pre>".getBytes(UTF_8));
    }


    void addFormatsSection(HttpServletRequest request, StringBuilder builder) {

        String accept = request.getHeader("Accept");
        StringBuffer urlB = request.getRequestURL();
        String q = request.getQueryString();

        if (q != null) {
            urlB.append('?').append(q);
        }

        builder.append("<h2>Request and Formats</h2>");

        String url = urlB.toString();

        builder.append("You requested this page using url <b>")
            .append(url)
            .append("</b> and Accept-Header <b>")
            .append(accept)
            .append("</b>  <br/><br/>");

        builder.append("You requested this page as HTML (Probably because your client has text/html "
            + "prior to application/json or application/xml in the Accept-Header.");
        builder.append("If you are intersted in another format please adjust your Accept-Header "
            + "or append .json or .xml at the url:<ul>");

        for (String type : singletonList("json")) {
            String typeUrl = url + "." + type;

            if (q != null) {
                typeUrl = request.getRequestURL().append(".").append(type).append("?").append(q).toString();
            }

            builder.append("<li><a href='")
                .append(typeUrl)
                .append("'>Show as ")
                .append(type)
                .append("</a>  (")
                .append(typeUrl)
                .append(")</li>");
        }

        builder.append("</ul>");
    }


    void addLinkSection(StringBuilder builder, Map<String, Object> value) {

        List<Link> links = new ArrayList<>();

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            Object v = entry.getValue();

            if (v instanceof ResourceSupport) {
                links = ((ResourceSupport) v).getLinks();
            }
        }

        builder.append("<h2>Links</h2>");

        builder.append("You can navigate to the following other resources from here:<ul>");

        String link;
        String decodedLink;

        for (Link singleLink : links) {
            link = singleLink.getHref();

            try {
                decodedLink = URLDecoder.decode(link, UTF_8);
            } catch (UnsupportedEncodingException e) {
                decodedLink = link;
            }

            if (decodedLink.contains("{")) {
                builder.append("<li>")
                    .append(singleLink.getRel())
                    .append(": ")
                    .append(decodedLink)
                    .append(" (url contains placeholders)</li>");
            } else {
                builder.append("<li><a href='")
                    .append(decodedLink)
                    .append("'>")
                    .append(singleLink.getRel())
                    .append("</a> (")
                    .append(decodedLink)
                    .append(")</li>");
            }
        }

        builder.append("</ul>");
    }
}
