package net.contargo.iris.transport.api;

import net.contargo.iris.transport.service.TransportChainGenerator;
import net.contargo.iris.transport.service.TransportDescriptionExtender;

import org.apache.commons.io.IOUtils;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class TransportApiControllerUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TransportDescriptionExtender transportDescriptionExtenderMock;

    @Autowired
    private TransportChainGenerator transportChainGeneratorMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        reset(transportDescriptionExtenderMock, transportChainGeneratorMock);
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void transports() throws Exception {

        TransportDescriptionDto description = new TransportDescriptionDto(new TransportTemplateDto(emptyList()));
        when(transportChainGeneratorMock.from(any(TransportTemplateDto.class))).thenReturn(asList(description,
                description));

        String json = IOUtils.toString(getClass().getResourceAsStream("/transport/request-transport-template.json"),
                "UTF-8");

        MockHttpServletRequestBuilder request = post("/transports").content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());

        verify(transportDescriptionExtenderMock, times(2)).withRoutingInformation(any(TransportDescriptionDto.class));
    }


    @Test
    public void transportsBadRequestInvalidTemplate() throws Exception {

        String json = IOUtils.toString(getClass().getResourceAsStream(
                    "/transport/invalid-request-transport-template.json"), "UTF-8");

        MockHttpServletRequestBuilder request = post("/transports").content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isBadRequest());

        verifyZeroInteractions(transportChainGeneratorMock);
        verifyZeroInteractions(transportDescriptionExtenderMock);
    }


    @Test
    public void transport() throws Exception {

        String json = IOUtils.toString(getClass().getResourceAsStream("/transport/request-transport.json"), "UTF-8");

        MockHttpServletRequestBuilder request = post("/transport").content(json)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());

        verify(transportDescriptionExtenderMock).withRoutingInformation(any(TransportDescriptionDto.class));
    }
}
