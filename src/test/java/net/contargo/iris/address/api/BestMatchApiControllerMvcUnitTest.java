package net.contargo.iris.address.api;

import net.contargo.iris.address.service.BestMatch;
import net.contargo.iris.address.service.BestMatchService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class BestMatchApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private BestMatchService bestMatchService;

    @Before
    public void setUp() {

        mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void returnsBestMatch() throws Exception {

        when(bestMatchService.bestMatch("72810", "Gomaringen", "DE")).thenReturn(Optional.of(new BestMatchDummy()));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/addresses/bestmatch")
                .param("postalcode", "72810")
                .param("city", "Gomaringen")
                .param("countrycode", "DE")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.hashKey", is("3948756")));
    }


    @Test
    public void noBestMatchFound() throws Exception {

        when(bestMatchService.bestMatch("72810", "Gomaringen", "DE")).thenReturn(Optional.empty());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/addresses/bestmatch")
                .param("postalcode", "72810")
                .param("city", "Gomaringen")
                .param("countrycode", "DE")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }

    private static class BestMatchDummy extends BestMatch {

        private BestMatchDummy() {

            super("3948756", ONE, TEN, "72810", "Gomaringen", "DE", "suburbName");
        }
    }
}
