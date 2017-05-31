package net.contargo.iris.routedatarevision.service.cleanup;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.mail.service.EmailService;
import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.persistence.RouteDataRevisionRepository;
import net.contargo.iris.routedatarevision.web.RouteDataRevisionCleanupRequest;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.TruckRouteService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.math.BigDecimal;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteDataRevisionCleanupServiceUnitTest {

    private RouteDataRevisionCleanupService sut;

    @Mock
    private RouteDataRevisionRepository repoMock;
    @Mock
    private TruckRouteService routeServiceMock;
    @Mock
    private RoundingService roundingServiceMock;
    @Mock
    private EmailService emailServiceMock;
    @Mock
    private RouteDataRevisionCsvService csvServiceMock;
    @Captor
    private ArgumentCaptor<List<RouteDataRevisionCleanupRecord>> csvCaptor;
    @Captor
    private ArgumentCaptor<Map<String, String>> emailDataCaptor;

    @Before
    public void setUp() {

        sut = new RouteDataRevisionCleanupService(repoMock, routeServiceMock, roundingServiceMock, emailServiceMock,
                csvServiceMock);
    }


    @Test
    public void cleanup() {

        RouteDataRevision revision = new RouteDataRevision();
        revision.setLatitude(TEN);
        revision.setLongitude(ONE);
        revision.setTerminal(new Terminal(new GeoLocation(ONE, TEN)));
        revision.setTruckDistanceOneWayInKilometer(TEN);

        when(repoMock.findValid(any(Date.class))).thenReturn(Stream.of(revision));
        when(routeServiceMock.route(any(GeoLocation.class), any(GeoLocation.class))).thenReturn(new TruckRoute(TEN,
                TEN, ZERO));
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(TEN);

        InputStream csv = new ByteArrayInputStream("lala".getBytes());
        when(csvServiceMock.generateCsvReport(any())).thenReturn(csv);

        sut.cleanup(new RouteDataRevisionCleanupRequest("user@example.com"));

        verify(csvServiceMock).generateCsvReport(csvCaptor.capture());
        assertThat(csvCaptor.getValue(), hasSize(1));

        verify(emailServiceMock).sendWithAttachment(eq("user@example.com"), eq("Route revision - Cleanup report"),
            eq("routerevision-cleanup.ftl"), emailDataCaptor.capture(), eq(csv), eq("routerevision-cleanup.csv"));

        Map<String, String> emailData = emailDataCaptor.getValue();
        assertThat(emailData, hasEntry("username", "user@example.com"));
        assertThat(emailData, hasEntry("numberOfRevisions", "1"));
    }


    @Test
    public void cleanupError() {

        RouteDataRevision revision = new RouteDataRevision();
        revision.setLatitude(TEN);
        revision.setLongitude(ONE);
        revision.setTerminal(new Terminal(new GeoLocation(ONE, TEN)));
        revision.setTruckDistanceOneWayInKilometer(TEN);

        when(repoMock.findValid(any(Date.class))).thenReturn(Stream.of(revision));
        when(routeServiceMock.route(any(GeoLocation.class), any(GeoLocation.class))).thenReturn(new TruckRoute(TEN,
                TEN, ZERO));
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(TEN);
        doThrow(RouteDataRevisionCsvException.class).when(csvServiceMock).generateCsvReport(any());

        sut.cleanup(new RouteDataRevisionCleanupRequest("user@example.com"));

        verify(emailServiceMock).send(eq("user@example.com"), eq("Route revision - Cleanup report - Error"),
            eq("routerevision-cleanup-error.ftl"), emailDataCaptor.capture());

        Map<String, String> emailData = emailDataCaptor.getValue();
        assertThat(emailData, hasEntry("username", "user@example.com"));
        assertThat(emailData, hasEntry("numberOfRevisions", "1"));
    }


    @Test
    public void identifyObsoleteRevisionSwitzerland() {

        RouteDataRevision revision = new RouteDataRevision();
        revision.setCountry("CH");

        assertThat(sut.identifyObsoleteRevision(revision), isEmpty());
    }


    @Test
    public void identifyObsoleteRevisionFalse() {

        RouteDataRevision revision = new RouteDataRevision();
        revision.setLatitude(TEN);
        revision.setLongitude(ONE);
        revision.setTerminal(new Terminal(new GeoLocation(ONE, TEN)));
        revision.setTruckDistanceOneWayInKilometer(new BigDecimal("100"));

        when(routeServiceMock.route(any(GeoLocation.class), any(GeoLocation.class))).thenReturn(new TruckRoute(TEN,
                TEN, ZERO));
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(TEN);

        assertThat(sut.identifyObsoleteRevision(revision), isEmpty());
    }


    @Test
    public void identifyObsoleteRevisionTrue() {

        RouteDataRevision revision = new RouteDataRevision();
        revision.setLatitude(TEN);
        revision.setLongitude(ONE);
        revision.setTerminal(new Terminal(new GeoLocation(ONE, TEN)));
        revision.setTruckDistanceOneWayInKilometer(TEN);

        when(routeServiceMock.route(any(GeoLocation.class), any(GeoLocation.class))).thenReturn(new TruckRoute(TEN,
                TEN, ZERO));
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(TEN);

        assertThat(sut.identifyObsoleteRevision(revision), isPresent());
    }


    @Test
    public void isStaffelsprungFalse() {

        RouteDataRevision revision = new RouteDataRevision();
        revision.setTruckDistanceOneWayInKilometer(new BigDecimal("11"));

        BigDecimal truckDistance = new BigDecimal("20");
        TruckRoute truckRoute = new TruckRoute(truckDistance, null, null);
        when(roundingServiceMock.roundDistance(truckDistance)).thenReturn(truckDistance);

        assertThat(sut.isStaffelsprung(revision, truckRoute), is(false));
    }


    @Test
    public void isStaffelsprungTrue() {

        RouteDataRevision revision = new RouteDataRevision();
        revision.setTruckDistanceOneWayInKilometer(new BigDecimal("20"));

        BigDecimal truckDistance = new BigDecimal("20.7");
        TruckRoute truckRoute = new TruckRoute(truckDistance, null, null);
        when(roundingServiceMock.roundDistance(truckDistance)).thenReturn(new BigDecimal("21"));

        assertThat(sut.isStaffelsprung(revision, truckRoute), is(true));
    }


    @Test
    public void isStaffelSpringZeroInput() {

        RouteDataRevision revision = new RouteDataRevision();
        revision.setTruckDistanceOneWayInKilometer(ZERO);

        TruckRoute truckRoute = new TruckRoute(ZERO, null, null);
        when(roundingServiceMock.roundDistance(ZERO)).thenReturn(ZERO);

        assertThat(sut.isStaffelsprung(revision, truckRoute), is(false));
    }
}
