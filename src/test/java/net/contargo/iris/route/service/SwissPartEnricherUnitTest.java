package net.contargo.iris.route.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.NominatimToStaticAddressMapper;
import net.contargo.iris.address.staticsearch.service.NominatimToStaticAddressMapperException;
import net.contargo.iris.route.RoutePart;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class SwissPartEnricherUnitTest {

    @Mock
    private NominatimToStaticAddressMapper mapperMock;

    private SwissPartEnricher sut;

    @Before
    public void setUp() {

        sut = new SwissPartEnricher(mapperMock);
    }


    @Test
    public void mapOrigin() throws CriticalEnricherException {

        Address origin = new SwissAddress();
        RoutePart routePart = new RoutePart();
        routePart.setOrigin(origin);

        StaticAddress mapped = new StaticAddress();
        mapped.setLatitude(BigDecimal.ONE);

        when(mapperMock.map(origin)).thenReturn(mapped);

        sut.enrich(routePart, null);

        assertThat(routePart.getOrigin().getLatitude(), comparesEqualTo(BigDecimal.ONE));
    }


    @Test
    public void mapDestination() throws CriticalEnricherException {

        Address destination = new SwissAddress();
        RoutePart routePart = new RoutePart();
        routePart.setDestination(destination);

        StaticAddress mapped = new StaticAddress();
        mapped.setLatitude(BigDecimal.ONE);

        when(mapperMock.map(destination)).thenReturn(mapped);

        sut.enrich(routePart, null);

        assertThat(routePart.getDestination().getLatitude(), comparesEqualTo(BigDecimal.ONE));
    }


    @Test
    public void unmappedNotSwitzerland() throws CriticalEnricherException {

        Address address = new Address();
        RoutePart routePart = new RoutePart();
        routePart.setOrigin(address);
        routePart.setDestination(address);

        sut.enrich(routePart, null);

        verifyZeroInteractions(mapperMock);

        assertThat(routePart.getOrigin(), is(address));
        assertThat(routePart.getDestination(), is(address));
    }


    @Test
    public void unmappedStaticSwissAddress() throws CriticalEnricherException {

        Address address = new SwissStaticAddress();
        RoutePart routePart = new RoutePart();
        routePart.setOrigin(address);
        routePart.setDestination(address);

        sut.enrich(routePart, null);

        verifyZeroInteractions(mapperMock);

        assertThat(routePart.getOrigin(), is(address));
        assertThat(routePart.getDestination(), is(address));
    }


    @Test
    public void unmappedNonLoadingPlace() throws CriticalEnricherException {

        GeoLocation geoLocation = new GeoLocation();
        RoutePart routePart = new RoutePart();
        routePart.setOrigin(geoLocation);
        routePart.setDestination(geoLocation);

        sut.enrich(routePart, null);

        verifyZeroInteractions(mapperMock);

        assertThat(routePart.getOrigin(), is(geoLocation));
        assertThat(routePart.getDestination(), is(geoLocation));
    }


    @Test
    public void unmappedOriginWithMappingException() throws CriticalEnricherException {

        Address origin = new SwissAddress();
        RoutePart routePart = new RoutePart();
        routePart.setOrigin(origin);

        when(mapperMock.map(origin)).thenThrow(new NominatimToStaticAddressMapperException(origin));

        EnricherContext context = new EnricherContext.Builder().build();
        sut.enrich(routePart, context);

        assertThat(context.getErrors().get("swiss-route"), is("could not map from nominatim to static address"));
    }


    @Test
    public void unmappedDestincationWithMappingException() throws CriticalEnricherException {

        Address destination = new SwissAddress();
        RoutePart routePart = new RoutePart();
        routePart.setDestination(destination);

        when(mapperMock.map(destination)).thenThrow(new NominatimToStaticAddressMapperException(destination));

        EnricherContext context = new EnricherContext.Builder().build();
        sut.enrich(routePart, context);

        assertThat(context.getErrors().get("swiss-route"), is("could not map from nominatim to static address"));
    }

    class SwissAddress extends Address {

        @Override
        public String getCountryCode() {

            return "CH";
        }


        @Override
        public boolean isStatic() {

            return false;
        }
    }

    class SwissStaticAddress extends Address {

        @Override
        public String getCountryCode() {

            return "CH";
        }


        @Override
        public boolean isStatic() {

            return true;
        }
    }
}
