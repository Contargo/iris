package net.contargo.iris.routedatarevision.web;

import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;
import net.contargo.iris.terminal.service.TerminalService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Collections.singletonList;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-api-context.xml" })
@WebAppConfiguration
public class RouteDataRevisionControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RouteDataRevisionDtoService routeDataRevisionDtoServiceMock;
    @Autowired
    private TerminalService terminalServiceMock;
    private List<RouteDataRevisionDto> routeDataRevisions;
    private RouteDataRevisionDto routeDataRevision;
    private List<Terminal> terminals;

    @Before
    public void setUp() {

        routeDataRevision = new RouteDataRevisionDto();
        routeDataRevisions = singletonList(routeDataRevision);

        TerminalDto terminalDto = new TerminalDto();
        terminalDto.setUniqueId("foo");
        routeDataRevision.setTerminal(terminalDto);

        Terminal terminal = new Terminal();
        terminals = singletonList(terminal);
        reset(routeDataRevisionDtoServiceMock);
    }


    @Test
    public void getAll() throws Exception {

        when(routeDataRevisionDtoServiceMock.getRouteDataRevisions()).thenReturn(routeDataRevisions);

        ResultActions resultActions = perform(get("/routerevisions").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("routeRevisions", is(routeDataRevisions)));
        resultActions.andExpect(model().attribute("selectedTerminal", nullValue()));
    }


    @Test
    public void getAllByTerminal() throws Exception {

        when(routeDataRevisionDtoServiceMock.getRouteDataRevisions(5L)).thenReturn(routeDataRevisions);

        ResultActions resultActions = perform(get("/routerevisions").param("terminalId", "5").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("routeRevisions", is(routeDataRevisions)));
        resultActions.andExpect(model().attribute("selectedTerminal", 5L));

        verify(routeDataRevisionDtoServiceMock).getRouteDataRevisions(5L);
    }


    @Test
    public void createSkeleton() throws Exception {

        ResultActions resultActions = perform(get("/routerevisions/new").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("routeRevision", isA(RouteDataRevisionDto.class)));
    }


    @Test
    public void getOne() throws Exception {

        when(routeDataRevisionDtoServiceMock.getRouteDataRevision(6L)).thenReturn(routeDataRevision);
        when(terminalServiceMock.getAll()).thenReturn(terminals);

        ResultActions resultActions = perform(get("/routerevisions/6").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("routeRevision", is(routeDataRevision)));
        resultActions.andExpect(model().attribute("terminals", is(terminals)));
    }


    @Test
    public void create() throws Exception {

        when(routeDataRevisionDtoServiceMock.save(Mockito.any(RouteDataRevisionDto.class))).thenReturn(
            routeDataRevision);
        when(routeDataRevisionDtoServiceMock.existsEntry(eq("10"), eq(BigDecimal.TEN), eq(BigDecimal.ONE),
                    Mockito.any(ValidityRange.class), eq(null))).thenReturn(false);

        ResultActions resultActions = perform(post(
                        "/routerevisions?terminal.uniqueId=10&latitude=10&longitude=1&truckDistanceOneWayInKilometer=1&"
                        + "tollDistanceOneWayInKilometer=2&airlineDistanceInKilometer=3&radiusInMeter=4&validFrom=08.09.2015&validTo=23.09.2015")
                .contentType(APPLICATION_JSON));

        resultActions.andExpect(status().is3xxRedirection());
    }


    @Test
    public void createValidationErrors() throws Exception {

        when(routeDataRevisionDtoServiceMock.save(Mockito.any(RouteDataRevisionDto.class))).thenReturn(
            routeDataRevision);

        ResultActions resultActions = perform(post("/routerevisions").param("terminal.uniqueId", "foo")
                .contentType(APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().errorCount(7));
        resultActions.andExpect(model().attributeHasFieldErrorCode("routeRevision", "latitude", "NotNull"));
        resultActions.andExpect(model().attributeHasFieldErrorCode("routeRevision", "longitude", "NotNull"));
        resultActions.andExpect(model().attributeHasFieldErrorCode("routeRevision", "truckDistanceOneWayInKilometer",
                "NotNull"));
        resultActions.andExpect(model().attributeHasFieldErrorCode("routeRevision", "tollDistanceOneWayInKilometer",
                "NotNull"));
        resultActions.andExpect(model().attributeHasFieldErrorCode("routeRevision", "airlineDistanceInKilometer",
                "NotNull"));
        resultActions.andExpect(model().attributeHasFieldErrorCode("routeRevision", "radiusInMeter", "NotNull"));
        resultActions.andExpect(model().attributeHasFieldErrorCode("routeRevision", "validFrom", "NotNull"));
    }


    @Test
    public void createValidityRangeError() throws Exception {

        ResultActions resultActions = perform(post("/routerevisions").param("terminal.uniqueId", "foo")
                .param("validFrom", "01.05.2015")
                .param("validTo", "30.04.2015"));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attributeHasFieldErrorCode("routeRevision", "validFrom",
                "routerevision.validityrange"));
        resultActions.andExpect(model().attributeHasFieldErrorCode("routeRevision", "validTo",
                "routerevision.validityrange"));

        verifyZeroInteractions(routeDataRevisionDtoServiceMock);
    }


    @Test
    public void update() throws Exception {

        when(routeDataRevisionDtoServiceMock.save(Mockito.any(RouteDataRevisionDto.class))).thenReturn(
            routeDataRevision);

        ResultActions resultActions = perform(put(
                        "/routerevisions/7?id=7&terminal.uniqueId=10&latitude=10&longitude=1&truckDistanceOneWayInKilometer=1&"
                        + "tollDistanceOneWayInKilometer=2&airlineDistanceInKilometer=3&radiusInMeter=4&validFrom=08.09.2015&validTo=23.09.2015")
                .contentType(APPLICATION_JSON));

        resultActions.andExpect(status().is3xxRedirection());
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
