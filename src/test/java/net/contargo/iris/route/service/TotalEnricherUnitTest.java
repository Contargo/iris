package net.contargo.iris.route.service;

import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Unit test for implementation of TotalEnricher: {@link TotalEnricher}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class TotalEnricherUnitTest {

    private TotalEnricher sut;

    private EnricherContext enricherContext;
    private Route route;
    private RouteData routeData;
    private RoutePart routePart;
    private RoutePartData routePartData;
    private BigDecimal distance, tollDistance, duration;

    @Before
    public void setup() {

        distance = new BigDecimal(1);
        tollDistance = new BigDecimal(2);
        duration = new BigDecimal(3);

        route = new Route();
        routeData = new RouteData();
        routePart = new RoutePart();
        routePartData = new RoutePartData();

        enricherContext = new EnricherContext.Builder().build();

        sut = new TotalEnricher();
    }


    @Test
    public void testEnricher() {

        routePartData.setDistance(distance);
        routePartData.setTollDistance(tollDistance);
        routePartData.setDuration(duration);
        routePart.setData(routePartData);
        routeData.setParts(Arrays.asList(routePart, routePart, routePart, routePart));

        route.setData(routeData);

        BigDecimal multi = new BigDecimal(routeData.getParts().size());

        sut.enrich(route, enricherContext);

        assertThat(route.getData().getTotalDistance(), is(distance.multiply(multi)));
        assertThat(route.getData().getTotalRealTollDistance(), is(tollDistance.multiply(multi)));
        assertThat(route.getData().getTotalDuration(), is(duration.multiply(multi)));
    }


    @Test
    public void testEnricherNoData() {

        routePart.setData(routePartData);
        routeData.setParts(Arrays.asList(routePart, routePart, routePart, routePart));

        route.setData(routeData);

        sut.enrich(route, enricherContext);

        assertThat(route.getData().getTotalDistance(), nullValue());
        assertThat(route.getData().getTotalRealTollDistance(), nullValue());
        assertThat(route.getData().getTotalDuration(), nullValue());
    }
}
