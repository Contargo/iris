package net.contargo.iris.route.api;

import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.connection.dto.SeaportConnectionRoutesDtoService;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.route.service.RouteUrlSerializationService;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.seaport.dto.SeaportDtoService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;

import static net.contargo.iris.container.ContainerType.TWENTY_LIGHT;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;

import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class RoutesApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private SeaportDtoService seaportDtoServiceMock;
    @Autowired
    private SeaportConnectionRoutesDtoService seaportConnectionRoutesDtoServiceMock;
    @Autowired
    private RouteUrlSerializationService routeUrlSerializationServiceMock;

    @Before
    public void setUp() {

        reset(seaportDtoServiceMock);
        reset(seaportConnectionRoutesDtoServiceMock);
        reset(routeUrlSerializationServiceMock);
    }


    @Test
    public void getSeaportRoutes() throws Exception {

        BigInteger seaportUid = new BigInteger("42");

        Seaport port = new Seaport();
        port.setUniqueId(seaportUid);

        SeaportDto seaportDto = new SeaportDto(port);

        Route route = new Route();
        RouteDto routeDto = new RouteDto(route);

        String url = String.format("/connections/%s/%s:%s/%s", seaportUid, TEN, ONE, true);

        when(seaportDtoServiceMock.getByUid(seaportUid)).thenReturn(seaportDto);
        when(seaportConnectionRoutesDtoServiceMock.getAvailableSeaportConnectionRoutes(eq(seaportDto),
                    any(RouteInformation.class))).thenReturn(singletonList(routeDto));

        MockHttpServletRequestBuilder builder = get(url);
        builder.param("containerType", TWENTY_LIGHT.toString());
        builder.accept(APPLICATION_JSON);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(APPLICATION_JSON));
        resultActions.andExpect(jsonPath("$.response.routes", hasSize(1)));
        resultActions.andExpect(jsonPath("$response.routes[0].roundTrip", is(false)));

        verify(routeUrlSerializationServiceMock).serializeUrl(routeDto, "/routedetails", "/routepartdetails");
    }


    @Test
    public void getRoutes() throws Exception {

        BigInteger seaportUid = new BigInteger("42");

        Seaport port = new Seaport();
        port.setUniqueId(seaportUid);

        SeaportDto seaportDto = new SeaportDto(port);

        Route route = new Route();
        RouteDto routeDto = new RouteDto(route);

        String url = String.format("/routes/%s/%s:%s/%s", seaportUid, TEN, ONE, true);

        when(seaportDtoServiceMock.getByUid(seaportUid)).thenReturn(seaportDto);
        when(seaportConnectionRoutesDtoServiceMock.getAvailableSeaportConnectionRoutes(eq(seaportDto),
                    any(RouteInformation.class))).thenReturn(singletonList(routeDto));

        MockHttpServletRequestBuilder builder = get(url);
        builder.param("containerType", TWENTY_LIGHT.toString());
        builder.accept(APPLICATION_JSON);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(APPLICATION_JSON));
        resultActions.andExpect(jsonPath("$.response.routes", hasSize(1)));
        resultActions.andExpect(jsonPath("$response.routes[0].roundTrip", is(false)));

        verify(routeUrlSerializationServiceMock).serializeUrl(routeDto, "/routedetails", "/routepartdetails");
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
