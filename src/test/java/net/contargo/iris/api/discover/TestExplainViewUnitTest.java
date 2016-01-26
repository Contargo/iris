package net.contargo.iris.api.discover;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import net.contargo.iris.security.UserAuthenticationService;

import org.junit.Before;
import org.junit.Test;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Collections.singletonList;


/**
 * Unit test of {@link net.contargo.iris.api.discover.TestExplainView}.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public class TestExplainViewUnitTest {

    public static final int WANTED_NUMBER_OF_INVOCATIONS = 4;
    private final HttpServletResponse responseMock = mock(HttpServletResponse.class);
    private final HttpServletRequest requestMock = mock(HttpServletRequest.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    private final ServletOutputStream outputStreamMock = mock(ServletOutputStream.class);
    private final ResourceSupport countriesResponse = new ResourceSupport();
    private final UserAuthenticationService userAuthenticationService = mock(UserAuthenticationService.class);

    private final TestExplainView sut = new TestExplainView(userAuthenticationService, new ObjectMapper());
    private final Map<String, Object> model = new HashMap<>();

    @Before
    public void setUp() throws IOException {

        when(responseMock.getOutputStream()).thenReturn(outputStreamMock);

        model.put("aaa", 2.0);
        model.put("response", countriesResponse);

        when(requestMock.getHeader("Accept")).thenReturn("acceptHeader");
        when(requestMock.getRequestURL()).thenReturn(new StringBuffer("http://requestURL.de"));
        when(requestMock.getQueryString()).thenReturn("queryString");
        when(requestMock.getRemoteHost()).thenReturn("127.0.0.1");

        UserDetails userDetails = new User("admin@synyx.de", "password",
                singletonList(new SimpleGrantedAuthority("USER")));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null);

        when(userAuthenticationService.getCurrentUser()).thenReturn(usernamePasswordAuthenticationToken);
    }


    @Test
    public void testRenderMergedOutputModel() throws IOException {

        sut.renderMergedOutputModel(model, requestMock, responseMock);

        verify(outputStreamMock, times(WANTED_NUMBER_OF_INVOCATIONS)).write(any(byte[].class));
    }


    @Test
    public void addGreetingSection() {

        StringBuilder builder = new StringBuilder();
        sut.addGreetingSection(builder, requestMock);

        assertThat(builder.toString(),
            is("<h1>Hello admin@synyx.de from 127.0.0.1</h1>"
                + "This is a <i>RESTful</i> webservice. <br/>"));
    }


    @Test
    public void testAddJSONPrettyPrintSection() throws IOException {

        StringBuilder builder = new StringBuilder();

        sut.addJSONPrettyPrintSection(responseMock, model, builder);
        assertThat(builder.toString(), is("<h2>JSON</h2><pre>"));
        verify(outputStreamMock).write(objectWriter.writeValueAsBytes(model));
        verify(outputStreamMock).write("</pre>".getBytes("UTF-8"));
    }


    @Test
    public void testAddFormatSection() {

        StringBuilder builder = new StringBuilder();
        sut.addFormatsSection(requestMock, builder);
        assertThat(builder.toString(),
            is("<h2>Request and Formats</h2>You requested this page using url "
                + "<b>http://requestURL.de?queryString</b> and Accept-Header "
                + "<b>acceptHeader</b>  <br/><br/>You requested this page as HTML "
                + "(Probably because your client has text/html prior to application/json or application/xml "
                + "in the Accept-Header.If you are intersted in another format please adjust your Accept-Header "
                + "or append .json or .xml at the url:<ul><li>"
                + "<a href='http://requestURL.de?queryString.json?queryString'>Show as json</a>  "
                + "(http://requestURL.de?queryString.json?queryString)</li></ul>"));
    }


    @Test
    public void testAddFormatSectionWithQueryStringNull() {

        when(requestMock.getQueryString()).thenReturn(null);

        StringBuilder builder = new StringBuilder();
        sut.addFormatsSection(requestMock, builder);
        assertThat(builder.toString(),
            is("<h2>Request and Formats</h2>You requested this page using url "
                + "<b>http://requestURL.de</b> and Accept-Header <b>acceptHeader</b>  "
                + "<br/><br/>You requested this page as HTML (Probably because your client has text/html "
                + "prior to application/json or application/xml in the Accept-Header.If you are intersted "
                + "in another format please adjust your Accept-Header or append .json or .xml at the url:"
                + "<ul><li><a href='http://requestURL.de.json'>Show as json</a>  "
                + "(http://requestURL.de.json)</li></ul>"));
    }


    @Test
    public void testAddLinkSection() {

        StringBuilder builder = new StringBuilder();
        countriesResponse.add(new Link("http://textvomlink.de"));
        sut.addLinkSection(builder, model);
        assertThat(builder.toString(),
            is("<h2>Links</h2>You can navigate to the following other resources "
                + "from here:<ul><li><a href='http://textvomlink.de'>self</a> (http://textvomlink.de)</li></ul>"));
    }


    @Test
    public void testAddLinkSectionWithPlaceholder() {

        StringBuilder builder = new StringBuilder();
        countriesResponse.add(new Link("http://textvomlink.de/{placeholder}"));
        sut.addLinkSection(builder, model);
        assertThat(builder.toString(),
            is("<h2>Links</h2>You can navigate to the following other resources from here:"
                + "<ul><li>self: http://textvomlink.de/{placeholder} (url contains placeholders)</li></ul>"));
    }
}
