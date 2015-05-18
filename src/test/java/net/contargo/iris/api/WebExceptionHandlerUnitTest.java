package net.contargo.iris.api;

import net.contargo.iris.security.UserAuthenticationService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.unitils.thirdparty.org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link WebExceptionHandler}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class WebExceptionHandlerUnitTest {

    private static final String REPORT_PATH = "test-tmp";

    private WebExceptionHandler sut;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Mock
    private UserAuthenticationService userAuthenticationServiceMock;

    @Before
    public void setUp() {

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        sut = new WebExceptionHandler(new File(REPORT_PATH), userAuthenticationServiceMock);
    }


    @After
    public void tearDown() {

        // clean up the directory and delete all the files created for this test

        File dir = new File(REPORT_PATH);
        File[] files = dir.listFiles();

        for (File file : files) {
            file.delete();
        }

        dir.delete();
    }


    @Test
    public void doResolveExceptionErrorReportContainsTheRelevantInformation() throws Exception {

        sut.doResolveException(request, response, sut, new IOException("Exception"));

        File dir = new File(REPORT_PATH);
        File[] files = dir.listFiles();

        assertThat(files.length, equalTo(1));
        assertThat(IOUtils.toString(new FileInputStream(files[0])), containsString("java.io.IOException: Exception"));
    }


    @Test
    public void textualRepresentationOfCurrentUser() {

        UserDetails userDetails = new User("admin@synyx.de", "password", asList(new SimpleGrantedAuthority("USER")));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null);

        when(userAuthenticationServiceMock.getCurrentUser()).thenReturn(usernamePasswordAuthenticationToken);

        String user = sut.textualRepresentationOfCurrentUser();
        assertThat(user, is("user: admin@synyx.de\n"));
    }


    @Test
    public void textualRepresentationOfCurrentUserNull() {

        when(userAuthenticationServiceMock.getCurrentUser()).thenReturn(null);

        String user = sut.textualRepresentationOfCurrentUser();
        assertThat(user, is("?"));
    }
}
