package net.contargo.iris.connection.web;

import net.contargo.iris.Message;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import static net.contargo.iris.Message.MessageType.SUCCESS;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Arrays.asList;


/**
 * MVC Unit test of {@link MainRunConnectionController}.
 *
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:web-api-context.xml" })
public class MainRunConnectionControllerMvcUnitTest {

    @Autowired
    private MainRunConnectionService mainRunConnectionServiceMock;
    @Autowired
    private TerminalService terminalServiceMock;
    @Autowired
    private SeaportService seaportServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {

        reset(mainRunConnectionServiceMock);
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void getAllConnections() throws Exception {

        MainRunConnection mainRunConnection = new MainRunConnection();
        when(mainRunConnectionServiceMock.getAll()).thenReturn(asList(mainRunConnection));

        ResultActions resultActions = mockMvc.perform(get("/connections"));
        resultActions.andExpect(model().attribute("mainRunConnections", contains(mainRunConnection)));
        resultActions.andExpect(view().name("connectionManagement/mainrunconnections"));
        resultActions.andExpect(status().isOk());
    }


    @Test
    public void prepareForCreate() throws Exception {

        Terminal terminal = new Terminal();
        Seaport seaport = new Seaport();

        when(terminalServiceMock.getAll()).thenReturn(asList(terminal));
        when(seaportServiceMock.getAll()).thenReturn(asList(seaport));

        ResultActions resultActions = mockMvc.perform(get("/connections/new"));
        resultActions.andExpect(model().attribute("mainRunConnection", instanceOf(MainRunConnection.class)));
        resultActions.andExpect(model().attribute("routetypes", contains(RouteType.BARGE, RouteType.RAIL)));
        resultActions.andExpect(model().attribute("terminals", contains(terminal)));
        resultActions.andExpect(model().attribute("seaports", contains(seaport)));
        resultActions.andExpect(view().name("connectionManagement/mainrunconnection"));
        resultActions.andExpect(status().isOk());
    }


    @Test
    public void prepareForUpdate() throws Exception {

        MainRunConnection mainRunConnection = new MainRunConnection();
        mainRunConnection.setId(2L);

        Terminal terminal = new Terminal();
        Seaport seaport = new Seaport();

        when(mainRunConnectionServiceMock.getById(2L)).thenReturn(mainRunConnection);
        when(terminalServiceMock.getAll()).thenReturn(asList(terminal));
        when(seaportServiceMock.getAll()).thenReturn(asList(seaport));

        ResultActions resultActions = mockMvc.perform(get("/connections/2"));
        resultActions.andExpect(model().attribute("mainRunConnection", is(mainRunConnection)));
        resultActions.andExpect(model().attribute("routetypes", contains(RouteType.BARGE, RouteType.RAIL)));
        resultActions.andExpect(model().attribute("terminals", contains(terminal)));
        resultActions.andExpect(model().attribute("seaports", contains(seaport)));
        resultActions.andExpect(view().name("connectionManagement/mainrunconnection"));
        resultActions.andExpect(status().isOk());
    }


    @Test
    public void createConnection() throws Exception {

        MainRunConnection mainRunConnectionMock = mock(MainRunConnection.class);
        when(mainRunConnectionMock.getId()).thenReturn(2L);

        when(mainRunConnectionServiceMock.save(any(MainRunConnection.class))).thenReturn(mainRunConnectionMock);
        when(mainRunConnectionServiceMock.isAlreadyApplied(mainRunConnectionMock)).thenReturn(false);

        MockHttpServletRequestBuilder builder = post("/connections/");
        builder.param("electricDistance", "2.78");
        builder.param("dieselDistance", "3.14");
        builder.param("seaport.id", "1");
        builder.param("terminal.id", "1");
        builder.param("routeType", "BARGE");

        ResultActions resultActions = mockMvc.perform(builder);
        resultActions.andExpect(flash().attribute("message", instanceOf(Message.class)));
        resultActions.andExpect(redirectedUrl("/web/connections/2"));
        resultActions.andExpect(status().isFound());

        Message msg = (Message) resultActions.andReturn().getFlashMap().get("message");
        assertThat(msg.getMessage(), is("mainrunconnection.success.save.message"));
        assertThat(msg.getType(), is(SUCCESS));

        ArgumentCaptor<MainRunConnection> captor = ArgumentCaptor.forClass(MainRunConnection.class);
        verify(mainRunConnectionServiceMock).save(captor.capture());
        assertThat(captor.getValue().getId(), nullValue());
    }


    @Test
    public void createConnectionHasErrors() throws Exception {

        MainRunConnection mainRunConnectionMock = mock(MainRunConnection.class);
        when(mainRunConnectionMock.getId()).thenReturn(2L);

        when(mainRunConnectionServiceMock.isAlreadyApplied(mainRunConnectionMock)).thenReturn(false);

        MockHttpServletRequestBuilder builder = post("/connections/");
        builder.param("electricDistance", "2.78");
        builder.param("dieselDistance", "");

        ResultActions resultActions = mockMvc.perform(builder);
        resultActions.andExpect(view().name("connectionManagement/mainrunconnection"));
        resultActions.andExpect(status().isOk());
    }


    @Test
    public void updateConnection() throws Exception {

        MainRunConnection mainRunConnectionMock = mock(MainRunConnection.class);
        when(mainRunConnectionMock.getId()).thenReturn(2L);

        when(mainRunConnectionServiceMock.getById(2L)).thenReturn(mainRunConnectionMock);
        when(mainRunConnectionServiceMock.save(any(MainRunConnection.class))).thenReturn(mainRunConnectionMock);

        MockHttpServletRequestBuilder builder = put("/connections/2");
        builder.param("electricDistance", "2.78");
        builder.param("dieselDistance", "3.14");
        builder.param("seaport.id", "1");
        builder.param("terminal.id", "1");
        builder.param("routeType", "BARGE");

        ResultActions resultActions = mockMvc.perform(builder);
        resultActions.andExpect(flash().attribute("message", instanceOf(Message.class)));
        resultActions.andExpect(redirectedUrl("/web/connections/2"));
        resultActions.andExpect(status().isFound());

        Message msg = (Message) resultActions.andReturn().getFlashMap().get("message");
        assertThat(msg.getMessage(), is("mainrunconnection.success.edit.message"));
        assertThat(msg.getType(), is(SUCCESS));

        ArgumentCaptor<MainRunConnection> captor = ArgumentCaptor.forClass(MainRunConnection.class);
        verify(mainRunConnectionServiceMock).save(captor.capture());
        assertThat(captor.getValue().getId(), is(2L));
    }
}
