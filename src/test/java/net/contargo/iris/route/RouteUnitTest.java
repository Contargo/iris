package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.route.RouteDirection.EXPORT;
import static net.contargo.iris.route.RouteDirection.IMPORT;
import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.TRUCK;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;


/**
 * Unit test for {@link Route}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteUnitTest {

    @Mock
    private List<RoutePart> routePartsMock;
    @Mock
    private List<RoutePart> routeParts2Mock;

    @Test
    public void isRoundTripButOneWay() {

        Route route = RouteCreationUtil.createOneWayImportRoute();
        assertThat(route.isRoundTrip(), is(false));
    }


    @Test
    public void isRoundTrip() {

        Route route = RouteCreationUtil.createRoundTripImportRoute();
        assertThat(route.isRoundTrip(), is(true));
    }


    @Test
    public void getDirectionPartsEmpty() {

        Route route = RouteCreationUtil.createOneWayImportRoute();
        route.getData().setParts(new ArrayList<>());

        assertThat(route.getDirection(), nullValue());
    }


    @Test
    public void getDirectionContainerStateNull() {

        Route route = RouteCreationUtil.createOneWayImportRoute();

        for (RoutePart routePart : route.getData().getParts()) {
            routePart.setContainerState(null);
        }

        assertThat(route.getDirection(), nullValue());
    }


    @Test
    public void getDirectionGivenRouteComposedOfZeroRouteParts() {

        Route sut = new Route();
        RouteData data = new RouteData();
        data.setParts(new ArrayList<>());
        sut.setData(data);

        // invariant preventing sut#getDirection to determine a direction
        assertThat(sut.getDirection(), nullValue());
    }


    @Test
    public void getDirectionGivenRouteComposedOfOneRouteParts() {

        RoutePart routePartA = mock(RoutePart.class);

        Route sut = new Route();
        RouteData data = new RouteData();
        data.setParts(singletonList(routePartA));
        sut.setData(data);

        // invariant preventing sut#getDirection to determine a direction
        when(routePartA.getContainerState()).thenReturn(FULL);
        assertThat(sut.getDirection(), nullValue());
    }


    @Test
    public void getDirectionGivenRouteComposedOfManyRouteParts() {

        RoutePart routePartA = mock(RoutePart.class);
        RoutePart routePartB = mock(RoutePart.class);
        RoutePart routePartC = mock(RoutePart.class);
        RoutePart routePartD = mock(RoutePart.class);

        Route sut = new Route();
        RouteData data = new RouteData();
        data.setParts(asList(routePartA, routePartB, routePartC, routePartD));
        sut.setData(data);

        // transition from empty to full container state
        when(routePartA.getContainerState()).thenReturn(EMPTY);
        when(routePartB.getContainerState()).thenReturn(EMPTY);
        when(routePartC.getContainerState()).thenReturn(EMPTY);
        when(routePartD.getContainerState()).thenReturn(FULL);

        assertThat(sut.getDirection(), is(EXPORT));

        // transition from full to empty container state
        when(routePartA.getContainerState()).thenReturn(FULL);
        when(routePartB.getContainerState()).thenReturn(FULL);
        when(routePartC.getContainerState()).thenReturn(EMPTY);
        when(routePartD.getContainerState()).thenReturn(EMPTY);

        assertThat(sut.getDirection(), is(IMPORT));

        // invariant preventing sut#getDirection to determine a direction
        when(routePartA.getContainerState()).thenReturn(FULL);
        when(routePartB.getContainerState()).thenReturn(EMPTY);
        when(routePartC.getContainerState()).thenReturn(FULL);
        when(routePartD.getContainerState()).thenReturn(EMPTY);

        assertThat(sut.getDirection(), nullValue());
    }


    @Test
    public void isTriangle() {

        Route sut = new Route();
        RouteData routeDataMock = mock(RouteData.class);
        TruckRouteParts onewayTruckRoutePartsMock = mock(TruckRouteParts.class);
        List<RoutePart> onewayTruckPartsListMock = routePartsMock;
        List<RoutePart> truckPartsListMock = routeParts2Mock;
        when(truckPartsListMock.size()).thenReturn(2);
        when(onewayTruckPartsListMock.size()).thenReturn(2);
        when(onewayTruckRoutePartsMock.getTruckRoutePartList()).thenReturn(onewayTruckPartsListMock);
        when(routeDataMock.getOnewayTruckParts()).thenReturn(onewayTruckRoutePartsMock);
        when(routeDataMock.getRoutePartsOfType(TRUCK)).thenReturn(truckPartsListMock);
        sut.setData(routeDataMock);

        assertThat(sut.isTriangle(), is(true));

        when(onewayTruckPartsListMock.size()).thenReturn(3);

        assertThat(sut.isTriangle(), is(false));
    }


    @Test
    public void isDirectTruckRoute() {

        Route route = createRouteWithTypes(asList(TRUCK, TRUCK));

        assertThat(route.isDirectTruckRoute(), is(true));
    }


    @Test
    public void isDirectTruckRoutePartWithWrongType() {

        Route route = createRouteWithTypes(asList(TRUCK, BARGE));

        assertThat(route.isDirectTruckRoute(), is(false));
    }


    private Route createRouteWithTypes(List<RouteType> routeTypes) {

        Route route = new Route();
        RouteData routeData = new RouteData();
        routeData.setParts(routeTypes.stream().map(routeType ->
                    new RoutePart(new GeoLocation(), new GeoLocation(), routeType)).collect(toList()));
        route.setData(routeData);

        return route;
    }
}
