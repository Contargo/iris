package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import static java.util.Arrays.asList;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public class TruckRoutePartsUnitTest {

    private static final GeoLocation A = new GeoLocation(BigDecimal.ZERO, BigDecimal.ZERO);
    private static final GeoLocation B = new GeoLocation(BigDecimal.ZERO, BigDecimal.ONE);
    private static final GeoLocation C = new GeoLocation(BigDecimal.ONE, BigDecimal.ZERO);
    private static final GeoLocation D = new GeoLocation(BigDecimal.ONE, BigDecimal.ONE);

    private TruckRouteParts subjectUnderTest;

    @Before
    public void setUp() {

        List<RoutePart> routeParts = new ArrayList<>();
        subjectUnderTest = new TruckRouteParts(routeParts);
    }


    @Test
    public void testReduceToOneway() {

        List<RoutePart> routeParts = new ArrayList<>();
        RoutePart forth = new RoutePart(A, B, RouteType.TRUCK);
        RoutePart back = new RoutePart(B, A, RouteType.TRUCK);
        routeParts.add(forth);
        routeParts.add(back);
        subjectUnderTest.setTruckRoutePartList(routeParts);
        subjectUnderTest.reduceToOneway();
        assertThat(subjectUnderTest.getTruckRoutePartList().contains(forth), is(TRUE));
        assertThat(subjectUnderTest.getTruckRoutePartList().contains(back), is(FALSE));
    }


    @Test
    public void isEqualRoundTripEvaluatesToTrue() {

        // A -> B | B -> A
        List<RoutePart> routeParts = new ArrayList<>();
        routeParts.add(new RoutePart(A, B, RouteType.TRUCK));
        routeParts.add(new RoutePart(B, A, RouteType.TRUCK));
        subjectUnderTest.setTruckRoutePartList(routeParts);
        assertThat(subjectUnderTest.isEqualRoundTrip(), is(TRUE));

        // A -> B | B -> C | C -> B | B -> A
        routeParts.clear();
        routeParts.add(new RoutePart(A, B, RouteType.TRUCK));
        routeParts.add(new RoutePart(B, C, RouteType.TRUCK));
        routeParts.add(new RoutePart(C, B, RouteType.TRUCK));
        routeParts.add(new RoutePart(B, A, RouteType.TRUCK));
        assertThat(subjectUnderTest.isEqualRoundTrip(), is(TRUE));
    }


    @Test
    public void isEqualRoundTripEvaluatesToFalse() {

        // A -> B
        List<RoutePart> routeParts = new ArrayList<>();
        routeParts.add(new RoutePart(A, B, RouteType.TRUCK));
        subjectUnderTest.setTruckRoutePartList(routeParts);
        assertThat(subjectUnderTest.isEqualRoundTrip(), is(FALSE));

        // A -> B | B -> C
        routeParts.clear();
        routeParts.add(new RoutePart(A, B, RouteType.TRUCK));
        routeParts.add(new RoutePart(B, C, RouteType.TRUCK));
        assertThat(subjectUnderTest.isEqualRoundTrip(), is(FALSE));

        // A -> B | B -> C | C -> A
        routeParts.clear();
        routeParts.add(new RoutePart(A, B, RouteType.TRUCK));
        routeParts.add(new RoutePart(B, C, RouteType.TRUCK));
        routeParts.add(new RoutePart(C, A, RouteType.TRUCK));
        assertThat(subjectUnderTest.isEqualRoundTrip(), is(FALSE));

        // A -> B | B -> C | C -> B
        routeParts.clear();
        routeParts.add(new RoutePart(A, B, RouteType.TRUCK));
        routeParts.add(new RoutePart(B, C, RouteType.TRUCK));
        routeParts.add(new RoutePart(C, B, RouteType.TRUCK));
        assertThat(subjectUnderTest.isEqualRoundTrip(), is(FALSE));

        // A -> B | B -> C | C -> D | D -> A
        routeParts.clear();
        routeParts.add(new RoutePart(A, B, RouteType.TRUCK));
        routeParts.add(new RoutePart(B, C, RouteType.TRUCK));
        routeParts.add(new RoutePart(C, D, RouteType.TRUCK));
        routeParts.add(new RoutePart(D, A, RouteType.TRUCK));
        assertThat(subjectUnderTest.isEqualRoundTrip(), is(FALSE));
    }


    @Test
    public void extractDestinationAddressSucceeds() {

        RoutePart routePartA = mock(RoutePart.class);
        RoutePart routePartB = mock(RoutePart.class);
        List<RoutePart> routeParts = asList(routePartA, routePartB);
        subjectUnderTest.setTruckRoutePartList(routeParts);

        Terminal terminal = mock(Terminal.class);
        Address address = mock(Address.class);

        when(routePartA.getDestination()).thenReturn(terminal);
        when(routePartB.getDestination()).thenReturn(address); // this route parts destination is of type address

        assertThat(subjectUnderTest.extractDestinationAddress(), is(address));
    }


    @Test
    public void extractStaticAddressIdReturnsNullBecauseThereIsNoRoutePartHavingDestinationAddress() {

        RoutePart routePartA = mock(RoutePart.class);
        RoutePart routePartB = mock(RoutePart.class);
        List<RoutePart> routeParts = asList(routePartA, routePartB);

        subjectUnderTest.setTruckRoutePartList(routeParts);

        Terminal terminal = mock(Terminal.class);
        Seaport seaPort = mock(Seaport.class);

        when(routePartA.getDestination()).thenReturn(terminal);
        when(routePartB.getDestination()).thenReturn(seaPort); // this route parts destination is of type seaport

        assertThat(subjectUnderTest.extractDestinationAddress(), nullValue());
    }
}
