package net.contargo.iris.route2.api;

import net.contargo.iris.route2.ModeOfTransport;
import net.contargo.iris.route2.service.RoutePartEdgeResult;
import net.contargo.iris.route2.service.RouteService;

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

import static net.contargo.iris.route2.RoutePartEdgeResultStatus.OK;

import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Arrays.asList;


/**
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class RouteApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RouteService routeServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void getRoutes() throws Exception {

        MockHttpServletRequestBuilder request = post("/route").content(getContent())
                .contentType(MediaType.APPLICATION_JSON);

        RoutePartEdgeResult result1 = new RoutePartEdgeResult(61299.3, 12259.5, asList("geometry1", "geometry2"), OK);
        RoutePartEdgeResult result2 = new RoutePartEdgeResult(61299.3, 22068, asList("geometry3", "geometry4"), OK);

        when(routeServiceMock.route(any(), any(), eq(ModeOfTransport.WATER))).thenReturn(result1, result2);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].distance", is(61299.3)))
            .andExpect(jsonPath("$[0].duration", is(12259.5)))
            .andExpect(jsonPath("$[0].geometries[0]", is("geometry1")))
            .andExpect(jsonPath("$[0].geometries[1]", is("geometry2")))
            .andExpect(jsonPath("$[0].status", is("OK")))
            .andExpect(jsonPath("$[1].distance", is(61299.3)))
            .andExpect(jsonPath("$[1].duration", is(22068.0)))
            .andExpect(jsonPath("$[1].geometries[0]", is("geometry3")))
            .andExpect(jsonPath("$[1].geometries[1]", is("geometry4")))
            .andExpect(jsonPath("$[1].status", is("OK")));

        verify(routeServiceMock, times(2)).route(any(), any(), eq(ModeOfTransport.WATER));
    }


    private String getContent() {

        return "[{"
            + "    \"modeOfTransport\": \"WATER\","
            + "    \"start\": {"
            + "      \"type\": \"TERMINAL\","
            + "      \"uuid\": \"1300000000000025\""
            + "    },"
            + "    \"end\": {"
            + "      \"type\": \"TERMINAL\","
            + "      \"uuid\": \"1300000000000023\""
            + "    }"
            + "  },"
            + "  {"
            + "    \"modeOfTransport\": \"WATER\","
            + "    \"start\": {"
            + "      \"type\": \"TERMINAL\","
            + "      \"uuid\": \"1300000000000023\""
            + "    },"
            + "    \"end\": {"
            + "      \"type\": \"TERMINAL\","
            + "      \"uuid\": \"1300000000000025\""
            + "    }"
            + "  }"
            + "]";
    }
}
