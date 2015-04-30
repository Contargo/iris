package net.contargo.iris.web;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:web-api-context.xml" })
public class RootControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {

        mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void index() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/"));
        resultActions.andExpect(view().name("index"));
        resultActions.andExpect(status().isOk());
    }


    @Test
    public void triangle() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/triangle/"));
        resultActions.andExpect(view().name("routing/triangle"));
        resultActions.andExpect(status().isOk());
    }
}
