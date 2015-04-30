package net.contargo.iris.enricher.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.nominatim.service.AddressResolutionException;
import net.contargo.iris.location.GeoLocationService;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Unit test of {@link net.contargo.iris.enricher.service.GeoLocationPartEnricher}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class GeoLocationPartEnricherUnitTest {

    private GeoLocationPartEnricher sut;

    @Mock
    private GeoLocationService geoLocationServiceMock;
    @Mock
    private RoutePart routePartMock;

    // This is no mock because it is final
    private EnricherContext enricherContext;
    private GeoLocation x;
    private GeoLocation y;

    @Before
    public void setup() {

        x = new GeoLocation();
        y = new GeoLocation();

        when(routePartMock.getOrigin()).thenReturn(x);
        when(routePartMock.getDestination()).thenReturn(y);

        sut = new GeoLocationPartEnricher(geoLocationServiceMock);

        enricherContext = new EnricherContext.Builder().build();
    }


    @Test
    public void testDelegation() throws CriticalEnricherException {

        Terminal t = new Terminal();
        Terminal t2 = new Terminal();

        when(geoLocationServiceMock.getDetailedGeoLocation(x)).thenReturn(t);
        when(geoLocationServiceMock.getDetailedGeoLocation(y)).thenReturn(t2);

        sut.enrich(routePartMock, enricherContext);

        verify(routePartMock).setOrigin(t);
        verify(routePartMock).setDestination(t2);
    }


    @Test(expected = CriticalEnricherException.class)
    public void enrichWithCriticalException() throws CriticalEnricherException {

        when(geoLocationServiceMock.getDetailedGeoLocation(x)).thenThrow(new AddressResolutionException("",
                new Throwable()));

        sut.enrich(routePartMock, enricherContext);
    }
}
