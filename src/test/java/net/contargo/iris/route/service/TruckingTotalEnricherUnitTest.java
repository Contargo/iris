package net.contargo.iris.route.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.terminal.Terminal;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * Unit test for implementation of {@link TruckingTotalEnricher}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class TruckingTotalEnricherUnitTest {

    private TruckingTotalEnricher sut;

    private EnricherContext enricherContext;
    private Route route;
    private RoutePart routePart3, routePart4;
    private BigDecimal totalTollDistanceSeaport, totalTollDistanceTriangle, totalOnewayTruckDistanceSeaport,
        totalOnewayTruckDistanceTriangle;

    private Terminal terminal;
    private Address address1, address2, address3;

    @Before
    public void setup() {

        BigDecimal partTollDistance1 = new BigDecimal(1);
        BigDecimal partTollDistance2 = new BigDecimal(5);
        totalTollDistanceSeaport = new BigDecimal(4);
        totalTollDistanceTriangle = new BigDecimal(12);

        BigDecimal partDistance1 = new BigDecimal(4);
        BigDecimal partDistance2 = new BigDecimal(5);
        totalOnewayTruckDistanceSeaport = new BigDecimal(8);
        totalOnewayTruckDistanceTriangle = new BigDecimal(9);

        sut = new TruckingTotalEnricher();

        terminal = new Terminal();

        address1 = new Address();
        address1.setDisplayName("first");
        address2 = new Address();
        address2.setDisplayName("second");
        address3 = new Address();
        address3.setDisplayName("third");

        route = new Route();

        RouteData routeData = new RouteData();
        RoutePart routePart1 = new RoutePart();
        RoutePart routePart2 = new RoutePart();
        routePart3 = new RoutePart();
        routePart4 = new RoutePart();

        RoutePartData routePartData1 = new RoutePartData();
        routePartData1.setTollDistance(partTollDistance1);
        routePartData1.setDistance(partDistance1);

        RoutePartData routePartData2 = new RoutePartData();
        routePartData2.setTollDistance(partTollDistance2);
        routePartData2.setDistance(partDistance2);

        routePart1.setData(routePartData1);
        routePart1.setRouteType(RouteType.TRUCK);
        routePart1.setOrigin(terminal);
        routePart1.setDestination(address1);

        routePart2.setData(routePartData1);
        routePart2.setRouteType(RouteType.TRUCK);
        routePart2.setOrigin(address1);
        routePart2.setDestination(address2);

        routePart3.setData(routePartData2);
        routePart3.setRouteType(RouteType.TRUCK);
        routePart3.setOrigin(address2);
        routePart3.setDestination(address1);

        routePart4.setData(routePartData2);
        routePart4.setRouteType(RouteType.TRUCK);
        routePart4.setOrigin(address1);
        routePart4.setDestination(terminal);

        routeData.setParts(Arrays.asList(routePart1, routePart2, routePart3, routePart4));

        route.setData(routeData);

        enricherContext = new EnricherContext.Builder().build();
    }


    @Test
    public void testEnricherSeaport() {

        sut.enrich(route, enricherContext);

        assertThat(totalTollDistanceSeaport, is(route.getData().getTotalTollDistance()));
        assertThat(totalOnewayTruckDistanceSeaport, is(route.getData().getTotalOnewayTruckDistance()));
    }


    @Test
    public void testEnricherTriangle() {

        // this is a triangle routing and the first and third address are not the same (gps coordinates)
        // so this will return every part as the oneway

        address1.setLatitude(BigDecimal.ONE);
        address1.setLongitude(BigDecimal.ONE);

        address3.setLatitude(BigDecimal.TEN);
        address3.setLongitude(BigDecimal.TEN);

        routePart3.setOrigin(address2);
        routePart3.setDestination(address3);

        routePart4.setOrigin(address3);
        routePart4.setDestination(terminal);

        sut.enrich(route, enricherContext);

        assertThat(totalTollDistanceTriangle, is(route.getData().getTotalTollDistance()));
        assertThat(totalOnewayTruckDistanceTriangle, is(route.getData().getTotalOnewayTruckDistance()));
    }
}
