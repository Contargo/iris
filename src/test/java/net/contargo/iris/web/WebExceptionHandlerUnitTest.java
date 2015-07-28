package net.contargo.iris.web;

import net.contargo.iris.security.UserAuthenticationService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.core.Authentication;

import org.unitils.thirdparty.org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import static org.mockito.Mockito.when;


/**
 * Unit test for {@link net.contargo.iris.web.WebExceptionHandler}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class WebExceptionHandlerUnitTest {

    private static final String REPORT_PATH = "test-tmp";

    private WebExceptionHandler sut;

    @Mock
    private UserAuthenticationService userAuthenticationServiceMock;
    @Mock
    private Authentication authenticationMock;

    private MockHttpServletRequest requestMock;
    private MockHttpServletResponse responseMock;

    @Before
    public void setUp() {

        requestMock = new MockHttpServletRequest();
        responseMock = new MockHttpServletResponse();

        sut = new WebExceptionHandler(new File(REPORT_PATH), userAuthenticationServiceMock);
    }


    @After
    public void tearDown() {

        File dir = new File(REPORT_PATH);
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }

        dir.delete();
    }


    @Test
    public void doResolveExceptionErrorReportContainsTheRelevantInformation() throws Exception {

        when(userAuthenticationServiceMock.getCurrentUser()).thenReturn(authenticationMock);
        when(authenticationMock.getName()).thenReturn("thisName");

        sut.doResolveException(requestMock, responseMock, sut, new IOException("Exception"));

        File dir = new File(REPORT_PATH);
        File[] files = dir.listFiles();

        String errorReportContent = IOUtils.toString(new FileInputStream(files[0]));

        assertThat(files.length, equalTo(1));
        assertThat(errorReportContent, containsString("java.io.IOException: Exception"));
        assertThat(errorReportContent, containsString("Username: thisName"));
        assertThat(errorReportContent, containsString("Requested URL: http://localhost"));
        assertThat(errorReportContent, containsString("Controller: net.contargo.iris.web.WebExceptionHandler"));
    }
}
