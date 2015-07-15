package net.contargo.iris.connection.web;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Collections.singletonList;


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
        when(mainRunConnectionServiceMock.getAll()).thenReturn(singletonList(mainRunConnection));

        ResultActions resultActions = mockMvc.perform(get("/connections"));
        resultActions.andExpect(model().attribute("mainRunConnections", contains(mainRunConnection)));
        resultActions.andExpect(view().name("connectionManagement/mainrunconnections"));
        resultActions.andExpect(status().isOk());
    }


    @Test
    public void prepareForUpdate() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/connections/2"));
        resultActions.andExpect(view().name("connectionManagement/mainrunconnection"));
        resultActions.andExpect(status().isOk());
    }


    @Test
    public void prepareForCreate() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/connections/new"));
        resultActions.andExpect(view().name("connectionManagement/mainrunconnection"));
        resultActions.andExpect(status().isOk());
    }
}
