package net.contargo.iris.co2.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.co2.advice.Co2PartStrategy;
import net.contargo.iris.co2.advice.Co2PartStrategyAdvisor;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.builder.DirectTruckRouteBuilder;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.TRUCK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link net.contargo.iris.co2.service.Co2ServiceImpl}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class Co2ServiceImplUnitTest {

    private static final GeoLocation GEO_TERMINAL = new Terminal(new GeoLocation(BigDecimal.ONE, BigDecimal.ONE));
    private static final GeoLocation GEO_ADDRESS = new GeoLocation(BigDecimal.ONE, BigDecimal.TEN);
    private static final GeoLocation GEO_SEAPORT = new Seaport(new GeoLocation(BigDecimal.TEN, BigDecimal.TEN));

    private Co2ServiceImpl sut;

    @Mock
    private DirectTruckRouteBuilder builderMock;
    @Mock
    private Co2PartStrategyAdvisor co2PartStrategyAdvisorMock;

    @Before
    public void setup() {

        sut = new Co2ServiceImpl(builderMock, co2PartStrategyAdvisorMock);
    }


    @Test
    public void getEmissionExportOneWay() {

        Co2PartStrategy co2PartBargeStrategyMock = mock(Co2PartStrategy.class);
        when(co2PartStrategyAdvisorMock.advice(BARGE)).thenReturn(co2PartBargeStrategyMock);

        Co2PartStrategy co2PartTruckStrategyMock = mock(Co2PartStrategy.class);
        when(co2PartStrategyAdvisorMock.advice(TRUCK)).thenReturn(co2PartTruckStrategyMock);

        RoutePart truckPart1 = new RoutePart();
        truckPart1.setRouteType(TRUCK);
        truckPart1.setOrigin(GEO_TERMINAL);
        truckPart1.setDestination(GEO_ADDRESS);

        RoutePart truckPart2 = new RoutePart();
        truckPart2.setRouteType(TRUCK);
        truckPart2.setOrigin(GEO_ADDRESS);
        truckPart2.setDestination(GEO_TERMINAL);

        RoutePart bargePart = new RoutePart();
        bargePart.setRouteType(BARGE);
        bargePart.setOrigin(GEO_TERMINAL);
        bargePart.setDestination(GEO_SEAPORT);

        Route route = new Route();
        route.getData().setParts(asList(truckPart1, truckPart2, bargePart));

        when(co2PartBargeStrategyMock.getEmissionForRoutePart(bargePart)).thenReturn(new BigDecimal("10"));
        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart1)).thenReturn(new BigDecimal("2"));
        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart2)).thenReturn(new BigDecimal("3"));

        // + 2 (truck part 1)
        // + 3 (truck part 2)
        // + 8 (co2 handling)
        // +10 (barge part)
        // + 4 (Umschlag "Direkttruck Oneway")
        // = 27

        BigDecimal co2 = sut.getEmission(route);
        assertThat(co2, comparesEqualTo(new BigDecimal("27")));
    }


    @Test
    public void getEmissionImportOneWay() {

        Co2PartStrategy co2PartBargeStrategyMock = mock(Co2PartStrategy.class);
        when(co2PartStrategyAdvisorMock.advice(BARGE)).thenReturn(co2PartBargeStrategyMock);

        Co2PartStrategy co2PartTruckStrategyMock = mock(Co2PartStrategy.class);
        when(co2PartStrategyAdvisorMock.advice(TRUCK)).thenReturn(co2PartTruckStrategyMock);

        RoutePart bargePart = new RoutePart();
        bargePart.setRouteType(BARGE);
        bargePart.setOrigin(GEO_SEAPORT);
        bargePart.setDestination(GEO_TERMINAL);

        RoutePart truckPart1 = new RoutePart();
        truckPart1.setRouteType(TRUCK);
        truckPart1.setOrigin(GEO_TERMINAL);
        truckPart1.setDestination(GEO_ADDRESS);

        RoutePart truckPart2 = new RoutePart();
        truckPart2.setRouteType(TRUCK);
        truckPart2.setOrigin(GEO_ADDRESS);
        truckPart2.setDestination(GEO_TERMINAL);

        Route route = new Route();
        route.getData().setParts(asList(bargePart, truckPart1, truckPart2));

        when(co2PartBargeStrategyMock.getEmissionForRoutePart(bargePart)).thenReturn(new BigDecimal("10"));
        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart1)).thenReturn(new BigDecimal("2"));
        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart2)).thenReturn(new BigDecimal("3"));

        // + 2 (truck part 1)
        // + 3 (truck part 2)
        // + 8 (co2 handling)
        // +10 (barge part)
        // + 4 (Umschlag "Direkttruck Oneway")
        // = 27

        BigDecimal co2 = sut.getEmission(route);
        assertThat(co2, comparesEqualTo(new BigDecimal("27")));
    }


    @Test
    public void getEmissionRoundTrip() {

        Co2PartStrategy co2PartBargeStrategyMock = mock(Co2PartStrategy.class);
        when(co2PartStrategyAdvisorMock.advice(BARGE)).thenReturn(co2PartBargeStrategyMock);

        Co2PartStrategy co2PartTruckStrategyMock = mock(Co2PartStrategy.class);
        when(co2PartStrategyAdvisorMock.advice(TRUCK)).thenReturn(co2PartTruckStrategyMock);

        RoutePart bargePart1 = new RoutePart();
        bargePart1.setRouteType(BARGE);
        bargePart1.setOrigin(GEO_SEAPORT);
        bargePart1.setDestination(GEO_TERMINAL);

        RoutePart truckPart1 = new RoutePart();
        truckPart1.setRouteType(TRUCK);
        truckPart1.setOrigin(GEO_TERMINAL);
        truckPart1.setDestination(GEO_ADDRESS);

        RoutePart truckPart2 = new RoutePart();
        truckPart2.setRouteType(TRUCK);
        truckPart2.setOrigin(GEO_ADDRESS);
        truckPart2.setDestination(GEO_TERMINAL);

        RoutePart bargePart2 = new RoutePart();
        bargePart2.setRouteType(BARGE);
        bargePart2.setOrigin(GEO_TERMINAL);
        bargePart2.setDestination(GEO_SEAPORT);

        Route route = new Route();
        route.getData().setParts(asList(bargePart1, truckPart1, truckPart2, bargePart2));

        when(co2PartBargeStrategyMock.getEmissionForRoutePart(bargePart1)).thenReturn(new BigDecimal("10"));
        when(co2PartBargeStrategyMock.getEmissionForRoutePart(bargePart2)).thenReturn(new BigDecimal("15"));
        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart1)).thenReturn(new BigDecimal("2"));
        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart2)).thenReturn(new BigDecimal("3"));

        // +10 (barge part 1)
        // + 8 (co2 handling)
        // + 2 (truck part 1)
        // + 3 (truck part 2)
        // + 8 (co2 handling)
        // +15 (barge part 2)
        // = 46

        BigDecimal co2 = sut.getEmission(route);
        assertThat(co2, comparesEqualTo(new BigDecimal("46")));
    }


    @Test
    public void getEmissionDirectTruckExportOneWay() {

        Co2PartStrategy co2PartTruckStrategyMock = mock(Co2PartStrategy.class);
        when(co2PartStrategyAdvisorMock.advice(TRUCK)).thenReturn(co2PartTruckStrategyMock);

        RoutePart truckPart1 = new RoutePart();
        truckPart1.setRouteType(TRUCK);
        truckPart1.setOrigin(GEO_TERMINAL);
        truckPart1.setDestination(GEO_ADDRESS);

        RoutePart truckPart2 = new RoutePart();
        truckPart2.setRouteType(TRUCK);
        truckPart2.setOrigin(GEO_ADDRESS);
        truckPart2.setDestination(GEO_SEAPORT);

        Route route = new Route();
        route.getData().setParts(asList(truckPart1, truckPart2));

        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart1)).thenReturn(new BigDecimal("2"));
        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart2)).thenReturn(new BigDecimal("3"));

        // + 2 (truck part 1)
        // + 3 (truck part 2)
        // + 4 (Umschlag "Direkttruck Oneway")
        // = 9

        Route routeDummy = new Route();
        when(builderMock.getCorrespondingDirectTruckRoute(routeDummy)).thenReturn(route);

        BigDecimal co2 = sut.getEmissionDirectTruck(routeDummy);
        assertThat(co2, comparesEqualTo(new BigDecimal("9")));
    }


    @Test
    public void getEmissionDirectTruckImportOneWay() {

        Co2PartStrategy co2PartTruckStrategyMock = mock(Co2PartStrategy.class);
        when(co2PartStrategyAdvisorMock.advice(TRUCK)).thenReturn(co2PartTruckStrategyMock);

        RoutePart truckPart1 = new RoutePart();
        truckPart1.setRouteType(TRUCK);
        truckPart1.setOrigin(GEO_SEAPORT);
        truckPart1.setDestination(GEO_ADDRESS);

        RoutePart truckPart2 = new RoutePart();
        truckPart2.setRouteType(TRUCK);
        truckPart2.setOrigin(GEO_ADDRESS);
        truckPart2.setDestination(GEO_TERMINAL);

        Route route = new Route();
        route.getData().setParts(asList(truckPart1, truckPart2));

        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart1)).thenReturn(new BigDecimal("2"));
        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart2)).thenReturn(new BigDecimal("3"));

        // + 2 (truck part 1)
        // + 3 (truck part 2)
        // + 4 (Umschlag "Direkttruck Oneway")
        // = 9

        Route routeDummy = new Route();
        when(builderMock.getCorrespondingDirectTruckRoute(routeDummy)).thenReturn(route);

        BigDecimal co2 = sut.getEmissionDirectTruck(routeDummy);
        assertThat(co2, comparesEqualTo(new BigDecimal("9")));
    }


    @Test
    public void getEmissionDirectTruckRoundTrip() {

        Co2PartStrategy co2PartTruckStrategyMock = mock(Co2PartStrategy.class);
        when(co2PartStrategyAdvisorMock.advice(TRUCK)).thenReturn(co2PartTruckStrategyMock);

        RoutePart truckPart1 = new RoutePart();
        truckPart1.setRouteType(TRUCK);
        truckPart1.setOrigin(GEO_SEAPORT);
        truckPart1.setDestination(GEO_ADDRESS);

        RoutePart truckPart2 = new RoutePart();
        truckPart2.setRouteType(TRUCK);
        truckPart2.setOrigin(GEO_ADDRESS);
        truckPart2.setDestination(GEO_SEAPORT);

        Route route = new Route();
        route.getData().setParts(asList(truckPart1, truckPart2));

        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart1)).thenReturn(new BigDecimal("2"));
        when(co2PartTruckStrategyMock.getEmissionForRoutePart(truckPart2)).thenReturn(new BigDecimal("3"));

        // + 2 (truck part 1)
        // + 3 (truck part 2)
        // = 13

        Route routeDummy = new Route();
        when(builderMock.getCorrespondingDirectTruckRoute(routeDummy)).thenReturn(route);

        BigDecimal co2 = sut.getEmissionDirectTruck(routeDummy);
        assertThat(co2, comparesEqualTo(new BigDecimal("5")));
    }
}
