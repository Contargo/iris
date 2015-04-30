package net.contargo.iris.address.api;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.AddressDtoService;
import net.contargo.iris.address.dto.AddressListDto;
import net.contargo.iris.address.nominatim.service.AddressDetailKey;

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * MVC Unit test of {@link AddressApiController}.
 *
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class AddressApiControllerMvcUnitTest {

    static final int OSM_ID = 12;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Autowired
    private AddressDtoService addressDtoServiceMock;

    @Before
    public void setUp() throws Exception {

        mockMvc = webAppContextSetup(webApplicationContext).build();

        Address address = new Address();
        address.setDisplayName("76137 Karlsruhe");
        address.setLatitude(BigDecimal.ONE);
        address.setLongitude(BigDecimal.ONE);

        AddressDto addressDto = new AddressDto(address);
        when(addressDtoServiceMock.getAddressByOsmId(AddressApiControllerMvcUnitTest.OSM_ID)).thenReturn(addressDto);
        when(addressDtoServiceMock.wrapInListOfAddressLists(any(AddressDto.class))).thenReturn(Arrays.asList(
                new AddressListDto("Result", Arrays.asList(addressDto))));
        when(addressDtoServiceMock.getAddressForGeoLocation(new GeoLocation(BigDecimal.ONE, BigDecimal.ONE)))
            .thenReturn(addressDto);

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(AddressDetailKey.CITY.getKey(), "Karlsruhe");
        addressDetails.put(AddressDetailKey.NAME.getKey(), "zkm");
        when(addressDtoServiceMock.getAddressesByDetails(addressDetails)).thenReturn(Arrays.asList(
                new AddressListDto("name1", Arrays.asList(addressDto))));
        when(addressDtoServiceMock.getAddressesByDetailsPlain(addressDetails)).thenReturn(Arrays.asList(addressDto));
        when(addressDtoServiceMock.getAddressesWherePlaceIsIn(1L)).thenReturn(Arrays.asList(addressDto));
    }


    @Test
    public void testAddressByOsmId() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/osmaddresses/" + OSM_ID).accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json"));
        resultActions.andExpect((jsonPath("$.geoCodeResponse.addresses[0].addresses[0].displayName",
                    is("76137 Karlsruhe"))));
        resultActions.andExpect((jsonPath("$.geoCodeResponse.links", empty())));
    }


    @Test
    public void testAddressByGeolocation() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(
                    "/reversegeocode/" + BigDecimal.ONE + ":" + BigDecimal.ONE + "/").accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json"));
        resultActions.andExpect((jsonPath("$.reverseGeocodeResponse.address.displayName", is("76137 Karlsruhe"))));
        resultActions.andExpect((jsonPath("$.reverseGeocodeResponse.links", hasSize(1))));
        resultActions.andExpect((jsonPath("$.reverseGeocodeResponse.links[0].rel", is("self"))));
        resultActions.andExpect((jsonPath("$.reverseGeocodeResponse.links[0].href",
                    is("http://localhost/reversegeocode/1:1/"))));
    }


    @Test
    public void testAddressByAddressDetails() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/geocodes/?city=Karlsruhe&name=zkm").accept(
                    APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json"));
        resultActions.andExpect((jsonPath("$.geoCodeResponse.addresses[0].addresses[0].displayName",
                    is("76137 Karlsruhe"))));
        resultActions.andExpect((jsonPath("$.geoCodeResponse.links", hasSize(1))));
        resultActions.andExpect((jsonPath("$.geoCodeResponse.links[0].rel", is("self"))));
        resultActions.andExpect((jsonPath("$.geoCodeResponse.links[0].href",
                    is("http://localhost/geocodes?city=Karlsruhe&name=zkm"))));
    }


    @Test
    public void testAddressByAddressDetailsPlain() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/simplegeocodes/?city=Karlsruhe&name=zkm").accept(
                    APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json"));
        resultActions.andExpect((jsonPath("$.simpleGeoCodeResponse.addresses[0].displayName", is("76137 Karlsruhe"))));
        resultActions.andExpect((jsonPath("$.simpleGeoCodeResponse.links", hasSize(1))));
        resultActions.andExpect((jsonPath("$.simpleGeoCodeResponse.links[0].rel", is("self"))));
        resultActions.andExpect((jsonPath("$.simpleGeoCodeResponse.links[0].href",
                    is("http://localhost/simplegeocodes?city=Karlsruhe&name=zkm"))));
    }


    @Test
    public void testAddressByPlaceId() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/places/1/addresses").accept(APPLICATION_JSON));

        resultActions.andExpect(status().isOk()).andExpect(content().contentType("application/json")).andExpect(
            (jsonPath("$.addresses[0].displayName", is("76137 Karlsruhe"))));
    }
}
