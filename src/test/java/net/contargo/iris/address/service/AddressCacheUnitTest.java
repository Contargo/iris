package net.contargo.iris.address.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;

import org.junit.Before;
import org.junit.Test;

import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;


/**
 * Unit test for {@link AddressCache}.
 *
 * @author  Vincent Potucek - potucek@synyx.de
 * @author  Tobias Schenider - schneider@synyx.de
 */
public class AddressCacheUnitTest {

    public static final BigDecimal DURLACH_LATITUDE = new BigDecimal(48.9970339131);
    public static final BigDecimal DURLACH_LONGITUDE = new BigDecimal(8.4693895567);
    public static final String CACHE_NAME = "cacheName";

    private AddressCache sut;

    private Address expectedAddress;
    private GeoLocation geoLocation;

    @Before
    public void setUp() throws Exception {

        expectedAddress = new Address();
        expectedAddress.setLatitude(DURLACH_LATITUDE);
        expectedAddress.setLongitude(DURLACH_LONGITUDE);

        geoLocation = new GeoLocation(DURLACH_LATITUDE, DURLACH_LONGITUDE);

        sut = new AddressCache(new ConcurrentMapCache(CACHE_NAME));
    }


    @Test
    public void testCache() {

        List<Address> addresses = new ArrayList<>();
        addresses.add(expectedAddress);

        List<AddressList> arrayList = new ArrayList<>();
        arrayList.add(new AddressList(CACHE_NAME, addresses));

        sut.cache(arrayList);

        Address actualAddress = sut.getForLocation(geoLocation);

        assertThat(expectedAddress, is(actualAddress));
    }


    @Test
    public void testCacheIsNull() {

        List<AddressList> arrayList = new ArrayList<>();
        arrayList.add(new AddressList(CACHE_NAME, new ArrayList<Address>()));

        sut.cache(arrayList);

        Address actualAddress = sut.getForLocation(geoLocation);

        assertThat(actualAddress, is(nullValue()));
    }
}
