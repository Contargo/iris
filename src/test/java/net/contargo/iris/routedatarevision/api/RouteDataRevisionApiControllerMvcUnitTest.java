package net.contargo.iris.routedatarevision.api;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;
import net.contargo.iris.routedatarevision.service.RevisionDoesNotExistException;
import net.contargo.iris.terminal.dto.TerminalDto;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private MessageSource messageSourceMock;

    @Before
    public void setUp() {

        reset(revisionDtoServiceMock);
        when(messageSourceMock.getMessage(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn("foo");
    }


    @Test
    public void getRevision() throws Exception {

        TerminalDto terminal = new TerminalDto();
        terminal.setUniqueId("1");

        RouteDataRevisionDto dto = new RouteDataRevisionDto();
        dto.setAirlineDistanceInKilometer(BigDecimal.ONE);
        dto.setTollDistanceOneWayInKilometer(BigDecimal.TEN);
        dto.setTruckDistanceOneWayInKilometer(BigDecimal.ZERO);
        dto.setComment("abc");
        dto.setLatitude(BigDecimal.TEN);
        dto.setLongitude(BigDecimal.ZERO);
        dto.setTerminal(terminal);

        when(revisionDtoServiceMock.findNearest(eq("1"), any(GeoLocation.class), eq(null))).thenReturn(dto);

        ResultActions resultActions = perform(get("/routerevisions").param("terminalUid", "1")
                .param("latitude", "10")
                .param("longitude", "0")
                .accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.airlineDistanceInKilometer", comparesEqualTo(1)));
        resultActions.andExpect(jsonPath("$.tollDistanceOneWayInKilometer", comparesEqualTo(10)));
        resultActions.andExpect(jsonPath("$.truckDistanceOneWayInKilometer", comparesEqualTo(0)));
        resultActions.andExpect(jsonPath("$.comment", is("abc")));
        resultActions.andExpect(jsonPath("$.latitude", comparesEqualTo(10)));
        resultActions.andExpect(jsonPath("$.longitude", comparesEqualTo(0)));
        resultActions.andExpect(jsonPath("$.terminalUid", is("1")));
    }


    @Test
    public void getNoRevision() throws Exception {

        when(revisionDtoServiceMock.findNearest(eq("1"), any(GeoLocation.class), eq(null))).thenThrow(
            new RevisionDoesNotExistException("foo", "bar"));

        ResultActions resultActions = perform(get("/routerevisions").param("terminalUid", "1")
                .param("latitude", "10")
                .param("longitude", "0")
                .accept(APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
        resultActions.andExpect(jsonPath("$.code", is("bar")));
        resultActions.andExpect(jsonPath("$.message", is("foo")));
    }


    @Test
    public void createRouteRevision() throws Exception {

        String requestBody = "{"
            + "  \"terminalUid\": \"345\", "
            + "  \"truckDistanceOneWayInKilometer\": 34,"
            + "  \"tollDistanceOneWayInKilometer\": 0,"
            + "  \"airlineDistanceInKilometer\": 0,"
            + "  \"latitude\": 4,"
            + "  \"longitude\": 3,"
            + "  \"radiusInMeter\": 0,"
            + "  \"comment\": \"\","
            + "  \"validFrom\": \"29.01.2015\","
            + "  \"validTo\": \"30.01.2015\""
            + "}";

        when(revisionDtoServiceMock.existsEntry(eq("1300000000000001"), eq(new BigDecimal("4")),
                    eq(new BigDecimal("3")), Mockito.any(ValidityRange.class), eq(null))).thenReturn(false);

        when(revisionDtoServiceMock.save(Mockito.any(RouteDataRevisionDto.class))).thenReturn(
            new RouteDataRevisionDto());

        ResultActions resultActions = perform(post("/routerevisions").contentType(APPLICATION_JSON)
                .content(requestBody));
        resultActions.andExpect(status().isCreated());

        ArgumentCaptor<RouteDataRevisionDto> argumentCaptor = ArgumentCaptor.forClass(RouteDataRevisionDto.class);
        verify(revisionDtoServiceMock).save(argumentCaptor.capture());

        RouteDataRevisionDto savedRevision = argumentCaptor.getValue();
        assertThat(savedRevision.getLatitude(), is(new BigDecimal(4)));
    }


    @Test
    public void updateRouteRevision() throws Exception {

        String requestBody = "{\n"
            + "  \"id\": 5,\n"
            + "  \"terminalUid\": \"345\", "
            + "  \"truckDistanceOneWayInKilometer\": 34,\n"
            + "  \"tollDistanceOneWayInKilometer\": 0,\n"
            + "  \"airlineDistanceInKilometer\": 0,\n"
            + "  \"latitude\": 4,\n"
            + "  \"longitude\": 3,\n"
            + "  \"radiusInMeter\": 0,\n"
            + "  \"comment\": \"\",\n"
            + "  \"validFrom\": \"29.01.2015\",\n"
            + "  \"validTo\": \"30.01.2015\"\n"
            + "}";

        when(revisionDtoServiceMock.existsEntry(eq("1300000000000001"), eq(new BigDecimal("4")),
                    eq(new BigDecimal("3")), Mockito.any(ValidityRange.class), eq(null))).thenReturn(false);

        when(revisionDtoServiceMock.save(Mockito.any(RouteDataRevisionDto.class))).thenReturn(
            new RouteDataRevisionDto());

        ResultActions resultActions = perform(put("/routerevisions/5").contentType(APPLICATION_JSON)
                .content(requestBody));
        resultActions.andExpect(status().isOk());

        ArgumentCaptor<RouteDataRevisionDto> argumentCaptor = ArgumentCaptor.forClass(RouteDataRevisionDto.class);
        verify(revisionDtoServiceMock).save(argumentCaptor.capture());

        RouteDataRevisionDto savedRevision = argumentCaptor.getValue();
        assertThat(savedRevision.getLatitude(), is(new BigDecimal(4)));
    }


    @Test
    public void createRouteRevisionMisingRequestFields() throws Exception {

        String requestBody = "{"
            + "}";

        ResultActions resultActions = perform(post("/routerevisions").contentType(APPLICATION_JSON)
                .content(requestBody));
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.code", is("validation.error")));
    }


    @Test
    public void createRouteRevisionInvalidRange() throws Exception {

        String requestBody = "{"
            + "  \"terminalUid\": \"345\", "
            + "  \"truckDistanceOneWayInKilometer\": 34,"
            + "  \"tollDistanceOneWayInKilometer\": 0,"
            + "  \"airlineDistanceInKilometer\": 0,"
            + "  \"latitude\": 4,"
            + "  \"longitude\": 3,"
            + "  \"radiusInMeter\": 0,"
            + "  \"comment\": \"\","
            + "  \"validFrom\": \"30.01.2015\","
            + "  \"validTo\": \"29.01.2015\""
            + "}";

        ResultActions resultActions = perform(post("/routerevisions").contentType(APPLICATION_JSON)
                .content(requestBody));
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.code", is("routerevision.validityrange")));
    }


    @Test
    public void createRouteRevisionAlreadyExists() throws Exception {

        String requestBody = "{"
            + "  \"terminalUid\": \"345\", "
            + "  \"truckDistanceOneWayInKilometer\": 34,"
            + "  \"tollDistanceOneWayInKilometer\": 0,"
            + "  \"airlineDistanceInKilometer\": 0,"
            + "  \"latitude\": 4,"
            + "  \"longitude\": 3,"
            + "  \"radiusInMeter\": 0,"
            + "  \"comment\": \"\","
            + "  \"validFrom\": \"29.01.2015\","
            + "  \"validTo\": \"30.01.2015\""
            + "}";

        when(revisionDtoServiceMock.existsEntry(eq("345"), eq(new BigDecimal("4")), eq(new BigDecimal("3")),
                    Mockito.any(ValidityRange.class), eq(null))).thenReturn(true);

        ResultActions resultActions = perform(post("/routerevisions").contentType(APPLICATION_JSON)
                .content(requestBody));
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.code", is("routerevision.exists")));
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
