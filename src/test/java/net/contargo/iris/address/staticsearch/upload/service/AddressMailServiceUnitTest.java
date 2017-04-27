package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.mail.service.EmailService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;

import static org.mockito.Mockito.verify;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class AddressMailServiceUnitTest {

    private AddressMailService sut;

    @Mock
    private EmailService emailServiceMock;
    @Captor
    private ArgumentCaptor<Map<String, String>> dataCaptor;
    @Captor
    private ArgumentCaptor<String> attachmentNameCaptor;

    @Before
    public void setUp() {

        sut = new AddressMailService(emailServiceMock);
    }


    @Test
    public void send() {

        ByteArrayInputStream inputStream = new ByteArrayInputStream("example".getBytes());

        sut.sendSuccessMail("user@example.com", "addresses.csv", inputStream);

        verify(emailServiceMock).sendWithAttachment(eq("user@example.com"), eq("Static Address Import - Report"),
            eq("address-upload.ftl"), dataCaptor.capture(), eq(inputStream), attachmentNameCaptor.capture());

        assertThat(attachmentNameCaptor.getValue()
            .matches("address-import-user-at-example-com-\\d{4}-\\d{2}-\\d{2}\\.csv"), is(true));

        Map<String, ?> data = dataCaptor.getValue();
        assertThat(data.size(), is(2));
        assertThat(data, hasEntry("username", "user@example.com"));
        assertThat(data, hasEntry("csvfilename", "addresses.csv"));
    }
}
