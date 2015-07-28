package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.address.Address;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.contargo.iris.address.nominatim.service.AddressDetailKey.CITY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.COUNTRY;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.NAME;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.POSTAL_CODE;
import static net.contargo.iris.address.nominatim.service.AddressDetailKey.STREET;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;


/**
 * Test for Geo-coding (resolving addresses to geo coordinates) with {@link NominatimAddressService} using
 * http://nominatim.openstreetmap.org.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:application-context.xml")
public class NominatimAddressServiceIntegrationTest {

    @Autowired
    private AddressService addressService;

    @Test
    public void resolve() {

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(CITY.getKey(), "city");
        addressDetails.put(STREET.getKey(), "street");
        addressDetails.put(POSTAL_CODE.getKey(), "postalcode");
        addressDetails.put(NAME.getKey(), "name");
        addressDetails.put(COUNTRY.getKey(), "country");

        List<Address> addresses = addressService.getAddressesByDetails(addressDetails);

        assertThat(addresses, hasSize(greaterThanOrEqualTo(1)));
    }


    @Test
    public void resolveByName() {

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put(CITY.getKey(), "city");
        addressDetails.put(NAME.getKey(), "name");

        List<Address> addresses = addressService.getAddressesByDetails(addressDetails);

        assertThat(addresses, hasSize(greaterThanOrEqualTo(1)));
    }
}
