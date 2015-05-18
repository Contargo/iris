
package net.contargo.iris.api;

import org.hamcrest.Matchers;

import org.junit.Assert;
import org.junit.Test;

import org.springframework.http.HttpStatus;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;


/**
 * Unit test for {@link PublicAPIExceptionHandler}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class PublicAPIExceptionHandlerUnitTest {

    private final PublicAPIExceptionHandler sut;

    private final HttpServletRequest request;
    private final MockHttpServletResponse response;

    public PublicAPIExceptionHandlerUnitTest() {

        sut = new PublicAPIExceptionHandler();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void resolveIllegalArgumentException() {

        String exMessage = "Invalid ID for Foo";

        sut.resolveException(request, response, sut, new IllegalArgumentException(exMessage));

        Assert.assertThat(response.getErrorMessage(), Matchers.containsString(exMessage));
        Assert.assertThat(response.getStatus(), Matchers.equalTo(HttpServletResponse.SC_BAD_REQUEST));
    }


    @Test
    public void resolveIllegalStateException() {

        String exMessage = "Invalid state foo";

        sut.resolveException(request, response, sut, new IllegalStateException(exMessage));

        Assert.assertThat(response.getErrorMessage(), Matchers.containsString(exMessage));
        Assert.assertThat(response.getStatus(), Matchers.equalTo(HttpServletResponse.SC_BAD_REQUEST));
    }


    @Test
    public void resolveHttpClientErrorException() {

        String exMessage = "Nominatim down";

        sut.resolveException(request, response, sut,
            new HttpClientErrorException(HttpStatus.SERVICE_UNAVAILABLE, exMessage));

        Assert.assertThat(response.getErrorMessage(), Matchers.containsString(exMessage));
        Assert.assertThat(response.getErrorMessage(),
            Matchers.containsString("Service is temporary not available, please try again later"));
        Assert.assertThat(response.getStatus(), Matchers.equalTo(HttpServletResponse.SC_SERVICE_UNAVAILABLE));
    }


    @Test
    public void resolveNullPointerException() {

        String exMessage = "This should never happen";

        // NullpointerException must be thrown for test purposes
        sut.resolveException(request, response, sut, new NullPointerException(exMessage));

        Assert.assertThat(response.getErrorMessage(), Matchers.containsString(exMessage));
        Assert.assertThat(response.getStatus(), Matchers.equalTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }


    @Test
    public void resolveRuntimeException() {

        String exMessage = "This should never happen";

        // Runtimeexception must be thrown for test purposes
        sut.resolveException(request, response, sut, new RuntimeException(exMessage));

        Assert.assertThat(response.getErrorMessage(), Matchers.containsString(exMessage));
        Assert.assertThat(response.getStatus(), Matchers.equalTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }


    @Test
    public void exceptionWhileResolvingGetsCaught() throws IOException {

        String exMessage = "Invalid state foo";
        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        doThrow(new IOException()).when(responseMock).sendError(HttpServletResponse.SC_BAD_REQUEST,
            "Bad request. " + exMessage);
        sut.resolveException(request, responseMock, sut, new IllegalStateException(exMessage));
    }
}
