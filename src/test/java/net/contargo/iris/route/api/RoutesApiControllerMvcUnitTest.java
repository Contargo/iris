package net.contargo.iris.route.api;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.dto.RouteDataDto;
import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.connection.dto.RoutePartDataDto;
import net.contargo.iris.connection.dto.RoutePartDto;
import net.contargo.iris.connection.dto.SeaportConnectionRoutesDtoService;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.route.RoutePartData;
import net.contargo.iris.route.dto.EnricherDtoService;
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

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.container.ContainerType.TWENTY_LIGHT;
import static net.contargo.iris.route.RouteCombo.WATERWAY;
import static net.contargo.iris.route.RouteDirection.EXPORT;
import static net.contargo.iris.route.RouteProduct.ONEWAY;
import static net.contargo.iris.route.RouteType.BARGE;

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

import static java.util.Arrays.asList;
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
    @Autowired
    private EnricherDtoService enricherDtoServiceMock;

    @Before
    public void setUp() {

        reset(seaportDtoServiceMock, seaportConnectionRoutesDtoServiceMock, routeUrlSerializationServiceMock,
            enricherDtoServiceMock);
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


    @Test
    public void getRoutesWithCoordinates() throws Exception {

        SeaportDto seaport1 = new SeaportDto();
        seaport1.setUniqueId("uuid");

        SeaportDto seaport2 = new SeaportDto();
        seaport2.setUniqueId("uuid2");

        when(seaportDtoServiceMock.getAllActive()).thenReturn(asList(seaport1, seaport2));

        RouteInformation routeInformation = new RouteInformation(new GeoLocation(new BigDecimal("49.00383145"),
                    new BigDecimal("8.3849306514255")), ONEWAY, TWENTY_LIGHT, EXPORT, WATERWAY);

        RouteDto routeDto1 = new RouteDto();
        RouteDto routeDto2 = new RouteDto();

        when(seaportConnectionRoutesDtoServiceMock.getAvailableSeaportConnectionRoutes(seaport1, routeInformation))
            .thenReturn(asList(routeDto1, routeDto2));

        RoutePartData routePartData1 = new RoutePartData();
        routePartData1.setBargeDieselDistance(new BigDecimal("300"));

        RoutePartDto routePartDto1 = new RoutePartDto();
        routePartDto1.setRouteType(BARGE);
        routePartDto1.setData(new RoutePartDataDto(routePartData1));

        RouteDataDto routeDataDto1 = new RouteDataDto();
        routeDataDto1.setTotalDistance(new BigDecimal("434"));
        routeDataDto1.setParts(singletonList(routePartDto1));

        RouteDto enrichedRouteDto1 = new RouteDto();
        enrichedRouteDto1.setData(routeDataDto1);

        RoutePartData routePartData2 = new RoutePartData();
        routePartData2.setBargeDieselDistance(new BigDecimal("150"));

        RoutePartDto routePartDto2 = new RoutePartDto();
        routePartDto2.setRouteType(BARGE);
        routePartDto2.setData(new RoutePartDataDto(routePartData2));

        RouteDataDto routeDataDto2 = new RouteDataDto();
        routeDataDto2.setTotalDistance(new BigDecimal("200"));
        routeDataDto2.setParts(singletonList(routePartDto2));

        RouteDto enrichedRouteDto2 = new RouteDto();
        enrichedRouteDto2.setData(routeDataDto2);

        when(enricherDtoServiceMock.enrich(routeDto1)).thenReturn(enrichedRouteDto1);
        when(enricherDtoServiceMock.enrich(routeDto2)).thenReturn(enrichedRouteDto2);

        ResultActions resultActions = perform(get("/routes").param("lat", "49.00383145")
                .param("lon", "8.3849306514255"));

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$[0].distance").value(200))
            .andExpect(jsonPath("$[0].bargeDistance").value(150))
            .andExpect(jsonPath("$[1].distance").value(434));
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
