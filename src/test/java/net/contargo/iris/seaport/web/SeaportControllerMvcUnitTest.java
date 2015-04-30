package net.contargo.iris.seaport.web;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.ui.ModelMap;

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
 * MVC Unit test of {@link net.contargo.iris.seaport.web.SeaportController}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-api-context.xml" })
@WebAppConfiguration
public class SeaportControllerMvcUnitTest {

    private static final int SEAPORT_ID = 2;
    public static final long SEAPORT_ID_777 = 777L;
    public static final String SEAPORT_NAME_1 = "Entenhausen";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SeaportService seaportServiceMock;

    private Seaport seaport1;
    private Seaport seaport2;

    @Before
    public void setUp() throws Exception {

        seaport1 = createSeaport();
        seaport2 = createSeaport();
        seaport2.setName("Gotham City");
        seaport2.setId(3L);
        seaport2.setLatitude(BigDecimal.TEN);
        seaport2.setLongitude(BigDecimal.TEN);
    }


    @Test
    public void testGetAllSeaports() throws Exception {

        when(seaportServiceMock.getAll()).thenReturn(Arrays.asList(seaport1, seaport2));

        ResultActions resultActions = perform(get("/seaports/").accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("seaports", Arrays.asList(seaport1, seaport2)));
        resultActions.andExpect(view().name("seaportManagement/seaports"));
    }


    @Test
    public void testCreateSeaport() throws Exception {

        ResultActions resultActions = perform(get("/seaports/new").accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(view().name("seaportManagement/seaport"));
    }


    @Test
    public void testSaveSeaport() throws Exception {

        String requestBody = new ObjectMapper().writeValueAsString(seaport1);
        when(seaportServiceMock.save(any(Seaport.class))).thenReturn(seaport1);

        MockHttpServletRequestBuilder builder = post("/seaports/?name=Entenhausen&latitude=1&longitude=1");
        builder.accept(APPLICATION_JSON);
        builder.contentType(APPLICATION_JSON);
        builder.content(requestBody);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isFound());
        resultActions.andExpect(view().name("redirect:/web/seaports/" + seaport1.getId()));
    }


    @Test
    public void testSaveIncompleteSeaport() throws Exception {

        Seaport incompleteSeaport = new Seaport();
        incompleteSeaport.setName(SEAPORT_NAME_1);
        incompleteSeaport.setLatitude(BigDecimal.ONE);

        String requestBody = new ObjectMapper().writeValueAsString(seaport1);
        when(seaportServiceMock.save(seaport1)).thenReturn(seaport1);

        MockHttpServletRequestBuilder builder = post("/seaports/?name=Entenhausen&latitude=1");
        builder.accept(APPLICATION_JSON);
        builder.contentType(APPLICATION_JSON);
        builder.content(requestBody);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(view().name("seaportManagement/seaport"));

        ModelAndView modelAndView = resultActions.andReturn().getModelAndView();
        ModelMap modelMap = modelAndView.getModelMap();
        assertReflectionEquals(modelMap.get("seaport"), incompleteSeaport);
    }


    @Test
    public void testEditSeaport() throws Exception {

        when(seaportServiceMock.getById(2L)).thenReturn(seaport1);

        ResultActions resultActions = perform(get("/seaports/" + SEAPORT_ID).accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("seaport", seaport1));
        resultActions.andExpect(view().name("seaportManagement/seaport"));
    }


    @Test
    public void testUpdateSeaport() throws Exception {

        Seaport seaport777 = new Seaport();
        seaport777.setId(SEAPORT_ID_777);
        seaport777.setName(SEAPORT_NAME_1);
        seaport777.setLatitude(BigDecimal.ONE);
        seaport777.setLongitude(BigDecimal.ONE);
        when(seaportServiceMock.getById(SEAPORT_ID_777)).thenReturn(seaport777);
        when(seaportServiceMock.save(any(Seaport.class))).thenReturn(seaport777);

        MockHttpServletRequestBuilder builder = put("/seaports/" + SEAPORT_ID_777
                + "?name=Entenhausen&latitude=1&longitude=1");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isFound());
        resultActions.andExpect(view().name("redirect:/web/seaports/" + SEAPORT_ID_777));
    }


    private Seaport createSeaport() {

        Seaport seaport = new Seaport();
        seaport.setId(2L);
        seaport.setLatitude(BigDecimal.ONE);
        seaport.setLongitude(BigDecimal.ONE);
        seaport.setName(SEAPORT_NAME_1);

        return seaport;
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
