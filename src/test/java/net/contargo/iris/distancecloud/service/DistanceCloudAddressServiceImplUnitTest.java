package net.contargo.iris.distancecloud.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.persistence.StaticAddressRepository;
import net.contargo.iris.distancecloud.DistanceCloudAddress;
import net.contargo.iris.gis.service.GisService;
import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.OSRMNonRoutableRouteException;
import net.contargo.iris.truck.service.TruckRouteService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link DistanceCloudAddressServiceImpl}.
 *
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class DistanceCloudAddressServiceImplUnitTest {

    private DistanceCloudAddressServiceImpl sut;

    @Mock
    private StaticAddressRepository repositoryMock;
    @Mock
    private TruckRouteService truckRouteServiceMock;
    @Mock
    private GisService gisServiceMock;
    @Mock
    private RoundingService roundingServiceMock;
    @Mock
    private TerminalService terminalServiceMock;

    private BigDecimal distance = BigDecimal.ONE;
    private BigDecimal tollDistance = BigDecimal.ONE;
    private BigDecimal airLineDistance = BigDecimal.ONE;
    private StaticAddress staticAddress;
    private BigInteger uniqueId = new BigInteger("1301000000000001");

    @Before
    public void setup() {

        staticAddress = createAddressForCloudTest(BigDecimal.ONE, BigDecimal.ONE);

        sut = new DistanceCloudAddressServiceImpl(truckRouteServiceMock, repositoryMock, roundingServiceMock,
                gisServiceMock, terminalServiceMock);
    }


    private StaticAddress createAddressForCloudTest(BigDecimal lat, BigDecimal lon) {

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setId(42L);
        staticAddress.setLatitude(lat);
        staticAddress.setLongitude(lon);
        staticAddress.setCity("Karlsruhe");
        staticAddress.setSuburb("Weststadt");
        staticAddress.setPostalcode("76137");
        staticAddress.setCountry("DE");
        staticAddress.setUniqueId(uniqueId);

        return staticAddress;
    }


    @Test
    public void getAddressInCloud() {

        Terminal terminalMock = mock(Terminal.class);

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminalMock);
        when(repositoryMock.findByUniqueId(uniqueId)).thenReturn(staticAddress);
        when(truckRouteServiceMock.route(any(GeoLocation.class), any(GeoLocation.class))).thenReturn(new TruckRoute(
                distance, tollDistance, null));
        when(gisServiceMock.calcAirLineDistInMeters(any(GeoLocation.class), any(GeoLocation.class))).thenReturn(
            airLineDistance);
        when(roundingServiceMock.roundDistance(distance)).thenReturn(BigDecimal.TEN);
        when(roundingServiceMock.roundDistance(tollDistance)).thenReturn(BigDecimal.TEN);
        when(roundingServiceMock.roundDistance(airLineDistance)).thenReturn(BigDecimal.TEN);

        DistanceCloudAddress cloudAddress = sut.getAddressInCloud(BigInteger.ONE, uniqueId);

        assertThat(cloudAddress.getDistance(), comparesEqualTo(BigDecimal.TEN));
        assertThat(cloudAddress.getTollDistance(), comparesEqualTo(new BigDecimal("20")));
        assertThat(cloudAddress.getAirLineDistanceMeter(), comparesEqualTo(BigDecimal.TEN));
        assertThat(cloudAddress.getCity(), is("Karlsruhe"));
        assertThat(cloudAddress.getSuburb(), is("Weststadt"));
        assertThat(cloudAddress.getPostalcode(), is("76137"));
        assertThat(cloudAddress.getErrorMessage(), isEmptyString());
        assertThat(cloudAddress.getUniqueId(), is(uniqueId));
    }


    @Test
    public void getAddressInCloudWithError() {

        Terminal terminalMock = mock(Terminal.class);

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminalMock);
        when(repositoryMock.findByUniqueId(uniqueId)).thenReturn(staticAddress);

        doThrow(OSRMNonRoutableRouteException.class).when(truckRouteServiceMock).route(any(GeoLocation.class),
            any(GeoLocation.class));

        DistanceCloudAddress cloudAddress = sut.getAddressInCloud(BigInteger.ONE, uniqueId);

        assertThat(cloudAddress.getErrorMessage(), is("Routing not possible"));

        verifyZeroInteractions(roundingServiceMock);
        verifyZeroInteractions(gisServiceMock);
    }
}
