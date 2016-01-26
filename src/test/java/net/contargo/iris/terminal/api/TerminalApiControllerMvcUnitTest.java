package net.contargo.iris.terminal.api;

import net.contargo.iris.connection.dto.SeaportTerminalConnectionDtoService;
import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.seaport.dto.SeaportDtoService;
import net.contargo.iris.terminal.dto.TerminalDto;
import net.contargo.iris.terminal.dto.TerminalDtoService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.route.RouteType.RAIL;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.argThat;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * Unit test for {@link TerminalApiController}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class TerminalApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private TerminalDtoService terminalDtoService;
    @Autowired
    private SeaportDtoService seaportDtoService;
    @Autowired
    private SeaportTerminalConnectionDtoService seaportTerminalConnectionDtoService;

    private MockMvc mockMvc;
    private TerminalDto terminalDto1;
    private TerminalDto terminalDto2;
    private SeaportDto seaportDto1;

    @Before
    public void before() {

        mockMvc = webAppContextSetup(webApplicationContext).build();

        reset(terminalDtoService);
        reset(seaportDtoService);
        reset(seaportTerminalConnectionDtoService);

        createTerminalDtos();
        createSeaportDtos();
    }


    private void createTerminalDtos() {

        terminalDto1 = new TerminalDto();
        terminalDto1.setEnabled(true);
        terminalDto1.setName("terminalDto1");
        terminalDto1.setLatitude(BigDecimal.ONE);
        terminalDto1.setLongitude(BigDecimal.TEN);

        terminalDto2 = new TerminalDto();
        terminalDto2.setEnabled(true);
        terminalDto2.setName("terminalDto2");
        terminalDto2.setLatitude(BigDecimal.ONE);
        terminalDto2.setLongitude(BigDecimal.ZERO);
    }


    private void createSeaportDtos() {

        seaportDto1 = new SeaportDto();
        seaportDto1.setEnabled(true);
        seaportDto1.setName("terminalDto1");
        seaportDto1.setLatitude(BigDecimal.ONE);
        seaportDto1.setLongitude(BigDecimal.TEN);
    }


    @Test
    public void getTerminals() throws Exception {

        when(terminalDtoService.getAllActive()).thenReturn(asList(terminalDto1, terminalDto2));

        ResultActions resultActions = mockMvc.perform(get("/terminals").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json"));

        resultActions.andExpect(jsonPath("$response.terminals[0].latitude", is(terminalDto1.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminals[0].longitude",
                is(terminalDto1.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminals[0].name", is(terminalDto1.getName())));
        resultActions.andExpect(jsonPath("$response.terminals[0].enabled", is(terminalDto1.isEnabled())));

        resultActions.andExpect(jsonPath("$response.terminals[1].latitude", is(terminalDto2.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminals[1].longitude",
                is(terminalDto2.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminals[1].name", is(terminalDto2.getName())));
        resultActions.andExpect(jsonPath("$response.terminals[1].enabled", is(terminalDto2.isEnabled())));

        resultActions.andExpect(jsonPath("$response.links", hasSize(1)));
        resultActions.andExpect(jsonPath("$response.links[0].rel", is("self")));

        verify(terminalDtoService, never()).getAll();
    }


    @Test
    public void getAllTerminals() throws Exception {

        when(terminalDtoService.getAll()).thenReturn(asList(terminalDto1, terminalDto2));

        ResultActions resultActions = mockMvc.perform(get("/terminals?activeOnly=false").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json"));

        resultActions.andExpect(jsonPath("$response.terminals[0].latitude", is(terminalDto1.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminals[0].longitude",
                is(terminalDto1.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminals[0].name", is(terminalDto1.getName())));
        resultActions.andExpect(jsonPath("$response.terminals[0].enabled", is(terminalDto1.isEnabled())));

        resultActions.andExpect(jsonPath("$response.terminals[1].latitude", is(terminalDto2.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminals[1].longitude",
                is(terminalDto2.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminals[1].name", is(terminalDto2.getName())));
        resultActions.andExpect(jsonPath("$response.terminals[1].enabled", is(terminalDto2.isEnabled())));

        resultActions.andExpect(jsonPath("$response.links", hasSize(1)));
        resultActions.andExpect(jsonPath("$response.links[0].rel", is("self")));

        verify(terminalDtoService, never()).getAllActive();
    }


    @Test
    public void getTerminalsForSeaportAndRouteType() throws Exception {

        when(seaportDtoService.getByUid(new BigInteger("42"))).thenReturn(seaportDto1);
        when(seaportTerminalConnectionDtoService.findTerminalsConnectedToSeaPortByRouteType(seaportDto1, RAIL))
            .thenReturn(singletonList(terminalDto2));

        ResultActions resultActions = mockMvc.perform(get("/terminals").param("seaportUid", "42")
                .param("routeType", "RAIL")
                .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json"));

        resultActions.andExpect(jsonPath("$response.terminals", hasSize(1)));

        resultActions.andExpect(jsonPath("$response.terminals[0].latitude", is(terminalDto2.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminals[0].longitude",
                is(terminalDto2.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminals[0].name", is(terminalDto2.getName())));
        resultActions.andExpect(jsonPath("$response.terminals[0].enabled", is(terminalDto2.isEnabled())));

        resultActions.andExpect(jsonPath("$response.links", hasSize(1)));
        resultActions.andExpect(jsonPath("$response.links[0].rel", is("self")));
    }


    @Test
    public void getTerminalsForInvalidSeaport() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/terminals").param("seaportId", "42")
                .param("routeType", "RAIL")
                .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$response.terminals", empty()));
    }


    @Test
    public void getTerminalById() throws Exception {

        when(terminalDtoService.getByUid(new BigInteger("42"))).thenReturn(terminalDto2);

        ResultActions resultActions = mockMvc.perform(get("/terminals/42").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json"));

        resultActions.andExpect(jsonPath("$response.terminal.latitude", is(terminalDto2.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminal.longitude", is(terminalDto2.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$response.terminal.name", is(terminalDto2.getName())));
        resultActions.andExpect(jsonPath("$response.terminal.enabled", is(terminalDto2.isEnabled())));

        resultActions.andExpect(jsonPath("$response.links", hasSize(2)));
        resultActions.andExpect(jsonPath("$response.links[0].rel", is("self")));
        resultActions.andExpect(jsonPath("$response.links[0].href", endsWith("/terminals/42")));
        resultActions.andExpect(jsonPath("$response.links[1].rel", is("terminals")));
    }


    @Test
    public void getTerminalWithInvalidId() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/terminals/314").accept(APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }


    @Test
    public void syncTerminalCreate() throws Exception {

        BigInteger uniqueId = new BigInteger("8491748714921");

        when(terminalDtoService.existsByUniqueId(uniqueId)).thenReturn(false);

        ResultActions resultActions = mockMvc.perform(put("/terminals/8491748714921").contentType(APPLICATION_JSON)
                .content("{\"name\": \"foo\", \"enabled\": true, \"latitude\": 0, \"longitude\": 0}"));

        resultActions.andExpect(status().isCreated());

        verify(terminalDtoService).save(argThat(
                org.hamcrest.Matchers.<TerminalDto>allOf(hasProperty("uniqueId", is(uniqueId.toString())),
                    hasProperty("name", is("foo")), hasProperty("enabled", is(true)),
                    hasProperty("latitude", is(BigDecimal.ZERO)), hasProperty("longitude", is(BigDecimal.ZERO)))));
    }


    @Test
    public void syncTerminalUpdate() throws Exception {

        BigInteger uniqueId = new BigInteger("8491748714921");

        when(terminalDtoService.existsByUniqueId(uniqueId)).thenReturn(true);

        ResultActions resultActions = mockMvc.perform(put("/terminals/8491748714921").contentType(APPLICATION_JSON)
                .content("{\"name\": \"foo\", \"enabled\": true, \"latitude\": 0, \"longitude\": 0}"));

        resultActions.andExpect(status().isNoContent());

        verify(terminalDtoService).updateTerminal(Mockito.eq(uniqueId),
            argThat(
                org.hamcrest.Matchers.<TerminalDto>allOf(hasProperty("name", is("foo")),
                    hasProperty("enabled", is(true)), hasProperty("latitude", is(BigDecimal.ZERO)),
                    hasProperty("longitude", is(BigDecimal.ZERO)))));
    }
}
