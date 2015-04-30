package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.address.Address;

import org.junit.Test;

import java.math.BigDecimal;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasItem;

import static java.util.Arrays.asList;


/**
 * Unit test of {@link AddressServiceHelper}.
 *
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class AddressServiceHelperUnitTest {

    @Test
    public void mergeSearchResultsWithoutDuplications() {

        Address address1 = new Address();
        address1.setLatitude(BigDecimal.ZERO);
        address1.setLongitude(BigDecimal.ZERO);

        Address address2 = new Address();
        address2.setLatitude(BigDecimal.ONE);
        address2.setLongitude(BigDecimal.ONE);

        Address address3 = new Address(); // equals address1
        address3.setLatitude(BigDecimal.ZERO);
        address3.setLongitude(BigDecimal.ZERO);

        Address address4 = new Address(); // equals address2
        address4.setLatitude(BigDecimal.ONE);
        address4.setLongitude(BigDecimal.ONE);

        AddressServiceHelper sut = new AddressServiceHelper();

        List<Address> mergeResult = sut.mergeSearchResultsWithoutDuplications(asList(address1, address2),
                asList(address3, address4));

        assertThat(mergeResult, hasItem(address2));
        assertThat(mergeResult, hasItem(address1));

        mergeResult = sut.mergeSearchResultsWithoutDuplications(asList(address3, address4), asList(address1, address2));
        assertThat(mergeResult, hasItem(address2));
        assertThat(mergeResult, hasItem(address1));
    }
}
