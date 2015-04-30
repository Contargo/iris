package net.contargo.iris.terminal.web;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

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
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

import java.util.Arrays;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;


/**
 * MVC Unit test of {@link net.contargo.iris.terminal.web.TerminalController}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-api-context.xml" })
@WebAppConfiguration
public class TerminalControllerMvcUnitTest {

    private static final int TERMINAL_ID = 2;
    public static final long TERMINAL_ID_777 = 777L;
    public static final String TERMINAL_NAME_1 = "Entenhausen";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TerminalService terminalServiceMock;

    private Terminal terminal1;
    private Terminal terminal2;

    @Before
    public void setUp() throws Exception {

        terminal1 = createTerminal();
        terminal2 = createTerminal();
        terminal2.setName("Gotham City");
        terminal2.setId(3L);
        terminal2.setLatitude(BigDecimal.TEN);
        terminal2.setLongitude(BigDecimal.TEN);
    }


    @Test
    public void testGetAllTerminals() throws Exception {

        when(terminalServiceMock.getAll()).thenReturn(Arrays.asList(terminal1, terminal2));

        ResultActions resultActions = perform(get("/terminals/").accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("terminals", Arrays.asList(terminal1, terminal2)));
        resultActions.andExpect(view().name("terminalManagement/terminals"));
    }


    @Test
    public void testCreateTerminal() throws Exception {

        ResultActions resultActions = perform(get("/terminals/new").accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(view().name("terminalManagement/terminal"));
    }


    @Test
    public void testSaveTerminal() throws Exception {

        String requestBody = new ObjectMapper().writeValueAsString(terminal1);
        when(terminalServiceMock.save(any(Terminal.class))).thenReturn(terminal1);

        MockHttpServletRequestBuilder builder = post("/terminals/?name=Entenhausen&latitude=1&longitude=1");
        builder.accept(APPLICATION_JSON);
        builder.contentType(APPLICATION_JSON);
        builder.content(requestBody);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isFound());
        resultActions.andExpect(view().name("redirect:/web/terminals/" + terminal1.getId()));
    }


    @Test
    public void testSaveIncompleteTerminal() throws Exception {

        Terminal incompleteTerminal = new Terminal();
        incompleteTerminal.setName(TERMINAL_NAME_1);
        incompleteTerminal.setLatitude(BigDecimal.ONE);

        String requestBody = new ObjectMapper().writeValueAsString(terminal1);
        when(terminalServiceMock.save(terminal1)).thenReturn(terminal1);

        MockHttpServletRequestBuilder builder = post("/terminals/?name=Entenhausen&latitude=1");
        builder.accept(APPLICATION_JSON);
        builder.contentType(APPLICATION_JSON);
        builder.content(requestBody);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());

        ModelAndView modelAndView = resultActions.andReturn().getModelAndView();
        assertReflectionEquals(modelAndView.getModelMap().get("terminal"), incompleteTerminal);
        resultActions.andExpect(view().name("terminalManagement/terminal"));
    }


    @Test
    public void testEditTerminal() throws Exception {

        when(terminalServiceMock.getById(2L)).thenReturn(terminal1);

        ResultActions resultActions = perform(get("/terminals/" + TERMINAL_ID));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("terminal", terminal1));
        resultActions.andExpect(view().name("terminalManagement/terminal"));
    }


    @Test
    public void testUpdateTerminal() throws Exception {

        Terminal terminal777 = new Terminal();
        terminal777.setId(TERMINAL_ID_777);
        terminal777.setName(TERMINAL_NAME_1);
        terminal777.setLatitude(BigDecimal.ONE);
        terminal777.setLongitude(BigDecimal.ONE);
        when(terminalServiceMock.getById(TERMINAL_ID_777)).thenReturn(terminal777);
        when(terminalServiceMock.save(any(Terminal.class))).thenReturn(terminal777);

        MockHttpServletRequestBuilder builder = put("/terminals/" + TERMINAL_ID_777
                + "?name=Entenhausen&latitude=1&longitude=1");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isFound());
        resultActions.andExpect(view().name("redirect:/web/terminals/" + TERMINAL_ID_777));
    }


    private Terminal createTerminal() {

        Terminal terminal = new Terminal();
        terminal.setId(2L);
        terminal.setLatitude(BigDecimal.ONE);
        terminal.setLongitude(BigDecimal.ONE);
        terminal.setName(TERMINAL_NAME_1);

        return terminal;
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
