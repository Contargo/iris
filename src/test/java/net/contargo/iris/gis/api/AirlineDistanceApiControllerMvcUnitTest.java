package net.contargo.iris.gis.api;

import net.contargo.iris.gis.dto.AirlineDistanceDto;
import net.contargo.iris.gis.dto.AirlineDistanceDtoService;

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

import java.math.BigDecimal;

import static net.contargo.iris.gis.dto.AirlineDistanceUnit.METER;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class AirlineDistanceApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    private AirlineDistanceDtoService airlineDistanceDtoServiceMock;

    @Before
    public void before() {

        mockMvc = webAppContextSetup(webApplicationContext).build();
        when(airlineDistanceDtoServiceMock.calcAirLineDistInMeters("1.1", "1.2", "2.1", "2.2")).thenReturn(
            new AirlineDistanceDto(new BigDecimal("7.89"), METER, "?alat=1.1&alon=1.2&blat=2.1&blon=2.2"));
    }


    @Test
    public void airlineDistance() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/airlineDistance?alat=1.1&alon=1.2&blat=2.1&blon=2.2")
                .accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk()).andExpect(content().contentType("application/json"));

        resultActions.andExpect(jsonPath("$.airlineDistance.distance", comparesEqualTo(7.89)));
        resultActions.andExpect(jsonPath("$.airlineDistance.unit", is("meter")));

        resultActions.andExpect(jsonPath("$.airlineDistance.links[0].rel", is("self")));
        resultActions.andExpect(jsonPath("$.airlineDistance.links[0].href",
                is("http://localhost/airlineDistance?alat=1.1&alon=1.2&blat=2.1&blon=2.2")));
    }


    @Test
    public void badRequestOnInvalidGeoCoordinates() throws Exception {

        // invalid latitude
        mockMvc.perform(get("/airlineDistance?alat=-90.5&alon=1.2&blat=2.1&blon=2.2").accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
        mockMvc.perform(get("/airlineDistance?alat=1.1&alon=1.2&blat=91.5&blon=2.2").accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        // invalid longitudes
        mockMvc.perform(get("/airlineDistance?alat=1.1&alon=-180.5&blat=2.1&blon=2.2").accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
        mockMvc.perform(get("/airlineDistance?alat=1.1&alon=1.2&blat=2.1&blon=180.5").accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        // missing request parameters
        mockMvc.perform(get("/airlineDistance?").accept(APPLICATION_JSON)).andExpect(status().isBadRequest());
    }
}
