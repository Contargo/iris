package net.contargo.iris.routedatarevision.api;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;
import net.contargo.iris.routedatarevision.service.RevisionDoesNotExistException;
import net.contargo.iris.terminal.dto.TerminalDto;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class RouteDataRevisionApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RouteDataRevisionDtoService revisionDtoServiceMock;

    @Test
    public void getRevision() throws Exception {

        TerminalDto terminal = new TerminalDto();
        terminal.setUniqueId("1");

        RouteDataRevisionDto dto = new RouteDataRevisionDto();
        dto.setAirlineDistanceInMeter(BigDecimal.ONE);
        dto.setTollDistanceOneWayInMeter(BigDecimal.TEN);
        dto.setTruckDistanceOneWayInMeter(BigDecimal.ZERO);
        dto.setComment("abc");
        dto.setLatitude(BigDecimal.TEN);
        dto.setLongitude(BigDecimal.ZERO);
        dto.setTerminal(terminal);

        when(revisionDtoServiceMock.findNearest(eq("1"), any(GeoLocation.class))).thenReturn(dto);

        ResultActions resultActions = perform(get("/routerevisions").param("terminalUid", "1")
                .param("latitude", "10")
                .param("longitude", "0")
                .accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.airlineDistanceInMeter", comparesEqualTo(1)));
        resultActions.andExpect(jsonPath("$.tollDistanceOneWayInMeter", comparesEqualTo(10)));
        resultActions.andExpect(jsonPath("$.truckDistanceOneWayInMeter", comparesEqualTo(0)));
        resultActions.andExpect(jsonPath("$.comment", is("abc")));
        resultActions.andExpect(jsonPath("$.latitude", comparesEqualTo(10)));
        resultActions.andExpect(jsonPath("$.longitude", comparesEqualTo(0)));
        resultActions.andExpect(jsonPath("$.terminalUid", is("1")));
    }


    @Test
    public void getNoRevision() throws Exception {

        when(revisionDtoServiceMock.findNearest(eq("1"), any(GeoLocation.class))).thenThrow(
            new RevisionDoesNotExistException("foo", "bar"));

        ResultActions resultActions = perform(get("/routerevisions").param("terminalUid", "1")
                .param("latitude", "10")
                .param("longitude", "0")
                .accept(APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.code", is("bar")));
        resultActions.andExpect(jsonPath("$.message", is("foo")));
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
