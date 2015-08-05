package net.contargo.iris.co2.advice;

import net.contargo.iris.route.RoutePart;

import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.route.RoutePart.*;
import static net.contargo.iris.route.RoutePart.Direction.DOWNSTREAM;
import static net.contargo.iris.route.RoutePart.Direction.UPSTREAM;
import static net.contargo.iris.terminal.Region.NIEDERRHEIN;
import static net.contargo.iris.terminal.Region.NOT_SET;
import static net.contargo.iris.terminal.Region.OBERRHEIN;
import static net.contargo.iris.terminal.Region.SCHELDE;

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
        when(routePartMock.getDirection()).thenReturn(DOWNSTREAM);

        assertThat(sut.getCo2FactorsKey(DOWNSTREAM, FULL), is("FullDownstream"));
    }


    @Test
    public void getCo2FactorsKeyFullUpStream() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);

        assertThat(sut.getCo2FactorsKey(Direction.UPSTREAM, FULL), is("FullUpstream"));
    }


    @Test
    public void getCo2FactorsKeyFullNotSet() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.NOT_SET);

        assertThat(sut.getCo2FactorsKey(Direction.NOT_SET, FULL), is("FullUpstream"));
    }


    @Test
    public void getCo2FactorsKeyEmptyDownStream() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(DOWNSTREAM);

        assertThat(sut.getCo2FactorsKey(DOWNSTREAM, EMPTY), is("EmptyDownstream"));
    }


    @Test
    public void getCo2FactorsKeyEmptyUpStream() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);

        assertThat(sut.getCo2FactorsKey(Direction.UPSTREAM, EMPTY), is("EmptyUpstream"));
    }


    @Test
    public void getCo2FactorsKeyEmptyNotSet() {

        RoutePart routePartMock = mock(RoutePart.class);
        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.NOT_SET);

        assertThat(sut.getCo2FactorsKey(Direction.NOT_SET, EMPTY), is("EmptyUpstream"));
    }


    @Test
    public void co2FactorsForRegionNiederrhein() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(UPSTREAM);
        assertThat(sut.getCo2Factor(NIEDERRHEIN, UPSTREAM, EMPTY), comparesEqualTo(new BigDecimal("0.27")));

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(DOWNSTREAM);
        assertThat(sut.getCo2Factor(NIEDERRHEIN, DOWNSTREAM, EMPTY), comparesEqualTo(new BigDecimal("0.14")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);
        assertThat(sut.getCo2Factor(NIEDERRHEIN, UPSTREAM, FULL), comparesEqualTo(new BigDecimal("0.31")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(DOWNSTREAM);
        assertThat(sut.getCo2Factor(NIEDERRHEIN, DOWNSTREAM, FULL), comparesEqualTo(new BigDecimal("0.17")));
    }


    @Test
    public void co2FactorsForRegionOberrhein() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(Direction.UPSTREAM);
        assertThat(sut.getCo2Factor(OBERRHEIN, UPSTREAM, EMPTY), comparesEqualTo(new BigDecimal("0.4")));

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(DOWNSTREAM);
        assertThat(sut.getCo2Factor(OBERRHEIN, DOWNSTREAM, EMPTY), comparesEqualTo(new BigDecimal("0.21")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(UPSTREAM);
        assertThat(sut.getCo2Factor(OBERRHEIN, UPSTREAM, FULL), comparesEqualTo(new BigDecimal("0.43")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(DOWNSTREAM);
        assertThat(sut.getCo2Factor(OBERRHEIN, DOWNSTREAM, FULL), comparesEqualTo(new BigDecimal("0.23")));
    }


    @Test
    public void co2FactorsForRegionSchelde() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(UPSTREAM);
        assertThat(sut.getCo2Factor(SCHELDE, UPSTREAM, EMPTY), comparesEqualTo(new BigDecimal("0.375")));

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(DOWNSTREAM);
        assertThat(sut.getCo2Factor(SCHELDE, DOWNSTREAM, EMPTY), comparesEqualTo(new BigDecimal("0.375")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(UPSTREAM);
        assertThat(sut.getCo2Factor(SCHELDE, UPSTREAM, FULL), comparesEqualTo(new BigDecimal("0.427")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(DOWNSTREAM);
        assertThat(sut.getCo2Factor(SCHELDE, DOWNSTREAM, FULL), comparesEqualTo(new BigDecimal("0.427")));
    }


    @Test
    public void co2FactorsForRegionNotSet() {

        RoutePart routePartMock = mock(RoutePart.class);

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(UPSTREAM);
        assertThat(sut.getCo2Factor(NOT_SET, UPSTREAM, EMPTY), comparesEqualTo(new BigDecimal("0.27")));

        when(routePartMock.getContainerState()).thenReturn(EMPTY);
        when(routePartMock.getDirection()).thenReturn(DOWNSTREAM);
        assertThat(sut.getCo2Factor(NOT_SET, DOWNSTREAM, EMPTY), comparesEqualTo(new BigDecimal("0.14")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(UPSTREAM);
        assertThat(sut.getCo2Factor(NOT_SET, UPSTREAM, FULL), comparesEqualTo(new BigDecimal("0.31")));

        when(routePartMock.getContainerState()).thenReturn(FULL);
        when(routePartMock.getDirection()).thenReturn(DOWNSTREAM);
        assertThat(sut.getCo2Factor(NOT_SET, DOWNSTREAM, FULL), comparesEqualTo(new BigDecimal("0.17")));
    }
}
