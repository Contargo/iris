package net.contargo.iris.connection.api;

import net.contargo.iris.connection.dto.MainRunConnectionDto;
import net.contargo.iris.connection.dto.MainRunConnectionDtoService;
import net.contargo.iris.connection.dto.SimpleMainRunConnectionDto;
import net.contargo.iris.connection.service.DuplicateMainRunConnectionException;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;

import static net.contargo.iris.route.RouteType.BARGE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import static java.util.Collections.singletonList;


/**
 * Mvc unit test for {@link MainRunConnectionApiController}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class MainRunConnectionApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MainRunConnectionDtoService connectionApiDtoServiceMock;

    @Before
    public void setUp() {

        reset(connectionApiDtoServiceMock);
    }


    @Test
    public void getConnectionsForTerminal() throws Exception {

        String terminalUid = "5";
        String seaportUid = "23";

        SimpleMainRunConnectionDto dto = new SimpleMainRunConnectionDto(seaportUid, terminalUid, BARGE);
        when(connectionApiDtoServiceMock.getConnectionsForTerminal(new BigInteger(terminalUid))).thenReturn(
            singletonList(dto));

        MockHttpServletRequestBuilder builder = get("/connections");
        builder.accept(APPLICATION_JSON);
        builder.param("terminalUid", terminalUid);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(APPLICATION_JSON_UTF8));

        resultActions.andExpect(jsonPath("$.[0].terminalUid", is(terminalUid)));
        resultActions.andExpect(jsonPath("$.[0].seaportUid", is(seaportUid)));
        resultActions.andExpect(jsonPath("$.[0].routeType", is("BARGE")));
    }


    @Test
    public void getConnection() throws Exception {

        MainRunConnectionDto dto = new MainRunConnectionDto(42L, "1", "2", ONE, TEN, ZERO, ONE, BARGE, true);

        when(connectionApiDtoServiceMock.get(42L)).thenReturn(dto);

        MockHttpServletRequestBuilder builder = get("/connections/42").accept(APPLICATION_JSON);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json;charset=UTF-8"));
        resultActions.andExpect(jsonPath("$.id", is(42)));
        resultActions.andExpect(jsonPath("$.bargeDieselDistance", is(1)));
        resultActions.andExpect(jsonPath("$.railDieselDistance", is(10)));
        resultActions.andExpect(jsonPath("$.railElectricDistance", is(0)));
        resultActions.andExpect(jsonPath("$.routeType", is("BARGE")));
        resultActions.andExpect(jsonPath("$.seaportUid", is("1")));
        resultActions.andExpect(jsonPath("$.terminalUid", is("2")));
        resultActions.andExpect(jsonPath("$.enabled", is(true)));
    }


    @Test
    public void createConnection() throws Exception {

        when(connectionApiDtoServiceMock.save(any(MainRunConnectionDto.class))).thenReturn(new MainRunConnectionDto(
                42L, null, null, null, null, null, null, null, null));

        MockHttpServletRequestBuilder builder = post("/connections/");
        builder.contentType("application/json;charset=UTF-8");
        builder.content(
            "{\"id\":42, \"bargeDieselDistance\":1,\"railDieselDistance\":10,\"roadDistance\":10,\"railElectricDistance\":0,"
            + "\"routeType\":\"BARGE\",\"seaportUid\":\"1\",\"terminalUid\":\"2\","
            + "\"enabled\":true}");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(header().string("location", endsWith("/web/connections/42")));

        ArgumentCaptor<MainRunConnectionDto> captor = ArgumentCaptor.forClass(MainRunConnectionDto.class);

        verify(connectionApiDtoServiceMock).save(captor.capture());

        assertThat(captor.getValue().getId(), is(42L));
    }


    @Test
    public void createConnectionDuplicate() throws Exception {

        when(connectionApiDtoServiceMock.save(any(MainRunConnectionDto.class))).thenThrow(
            new DuplicateMainRunConnectionException());

        MockHttpServletRequestBuilder builder = post("/connections/");
        builder.contentType("application/json;charset=UTF-8");
        builder.content(
            "{\"id\":42, \"bargeDieselDistance\":1,\"railDieselDistance\":10,\"roadDistance\":10,\"railElectricDistance\":0,"
            + "\"routeType\":\"BARGE\",\"seaportUid\":\"1\",\"terminalUid\":\"2\","
            + "\"enabled\":true}");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.message",
                is("Mainrun connection with given seaport, terminal and route type exists")));
        resultActions.andExpect(jsonPath("$.code", is("mainrunconnection.duplicate")));
    }


    @Test
    public void createConnectionWithValidationErrors() throws Exception {

        MockHttpServletRequestBuilder builder = post("/connections/");
        builder.contentType("application/json;charset=UTF-8");
        builder.content(
            "{\"id\":42, \"bargeDieselDistance\":11111111111,\"railDieselDistance\":10,\"railElectricDistance\":0,"
            + "\"roadDistance\":0,\"routeType\":\"BARGE\",\"seaportUid\":\"1\",\"terminalUid\":\"2\",\"enabled\":true}");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.message",
                is("bargeDieselDistance: The count of the digits before the point is out of range. "
                    + "It should be in the range 1 - 10.")));
    }


    @Test
    public void updateConnection() throws Exception {

        MockHttpServletRequestBuilder builder = put("/connections/42");
        builder.contentType("application/json;charset=UTF-8");
        builder.content(
            "{\"id\":42, \"bargeDieselDistance\":1,\"railDieselDistance\":10,\"roadDistance\":10,\"railElectricDistance\":0,"
            + "\"routeType\":\"BARGE\",\"seaportUid\":\"1\",\"terminalUid\":\"2\","
            + "\"enabled\":true}");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());

        ArgumentCaptor<MainRunConnectionDto> captor = ArgumentCaptor.forClass(MainRunConnectionDto.class);

        verify(connectionApiDtoServiceMock).save(captor.capture());

        assertThat(captor.getValue().getId(), is(42L));
    }


    @Test
    public void updateConnectionWithValidationErrors() throws Exception {

        MockHttpServletRequestBuilder builder = put("/connections/42");
        builder.contentType("application/json;charset=UTF-8");
        builder.content(
            "{\"id\":42, \"bargeDieselDistance\":1,\"railDieselDistance\":\"\",\"roadDistance\":\"0\",\"railElectricDistance\":0,"
            + "\"routeType\":\"BARGE\",\"seaportUid\":\"1\",\"terminalUid\":\"2\","
            + "\"enabled\":true}");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.message", is("railDieselDistance: may not be null")));
    }


    @Test
    public void getTypesWithDtruck() throws Exception {

        ResultActions resultActions = perform(get("/connections/types"));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$['BARGE']", is("Barge")));
        resultActions.andExpect(jsonPath("$['RAIL']", is("Rail")));
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
