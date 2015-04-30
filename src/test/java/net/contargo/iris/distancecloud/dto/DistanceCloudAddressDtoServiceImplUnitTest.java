package net.contargo.iris.distancecloud.dto;

import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.distancecloud.DistanceCloudAddress;
import net.contargo.iris.distancecloud.service.DistanceCloudAddressService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class DistanceCloudAddressDtoServiceImplUnitTest {

    private DistanceCloudAddressDtoServiceImpl sut;

    @Mock
    private DistanceCloudAddressService distanceCloudAddressServiceMock;

    private DistanceCloudAddress address1;

    @Before
    public void setUp() {

        sut = new DistanceCloudAddressDtoServiceImpl(distanceCloudAddressServiceMock);

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setCity("city");
        staticAddress.setCountry("country");
        staticAddress.setPostalcode("postalCode");
        staticAddress.setSuburb("suburb");
        staticAddress.setUniqueId(new BigInteger("1301000000000001"));

        address1 = new DistanceCloudAddress(staticAddress);
        address1.setDistance(BigDecimal.ONE);
        address1.setTollDistance(BigDecimal.ONE);
        address1.setErrorMessage("errorMessage");
        address1.setHashKey("hashKey");
        address1.setAirLineDistanceMeter(BigDecimal.TEN);
    }


    @Test
    public void getAddressInCloud() {

        when(distanceCloudAddressServiceMock.getAddressInCloud(BigInteger.ONE, BigInteger.TEN)).thenReturn(address1);

        DistanceCloudAddressDto address = sut.getAddressInCloud(BigInteger.ONE, BigInteger.TEN);

        assertCloudAddress(address);
    }


    private void assertCloudAddress(DistanceCloudAddressDto address) {

        assertThat(address.getCity(), is(address1.getCity()));
        assertThat(address.getDistance(), is(address1.getDistance()));
        assertThat(address.getTollDistance(), is(address1.getTollDistance()));
        assertThat(address.getCountry(), is(address1.getCountry()));
        assertThat(address.getErrorMessage(), is(address1.getErrorMessage()));
        assertThat(address.getHashKey(), is(address1.getHashKey()));
        assertThat(address.getPostalcode(), is(address1.getPostalcode()));
        assertThat(address.getSuburb(), is(address1.getSuburb()));
        assertThat(address.getUniqueId(), is(String.valueOf(address1.getUniqueId())));
        assertThat(address.getAirLineDistanceMeter(), is(address1.getAirLineDistanceMeter()));
    }
}
