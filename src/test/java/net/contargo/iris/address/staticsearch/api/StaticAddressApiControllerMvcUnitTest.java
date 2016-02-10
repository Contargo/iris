package net.contargo.iris.address.staticsearch.api;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.staticsearch.dto.StaticAddressDtoService;

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
import java.math.BigInteger;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class StaticAddressApiControllerMvcUnitTest {

    private static final String CITY = "Karlsruhe";
    private static final String POSTALCODE = "76131";
    private static final String COUNTRY = "DE";
    private static final String SUBURB = "Neureuth";

    @Autowired
    StaticAddressDtoService staticAddressDtoServiceMock;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void before() {

        mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void getByPostalCodeAndCityAndCountry() throws Exception {

        Map<String, String> addressMap = new HashMap<>();
        addressMap.put("city", CITY);
        addressMap.put("country_code", COUNTRY);
        addressMap.put("postcode", POSTALCODE);
        addressMap.put("suburb", null);

        Address address = new Address();
        address.setLatitude(BigDecimal.ONE);
        address.setLongitude(BigDecimal.ONE);
        address.setAddress(addressMap);

        AddressDto dto = new AddressDto(address);

        when(staticAddressDtoServiceMock.getAddressesByDetails("76131", "Karlsruhe", "DE")).thenReturn(singletonList(
                dto));

        ResultActions resultActions = mockMvc.perform(get(
                    "/staticaddresses?postalCode=76131&country=DE&city=Karlsruhe").accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
        resultActions.andExpect((jsonPath("$.[0].address.country_code", is(COUNTRY))));
        resultActions.andExpect((jsonPath("$.[0].address.city", is(CITY))));
        resultActions.andExpect((jsonPath("$.[0].address.postcode", is(POSTALCODE))));
    }


    @Test
    public void staticAddressesByBoundingBox() throws Exception {

        when(staticAddressDtoServiceMock.getStaticAddressByBoundingBox(any(GeoLocation.class), eq(20d))).thenReturn(
            asList(BigInteger.ONE, BigInteger.TEN));

        ResultActions resultActions = mockMvc.perform(get("/staticaddresses").param("lat", "10")
                .param("lon", "1")
                .param("distance", "20")
                .accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
        resultActions.andExpect((jsonPath("$.uids", hasSize(2))));
        resultActions.andExpect((jsonPath("$.uids[0]", is(1))));
        resultActions.andExpect((jsonPath("$.uids[1]", is(10))));
    }


    @Test
    public void getByHashKey() throws Exception {

        String hashKey = "ABCDE";
        String displayName = "displayName";
        AddressDto addressDto = new AddressDto(new Address(displayName));
        when(staticAddressDtoServiceMock.getStaticAddressByHashKey(hashKey)).thenReturn(addressDto);

        ResultActions resultActions = mockMvc.perform(get("/staticaddresses").param("hashkey", hashKey)
                .accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect((jsonPath("$.displayName", is(displayName))));
    }
}
