package net.contargo.iris.connection.api;

import net.contargo.iris.connection.dto.SeaportTerminalConnectionDtoService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.dto.SeaportDto;

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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class SeaportConnectionApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private SeaportTerminalConnectionDtoService seaportTerminalConnectionDtoService;

    private SeaportDto seaportDto1;
    private SeaportDto seaportDto2;

    @Before
    public void setUp() {

        reset(seaportTerminalConnectionDtoService);

        createSeaportDtos();
    }


    @Test
    public void getSeaportsOfConnections() throws Exception {

        when(seaportTerminalConnectionDtoService.findSeaportsConnectedByRouteType(RouteType.BARGE)).thenReturn(
            singletonList(seaportDto1));
        when(seaportTerminalConnectionDtoService.findSeaportsConnectedByRouteType(RouteType.RAIL)).thenReturn(asList(
                seaportDto1, seaportDto2));

        MockHttpServletRequestBuilder builder = get("/connections/seaports");
        builder.accept(APPLICATION_JSON);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json;charset=UTF-8"));
        resultActions.andExpect(jsonPath("$.seaports", hasSize(2)));
        resultActions.andExpect(jsonPath("$.seaports[*].latitude",
                containsInAnyOrder(seaportDto1.getLatitude().intValue(), seaportDto2.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$.seaports[*].longitude",
                containsInAnyOrder(seaportDto1.getLongitude().intValue(), seaportDto2.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$.seaports[*].name",
                containsInAnyOrder(seaportDto1.getName(), seaportDto2.getName())));
        resultActions.andExpect(jsonPath("$.seaports[*].enabled",
                containsInAnyOrder(seaportDto1.isEnabled(), seaportDto2.isEnabled())));
        resultActions.andExpect(jsonPath("$.links", hasSize(1)));
        resultActions.andExpect(jsonPath("$.links[0].rel", is("self")));
    }


    @Test
    public void getSeaportsOfConnectionsForCombo() throws Exception {

        when(seaportTerminalConnectionDtoService.findSeaportsConnectedByRouteType(RouteType.RAIL)).thenReturn(
            singletonList(seaportDto1));

        MockHttpServletRequestBuilder builder = get("/connections/seaports?combo=RAILWAY");
        builder.accept(APPLICATION_JSON);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json;charset=UTF-8"));
        resultActions.andExpect(jsonPath("$.seaports", hasSize(1)));
        resultActions.andExpect(jsonPath("$.seaports[0].latitude", is(seaportDto1.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$.seaports[0].longitude", is(seaportDto1.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$.seaports[0].name", is(seaportDto1.getName())));
        resultActions.andExpect(jsonPath("$.seaports[0].enabled", is(seaportDto1.isEnabled())));
        resultActions.andExpect(jsonPath("$.links", hasSize(1)));
        resultActions.andExpect(jsonPath("$.links[0].rel", is("self")));
    }


    @Test
    public void getSeaportsOfConnectionsForBadRequest() throws Exception {

        MockHttpServletRequestBuilder builder = get("/connections/seaports?combo=FOO");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isBadRequest());
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }


    private void createSeaportDtos() {

        seaportDto1 = new SeaportDto();
        seaportDto1.setEnabled(true);
        seaportDto1.setName("terminalDto1");
        seaportDto1.setLatitude(BigDecimal.ONE);
        seaportDto1.setLongitude(BigDecimal.TEN);

        seaportDto2 = new SeaportDto();
        seaportDto2.setEnabled(true);
        seaportDto2.setName("terminalDto2");
        seaportDto2.setLatitude(BigDecimal.ONE);
        seaportDto2.setLongitude(BigDecimal.ZERO);
    }
}
