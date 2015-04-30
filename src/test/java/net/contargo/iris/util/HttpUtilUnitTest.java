
package net.contargo.iris.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.fail;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.unitils.thirdparty.org.apache.commons.io.IOUtils.toInputStream;

import static java.io.File.createTempFile;


/**
 * Unit test for {@link HttpUtil}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class HttpUtilUnitTest {

    private static final int STATUS_CODE_5 = 5;

    private HttpUtil sut;

    @Mock
    private HttpClient httpClientMock;
    @Mock
    private HttpResponse httpResponseMock;
    @Mock
    private HttpEntity entityMock;
    @Mock
    private StatusLine statusLineMock;

    @Before
    public void setup() throws IOException {

        sut = new HttpUtil(httpClientMock);

        when(httpClientMock.execute(any(HttpGet.class))).thenReturn(httpResponseMock);
        when(httpResponseMock.getEntity()).thenReturn(entityMock);

        when(httpResponseMock.getStatusLine()).thenReturn(statusLineMock);
        when(statusLineMock.getStatusCode()).thenReturn(STATUS_CODE_5);
    }


    @Test(expected = RuntimeException.class)
    public void httpGetCantBeExecuted() {

        sut = new HttpUtil(HttpClientBuilder.create().build());
        sut.getResponseContent("foo");
    }


    @Test
    public void closeInputStream() throws IOException {

        InputStream stream = new FileInputStream(createTempFile("tmp", "txt"));
        when(entityMock.getContent()).thenReturn(stream);

        sut.getResponseContent("bar");

        try {
            // there is no isClosed flag on InputStream so you have to check if it's closed this way
            stream.read();
            fail("Expected Stream closed exception");
        } catch (IOException ex) {
            assertThat("Stream Closed", is(ex.getMessage()));
        }
    }


    @Test
    public void closeInputStreamWithExceptionWhichGetsCaught() throws IOException {

        when(entityMock.getContent()).thenReturn(toInputStream("inputstream stub"));

        sut.getResponseContent("bar");
    }


    @Test(expected = HttpUtilException.class)
    public void executeWithIOExceptionWhichGetsCaught() throws IOException {

        doThrow(new IOException()).when(httpClientMock).execute(any(HttpGet.class));
        sut.getResponseContent("bar");
    }


    @Test
    public void closeEntityNullDoesntLeadToNullpointer() {

        when(httpResponseMock.getEntity()).thenReturn(null);
        sut.getResponseContent("bar");
    }
}
