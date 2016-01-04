package net.contargo.iris.distancecloud.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.persistence.StaticAddressRepository;
import net.contargo.iris.distancecloud.DistanceCloudAddress;
import net.contargo.iris.gis.service.GisService;
import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;


/**
 * Unit tests for {@link DistanceCloudAddressServiceImpl}.
 *
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class DistanceCloudAddressServiceImplUnitTest {

    private static final BigInteger STATIC_ADDRESS_UID = new BigInteger("1301000000000001");
    private static final BigInteger TERMINAL_UID = BigInteger.ONE;
    private static final String KARLSRUHE = "Karlsruhe";
    private static final String WESTSTADT = "Weststadt";
    private static final String POSTAL_CODE = "76137";

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
    @Mock
    private RouteDataRevisionService routeDataRevisionServiceMock;

    private Terminal terminal;
    private StaticAddress staticAddress;

    @Before
    public void setup() {

        terminal = new Terminal();
        staticAddress = createAddressForCloudTest(ONE, ONE);

        sut = new DistanceCloudAddressServiceImpl(truckRouteServiceMock, repositoryMock, roundingServiceMock,
                gisServiceMock, terminalServiceMock, routeDataRevisionServiceMock);
    }


    @Test
    public void getAddressInCloudByMaps() {

        when(terminalServiceMock.getByUniqueId(TERMINAL_UID)).thenReturn(terminal);
        when(repositoryMock.findByUniqueId(STATIC_ADDRESS_UID)).thenReturn(staticAddress);

        when(routeDataRevisionServiceMock.getRouteDataRevision(terminal, staticAddress)).thenReturn(null);
        when(truckRouteServiceMock.route(terminal, staticAddress)).thenReturn(new TruckRoute(ZERO, ONE, TEN));
        when(gisServiceMock.calcAirLineDistInMeters(terminal, staticAddress)).thenReturn(TEN);
        when(roundingServiceMock.roundDistance(ZERO)).thenReturn(ZERO);
        when(roundingServiceMock.roundDistance(ONE)).thenReturn(ONE);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(TEN);

        DistanceCloudAddress cloudAddress = sut.getAddressInCloud(TERMINAL_UID, STATIC_ADDRESS_UID);
        assertThat(cloudAddress.getDistance(), is(ZERO));
        assertThat(cloudAddress.getTollDistance(), is(new BigDecimal("2")));
        assertThat(cloudAddress.getAirLineDistanceMeter(), is(TEN));
        assertThat(cloudAddress.getCity(), is(KARLSRUHE));
        assertThat(cloudAddress.getSuburb(), is(WESTSTADT));
        assertThat(cloudAddress.getPostalcode(), is(POSTAL_CODE));
        assertThat(cloudAddress.getUniqueId(), is(STATIC_ADDRESS_UID));
        assertThat(cloudAddress.getErrorMessage(), isEmptyString());
    }


    @Test
    public void getAddressInCloudByRouteRevision() {

        when(terminalServiceMock.getByUniqueId(TERMINAL_UID)).thenReturn(terminal);
        when(repositoryMock.findByUniqueId(STATIC_ADDRESS_UID)).thenReturn(staticAddress);

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        routeDataRevision.setTruckDistanceOneWayInKilometer(ONE);
        routeDataRevision.setTollDistanceOneWayInKilometer(TEN);
        routeDataRevision.setAirlineDistanceInKilometer(ZERO);
        when(routeDataRevisionServiceMock.getRouteDataRevision(terminal, staticAddress)).thenReturn(routeDataRevision);

        DistanceCloudAddress cloudAddress = sut.getAddressInCloud(TERMINAL_UID, STATIC_ADDRESS_UID);
        assertThat(cloudAddress.getDistance(), is(ONE));
        assertThat(cloudAddress.getTollDistance(), is(new BigDecimal("20")));
        assertThat(cloudAddress.getAirLineDistanceMeter(), is(ZERO));
        assertThat(cloudAddress.getCity(), is(KARLSRUHE));
        assertThat(cloudAddress.getSuburb(), is(WESTSTADT));
        assertThat(cloudAddress.getPostalcode(), is(POSTAL_CODE));
        assertThat(cloudAddress.getUniqueId(), is(STATIC_ADDRESS_UID));
        assertThat(cloudAddress.getErrorMessage(), isEmptyString());
    }


    @Test
    public void getAddressInCloudWithError() {

        when(terminalServiceMock.getByUniqueId(TERMINAL_UID)).thenReturn(terminal);
        when(repositoryMock.findByUniqueId(STATIC_ADDRESS_UID)).thenReturn(staticAddress);

        doThrow(OSRMNonRoutableRouteException.class).when(truckRouteServiceMock)
            .route(any(GeoLocation.class), any(GeoLocation.class));

        DistanceCloudAddress cloudAddress = sut.getAddressInCloud(TERMINAL_UID, STATIC_ADDRESS_UID);

        assertThat(cloudAddress.getErrorMessage(), is("Routing not possible"));

        verifyZeroInteractions(roundingServiceMock);
        verifyZeroInteractions(gisServiceMock);
    }


    private StaticAddress createAddressForCloudTest(BigDecimal lat, BigDecimal lon) {

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setId(42L);
        staticAddress.setLatitude(lat);
        staticAddress.setLongitude(lon);
        staticAddress.setCity(KARLSRUHE);
        staticAddress.setSuburb(WESTSTADT);
        staticAddress.setPostalcode(POSTAL_CODE);
        staticAddress.setUniqueId(STATIC_ADDRESS_UID);

        return staticAddress;
    }
}
