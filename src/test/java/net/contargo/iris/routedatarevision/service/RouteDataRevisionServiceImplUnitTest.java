package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.service.AddressServiceWrapper;
import net.contargo.iris.normalizer.NormalizerService;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.RouteRevisionRequest;
import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.routedatarevision.persistence.RouteDataRevisionRepository;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.joda.time.DateTime;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.time.LocalDate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * Unit test of {@link RouteDataRevisionServiceImpl}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteDataRevisionServiceImplUnitTest {

    private RouteDataRevisionService sut;

    @Mock
    private RouteDataRevisionRepository routeDataRevisionRepositoryMock;
    @Mock
    private TerminalService terminalServiceMock;
    @Mock
    private AddressServiceWrapper addressServiceWrapperMock;
    @Mock
    private NormalizerService normalizerServiceMock;

    private Terminal terminal;

    @Before
    public void before() {

        sut = new RouteDataRevisionServiceImpl(routeDataRevisionRepositoryMock, terminalServiceMock,
                addressServiceWrapperMock, normalizerServiceMock);
        terminal = new Terminal();
    }


    @Test
    public void getRouteDataRevisionByTerminalAndGeolocation() {

        Address address = new Address(BigDecimal.ONE, BigDecimal.TEN);
        RouteDataRevision routeDataRevisionDB = new RouteDataRevision();

        when(routeDataRevisionRepositoryMock.findNearest(eq(terminal), eq(address.getLatitude()),
                    eq(address.getLongitude()), any(Date.class))).thenReturn(routeDataRevisionDB);

        RouteDataRevision routeDataRevision = sut.getRouteDataRevision(terminal, address);

        assertThat(routeDataRevision, is(routeDataRevisionDB));
    }


    @Test
    public void getRouteDataRevisionByTerminalUidAndGeolocataion() {

        Address address = new Address(BigDecimal.ONE, BigDecimal.TEN);
        RouteDataRevision routeDataRevisionDB = new RouteDataRevision();

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);

        Date date = new Date();
        when(routeDataRevisionRepositoryMock.findNearest(eq(terminal), eq(address.getLatitude()),
                    eq(address.getLongitude()), eq(date))).thenReturn(routeDataRevisionDB);

        RouteDataRevision routeDataRevision = sut.getRouteDataRevision(BigInteger.ONE, address, date);

        assertThat(routeDataRevision, is(routeDataRevisionDB));
    }


    @Test
    public void getRouteDataRevisionByTerminalUidAndGeolocataionWithoutDate() {

        Address address = new Address(BigDecimal.ONE, BigDecimal.TEN);
        RouteDataRevision routeDataRevisionDB = new RouteDataRevision();

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);

        when(routeDataRevisionRepositoryMock.findNearest(eq(terminal), eq(address.getLatitude()),
                    eq(address.getLongitude()), any(Date.class))).thenReturn(routeDataRevisionDB);

        RouteDataRevision routeDataRevision = sut.getRouteDataRevision(BigInteger.ONE, address, null);

        assertThat(routeDataRevision, is(routeDataRevisionDB));
    }


    @Test(expected = RevisionDoesNotExistException.class)
    public void getRouteDataRevisionByTerminalUidAndGeolocataionWithoutDateWithoutRevision() {

        Address address = new Address(BigDecimal.ONE, BigDecimal.TEN);

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);

        when(routeDataRevisionRepositoryMock.findNearest(eq(terminal), eq(address.getLatitude()),
                    eq(address.getLongitude()), any(Date.class))).thenReturn(null);

        sut.getRouteDataRevision(BigInteger.ONE, address, null);
    }


    @Test
    public void getNoRouteDataRevisionByTerminalUidAndGeolocataion() {

        GeoLocation address = new GeoLocation(BigDecimal.ONE, BigDecimal.TEN);

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);

        Date date = new Date();
        when(routeDataRevisionRepositoryMock.findNearest(eq(terminal), eq(address.getLatitude()),
                    eq(address.getLongitude()), eq(date))).thenReturn(null);

        try {
            sut.getRouteDataRevision(BigInteger.ONE, new Address(BigDecimal.ONE, BigDecimal.TEN), date);
        } catch (RevisionDoesNotExistException e) {
            assertThat(e.getCode(), is("routerevision.notfound"));
        }
    }


    @Test
    public void getNoTerminal() {

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(null);

        try {
            sut.getRouteDataRevision(BigInteger.ONE, new Address(BigDecimal.ONE, BigDecimal.TEN), new Date());
        } catch (RevisionDoesNotExistException e) {
            assertThat(e.getCode(), is("terminal.error.notfound"));
        }
    }


    @Test
    public void getRouteDataRevision() {

        List<RouteDataRevision> routeDataRevisions = singletonList(new RouteDataRevision());
        when(routeDataRevisionRepositoryMock.findAll()).thenReturn(routeDataRevisions);

        List<RouteDataRevision> resultList = sut.getRouteDataRevisions();
        assertThat(resultList, is(routeDataRevisions));
    }


    @Test
    public void getRouteDataRevisionByTerminal() {

        List<RouteDataRevision> routeDataRevisions = singletonList(new RouteDataRevision());
        when(routeDataRevisionRepositoryMock.findByTerminalId(1L)).thenReturn(routeDataRevisions);

        List<RouteDataRevision> resultList = sut.getRouteDataRevisions(1L);
        assertThat(resultList, is(routeDataRevisions));
    }


    @Test
    public void getOne() {

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        when(routeDataRevisionRepositoryMock.findOne(5L)).thenReturn(routeDataRevision);

        RouteDataRevision result = sut.getRouteDataRevision(5L);
        assertThat(result, is(routeDataRevision));
    }


    @Test
    public void save() {

        BigDecimal lat = BigDecimal.TEN;
        BigDecimal lon = BigDecimal.ONE;

        Address address = new Address();
        address.getAddress().put("village", "Ehningen");
        address.getAddress().put("country_code", "DE");
        address.getAddress().put("postcode", "66666");

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        routeDataRevision.setId(5L);
        routeDataRevision.setLatitude(lat);
        routeDataRevision.setLongitude(lon);

        when(routeDataRevisionRepositoryMock.save(routeDataRevision)).thenReturn(routeDataRevision);
        when(routeDataRevisionRepositoryMock.findOne(5L)).thenReturn(routeDataRevision);
        when(addressServiceWrapperMock.getAddressForGeoLocation(new GeoLocation(lat, lon))).thenReturn(address);
        when(normalizerServiceMock.normalize("Ehningen")).thenReturn("EHNINGEN");

        RouteDataRevision result = sut.save(routeDataRevision);

        ArgumentCaptor<RouteDataRevision> captor = ArgumentCaptor.forClass(RouteDataRevision.class);

        assertThat(result, is(routeDataRevision));
        verify(routeDataRevisionRepositoryMock).save(captor.capture());

        assertThat(captor.getValue().getCity(), is("Ehningen"));
        assertThat(captor.getValue().getCityNormalized(), is("EHNINGEN"));
        assertThat(captor.getValue().getCountry(), is("DE"));
        assertThat(captor.getValue().getPostalCode(), is("66666"));
    }


    @Test
    public void saveValidationErrors() {

        BigDecimal lat = BigDecimal.TEN;
        BigDecimal lon = BigDecimal.ONE;

        Address address = new Address();
        address.getAddress().put("village", "Ehningen");
        address.getAddress().put("country_code", "Ein zu langer Wert");
        address.getAddress().put("postcode", "66666");

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        routeDataRevision.setId(5L);
        routeDataRevision.setLatitude(lat);
        routeDataRevision.setLongitude(lon);

        when(routeDataRevisionRepositoryMock.save(routeDataRevision)).thenReturn(routeDataRevision);
        when(routeDataRevisionRepositoryMock.findOne(5L)).thenReturn(routeDataRevision);
        when(addressServiceWrapperMock.getAddressForGeoLocation(new GeoLocation(lat, lon))).thenReturn(address);
        when(normalizerServiceMock.normalize("Ehningen")).thenReturn("EHNINGEN");

        RouteDataRevision result = sut.save(routeDataRevision);

        ArgumentCaptor<RouteDataRevision> captor = ArgumentCaptor.forClass(RouteDataRevision.class);

        assertThat(result, is(routeDataRevision));
        verify(routeDataRevisionRepositoryMock).save(captor.capture());

        assertThat(captor.getValue().getCity(), nullValue());
        assertThat(captor.getValue().getCityNormalized(), nullValue());
        assertThat(captor.getValue().getCountry(), nullValue());
        assertThat(captor.getValue().getPostalCode(), nullValue());
    }


    @Test
    public void existsEntryNewSurroundsExisting() {

        RouteDataRevision firstExistingRevision = new RouteDataRevision();
        firstExistingRevision.setValidFrom(new DateTime().minusDays(3).toDate());
        firstExistingRevision.setValidTo(new DateTime().minusDays(2).toDate());
        firstExistingRevision.setId(5L);

        RouteDataRevision secondRevision = new RouteDataRevision();
        secondRevision.setValidFrom(new DateTime().minusDays(1).toDate());
        secondRevision.setValidTo(new DateTime().plusDays(1).toDate());
        secondRevision.setId(7L);

        when(routeDataRevisionRepositoryMock.findByTerminalAndLatitudeAndLongitude(BigInteger.ONE, BigDecimal.TEN,
                    BigDecimal.ONE)).thenReturn(asList(firstExistingRevision, secondRevision));

        assertThat(sut.overlapsWithExisting(BigInteger.ONE, BigDecimal.TEN, BigDecimal.ONE,
                new ValidityRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)), null), is(true));

        assertThat(sut.overlapsWithExisting(BigInteger.ONE, BigDecimal.TEN, BigDecimal.ONE,
                new ValidityRange(LocalDate.now().plusDays(2), LocalDate.now().plusDays(3)), null), is(false));

        assertThat(sut.overlapsWithExisting(BigInteger.ONE, BigDecimal.TEN, BigDecimal.ONE,
                new ValidityRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)), 7L), is(false));
    }


    @Test
    @SuppressWarnings("unchecked")
    public void search() {

        RouteRevisionRequest routeRevisionRequest = new RouteRevisionRequest();
        routeRevisionRequest.setCity("Mücheln");

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        RouteDataRevision routeDataRevision2 = new RouteDataRevision();

        when(normalizerServiceMock.normalize("Mücheln")).thenReturn("MUECHELN");
        when(routeDataRevisionRepositoryMock.findAll(any(Specification.class))).thenReturn(asList(routeDataRevision,
                routeDataRevision2));

        List<RouteDataRevision> results = sut.search(routeRevisionRequest);

        assertThat(results, hasSize(2));
    }


    @Test
    public void enrichWithAddressInformation() {

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        routeDataRevision.setLongitude(BigDecimal.ONE);
        routeDataRevision.setLatitude(BigDecimal.ONE);
        routeDataRevision.setId(42L);

        RouteDataRevision routeDataRevision2 = new RouteDataRevision();
        routeDataRevision2.setLatitude(BigDecimal.TEN);
        routeDataRevision2.setLongitude(BigDecimal.TEN);

        when(routeDataRevisionRepositoryMock.findByCityIsNullAndPostalCodeIsNull()).thenReturn(asList(
                routeDataRevision, routeDataRevision2));
        when(addressServiceWrapperMock.getAddressForGeoLocation(new GeoLocation(BigDecimal.ONE, BigDecimal.ONE)))
            .thenReturn(new Address());
        when(addressServiceWrapperMock.getAddressForGeoLocation(new GeoLocation(BigDecimal.TEN, BigDecimal.TEN)))
            .thenReturn(new Address());
        when(routeDataRevisionRepositoryMock.save(any(RouteDataRevision.class))).thenReturn(routeDataRevision);
        when(routeDataRevisionRepositoryMock.findOne(42L)).thenReturn(routeDataRevision);

        sut.enrichWithAddressInformation();

        verify(routeDataRevisionRepositoryMock, times(2)).save(any(RouteDataRevision.class));
    }


    @Test
    public void optionalRouteDataRevision() {

        Address address = new Address(BigDecimal.ONE, BigDecimal.TEN);
        RouteDataRevision routeDataRevisionDB = new RouteDataRevision();

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);

        when(routeDataRevisionRepositoryMock.findNearest(eq(terminal), eq(address.getLatitude()),
                    eq(address.getLongitude()), any(Date.class))).thenReturn(routeDataRevisionDB);

        Optional<RouteDataRevision> routeDataRevision = sut.getRouteDataRevision(BigInteger.ONE, address);

        assertThat(routeDataRevision.isPresent(), is(true));
        assertThat(routeDataRevision.get(), is(routeDataRevisionDB));
    }


    @Test
    public void optionalRouteDataRevisionNoTerminal() {

        Address address = new Address(BigDecimal.ONE, BigDecimal.TEN);

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(null);

        Optional<RouteDataRevision> routeDataRevision = sut.getRouteDataRevision(BigInteger.ONE, address);

        assertThat(routeDataRevision.isPresent(), is(false));
    }


    @Test
    public void optionalRouteDataRevisionNoRevision() {

        Address address = new Address(BigDecimal.ONE, BigDecimal.TEN);

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal);

        when(routeDataRevisionRepositoryMock.findNearest(eq(terminal), eq(address.getLatitude()),
                    eq(address.getLongitude()), any(Date.class))).thenReturn(null);

        Optional<RouteDataRevision> routeDataRevision = sut.getRouteDataRevision(BigInteger.ONE, address);

        assertThat(routeDataRevision.isPresent(), is(false));
    }
}
