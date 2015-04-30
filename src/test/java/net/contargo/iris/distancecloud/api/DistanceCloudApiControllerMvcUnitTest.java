package net.contargo.iris.distancecloud.api;

import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.distancecloud.DistanceCloudAddress;
import net.contargo.iris.distancecloud.dto.DistanceCloudAddressDto;
import net.contargo.iris.distancecloud.dto.DistanceCloudAddressDtoService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * MVC Unit test of {@link net.contargo.iris.distancecloud.api.DistanceCloudApiController}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class DistanceCloudApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private DistanceCloudAddressDto distanceCloudAddressDto1;
    private MockMvc mockMvc;

    @Autowired
    DistanceCloudAddressDtoService distanceCloudAddressDtoServiceMock;

    @Before
    public void setUp() throws Exception {

        mockMvc = webAppContextSetup(webApplicationContext).build();

        StaticAddress staticAddress1 = new StaticAddress();
        staticAddress1.setCity("city1");

        DistanceCloudAddress distanceCloudAddress1 = new DistanceCloudAddress(staticAddress1);
        distanceCloudAddress1.setDistance(new BigDecimal("24"));

        distanceCloudAddressDto1 = new DistanceCloudAddressDto(distanceCloudAddress1);
    }


    @Test
    public void distanceCloudAddress() throws Exception {

        when(distanceCloudAddressDtoServiceMock.getAddressInCloud(BigInteger.ONE, BigInteger.TEN)).thenReturn(
            distanceCloudAddressDto1);

        MockHttpServletRequestBuilder builder = get("/distancecloudaddress");
        builder.param("terminal", BigInteger.ONE.toString());
        builder.param("address", BigInteger.TEN.toString());
        builder.accept(APPLICATION_JSON);

        ResultActions resultActions = mockMvc.perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json;charset=UTF-8"));

        // some random sampling
        resultActions.andExpect((jsonPath("$.address.city", is(distanceCloudAddressDto1.getCity()))));
        resultActions.andExpect((jsonPath("$.address.distance", is(24))));
    }
}
