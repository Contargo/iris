package net.contargo.iris.address.api;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.AddressDtoService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Collections.singletonList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class SimpleAddressApiControllerUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private AddressDtoService addressDtoServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {

        mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void getAddresses() throws Exception {

        Address address = new Address();
        address.setDisplayName("Gartenstr. 67, Karlsruhe (Südweststadt)");

        when(addressDtoServiceMock.getAddressesByQuery("Gartenstraße 67, Karlsruhe")).thenReturn(singletonList(
                new AddressDto(address)));

        mockMvc.perform(get("/addresses/simpleaddress").param("query", "Gartenstraße 67, Karlsruhe"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.addressDtoList", hasSize(1)))
            .andExpect(jsonPath("$.addressDtoList[0].displayName", is("Gartenstr. 67, Karlsruhe (Südweststadt)")));
    }
}
