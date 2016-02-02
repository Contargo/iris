
package net.contargo.iris.api;

import net.contargo.iris.security.UserAuthenticationService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.http.HttpStatus;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_SERVICE_UNAVAILABLE;


/**
 * Unit test for {@link PublicAPIExceptionHandler}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class PublicAPIExceptionHandlerUnitTest {

    private PublicAPIExceptionHandler sut;

    private final MockHttpServletRequest requestMock;
    private final MockHttpServletResponse responseMock;

    @Mock
    private UserAuthenticationService userAuthenticationServiceMock;

    public PublicAPIExceptionHandlerUnitTest() {

        requestMock = new MockHttpServletRequest();
        responseMock = new MockHttpServletResponse();
    }

    @Before
    public void setUp() {

        sut = new PublicAPIExceptionHandler(userAuthenticationServiceMock);
    }


    @Test
    public void resolveIllegalArgumentException() {

        String exMessage = "Invalid ID for terminal";

        sut.resolveException(requestMock, responseMock, null, new IllegalArgumentException(exMessage));

        assertThat(responseMock.getErrorMessage(), containsString(exMessage));
        assertThat(responseMock.getStatus(), equalTo(SC_BAD_REQUEST));
    }


    @Test
    public void resolveIllegalStateException() {

        String exMessage = "Invalid application state";

        sut.resolveException(requestMock, responseMock, null, new IllegalStateException(exMessage));

        assertThat(responseMock.getErrorMessage(), containsString(exMessage));
        assertThat(responseMock.getStatus(), equalTo(SC_BAD_REQUEST));
    }


    @Test
    public void resolveHttpClientErrorException() {

        String exMessage = "Nominatim down";

        sut.resolveException(requestMock, responseMock, null,
            new HttpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE, exMessage));

        assertThat(responseMock.getErrorMessage(), containsString(exMessage));
        assertThat(responseMock.getErrorMessage(),
            containsString("Service is temporary not available, please try again later"));
        assertThat(responseMock.getStatus(), equalTo(SC_SERVICE_UNAVAILABLE));
    }


    @Test
    public void resolveNullPointerException() {

        String exMessage = "This should never happen";

        sut.resolveException(requestMock, responseMock, null, new NullPointerException(exMessage));

        assertThat(responseMock.getErrorMessage(), containsString(exMessage));
        assertThat(responseMock.getStatus(), equalTo(SC_INTERNAL_SERVER_ERROR));
    }


    @Test
    public void resolveRuntimeException() {

        String exMessage = "This should never happen";

        sut.resolveException(requestMock, responseMock, null, new RuntimeException(exMessage));

        assertThat(responseMock.getErrorMessage(), containsString(exMessage));
        assertThat(responseMock.getStatus(), equalTo(SC_INTERNAL_SERVER_ERROR));
    }


    @Test
    public void exceptionWhileResolvingGetsCaught() throws IOException {

        String exMessage = "Invalid application state";
        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        doThrow(new IOException()).when(responseMock).sendError(SC_BAD_REQUEST, "Bad requestMock. " + exMessage);

        sut.resolveException(requestMock, responseMock, null, new IllegalStateException(exMessage));
    }
}
