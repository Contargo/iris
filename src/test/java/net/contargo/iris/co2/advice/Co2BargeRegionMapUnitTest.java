package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RoutePart;
import net.contargo.iris.terminal.Region;

import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.route.RoutePart.*;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2BargeRegionMapUnitTest {

    private Co2BargeRegionMap sut = new Co2BargeRegionMap();

    @Test
    public void getCo2FactorsKeyFullDownStream() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.DOWNSTREAM);

        assertThat(sut.getCo2FactorsKey(routePartMock), is("FullDownstream"));
    }


    @Test
    public void getCo2FactorsKeyFullUpStream() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);

        assertThat(sut.getCo2FactorsKey(routePartMock), is("FullUpstream"));
    }


    @Test
    public void getCo2FactorsKeyFullNotSet() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.NOT_SET);

        assertThat(sut.getCo2FactorsKey(routePartMock), is("FullUpstream"));
    }


    @Test
    public void getCo2FactorsKeyEmptyDownStream() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.DOWNSTREAM);

        assertThat(sut.getCo2FactorsKey(routePartMock), is("EmptyDownstream"));
    }


    @Test
    public void getCo2FactorsKeyEmptyUpStream() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);

        assertThat(sut.getCo2FactorsKey(routePartMock), is("EmptyUpstream"));
    }


    @Test
    public void getCo2FactorsKeyEmptyNotSet() {

        RoutePart routePartMock = mock(RoutePart.class);
        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.NOT_SET);

        assertThat(sut.getCo2FactorsKey(routePartMock), is("EmptyUpstream"));
    }


    @Test
    public void co2FactorsForRegionNiederrhein() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);
        assertThat(sut.getCo2Factor(Region.NIEDERRHEIN, routePartMock), comparesEqualTo(new BigDecimal("0.27")));

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.DOWNSTREAM);
        assertThat(sut.getCo2Factor(Region.NIEDERRHEIN, routePartMock), comparesEqualTo(new BigDecimal("0.14")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);
        assertThat(sut.getCo2Factor(Region.NIEDERRHEIN, routePartMock), comparesEqualTo(new BigDecimal("0.31")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.DOWNSTREAM);
        assertThat(sut.getCo2Factor(Region.NIEDERRHEIN, routePartMock), comparesEqualTo(new BigDecimal("0.17")));
    }


    @Test
    public void co2FactorsForRegionOberrhein() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);
        assertThat(sut.getCo2Factor(Region.OBERRHEIN, routePartMock), comparesEqualTo(new BigDecimal("0.4")));

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.DOWNSTREAM);
        assertThat(sut.getCo2Factor(Region.OBERRHEIN, routePartMock), comparesEqualTo(new BigDecimal("0.21")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);
        assertThat(sut.getCo2Factor(Region.OBERRHEIN, routePartMock), comparesEqualTo(new BigDecimal("0.43")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.DOWNSTREAM);
        assertThat(sut.getCo2Factor(Region.OBERRHEIN, routePartMock), comparesEqualTo(new BigDecimal("0.23")));
    }


    @Test
    public void co2FactorsForRegionSchelde() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);
        assertThat(sut.getCo2Factor(Region.SCHELDE, routePartMock), comparesEqualTo(new BigDecimal("0.375")));

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.DOWNSTREAM);
        assertThat(sut.getCo2Factor(Region.SCHELDE, routePartMock), comparesEqualTo(new BigDecimal("0.375")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);
        assertThat(sut.getCo2Factor(Region.SCHELDE, routePartMock), comparesEqualTo(new BigDecimal("0.427")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.DOWNSTREAM);
        assertThat(sut.getCo2Factor(Region.SCHELDE, routePartMock), comparesEqualTo(new BigDecimal("0.427")));
    }


    @Test
    public void co2FactorsForRegionNotSet() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);
        assertThat(sut.getCo2Factor(Region.NOT_SET, routePartMock), comparesEqualTo(new BigDecimal("0.27")));

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.DOWNSTREAM);
        assertThat(sut.getCo2Factor(Region.NOT_SET, routePartMock), comparesEqualTo(new BigDecimal("0.14")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);
        assertThat(sut.getCo2Factor(Region.NOT_SET, routePartMock), comparesEqualTo(new BigDecimal("0.31")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.DOWNSTREAM);
        assertThat(sut.getCo2Factor(Region.NOT_SET, routePartMock), comparesEqualTo(new BigDecimal("0.17")));
    }
}
