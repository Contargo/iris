package net.contargo.iris.transport.api;

import net.contargo.iris.transport.service.DescriptionGenerator;
import net.contargo.iris.transport.service.TransportDescriptionExtender;

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

import static org.junit.Assert.*;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class TransportApiControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TransportDescriptionExtender transportDescriptionExtenderMock;
    @Autowired
    private DescriptionGenerator descriptionGeneratorMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        reset(transportDescriptionExtenderMock);
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void all() throws Exception {

        TransportDescriptionDto description = new TransportDescriptionDto(new TransportTemplateDto(emptyList()));
        when(descriptionGeneratorMock.from(any(TransportTemplateDto.class))).thenReturn(asList(description,
                description));

        MockHttpServletRequestBuilder request = post("/transports/all").content(getContent())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());

        verify(transportDescriptionExtenderMock, times(2)).withRoutingInformation(any(TransportDescriptionDto.class));
    }


    @Test
    public void single() throws Exception {

        MockHttpServletRequestBuilder request = post("/transports/single").content(getContent())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());

        verify(transportDescriptionExtenderMock).withRoutingInformation(any(TransportDescriptionDto.class));
    }


    private String getContent() {

        return "{"
            + "  \"transportDescription\": ["
            + "    {"
            + "      \"fromSite\": {"
            + "        \"type\": \"TERMINAL\""
            + "      },"
            + "      \"toSite\": {"
            + "        \"type\": \"ADDRESS\","
            + "        \"lat\": 49.004895,"
            + "        \"lon\": 8.38487511865672"
            + "      },"
            + "      \"loadingState\": \"EMPTY\""
            + "    },"
            + "    {"
            + "      \"fromSite\": {"
            + "        \"type\": \"ADDRESS\","
            + "        \"coordinates\": {"
            + "          \"lat\": 49.004895,"
            + "          \"lon\": 8.38487511865672"
            + "        }"
            + "      },"
            + "      \"toSite\": {"
            + "        \"type\": \"TERMINAL\""
            + "      },"
            + "      \"loadingState\": \"FULL\""
            + "    },"
            + "    {"
            + "      \"fromSite\": {"
            + "        \"type\": \"TERMINAL\""
            + "      },"
            + "      \"toSite\": {"
            + "        \"type\": \"SEAPORT\","
            + "        \"uuid\": \"1300000000000006\""
            + "      },"
            + "      \"loadingState\": \"FULL\""
            + "    }"
            + "  ]"
            + "}";
    }
}
