package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;

import org.junit.Test;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import static java.util.Arrays.asList;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class RouteDataUnitTest {

    private static final GeoLocation A = new GeoLocation(BigDecimal.ZERO, BigDecimal.ZERO);
    private static final GeoLocation B = new GeoLocation(BigDecimal.ZERO, BigDecimal.ONE);
    private static final GeoLocation C = new GeoLocation(BigDecimal.ONE, BigDecimal.ZERO);
    private static final GeoLocation D = new GeoLocation(BigDecimal.ONE, BigDecimal.ONE);

    private final RouteData sut = new RouteData();

    @Test
    public void getRoutePartsOfType() {

        RoutePart ab = new RoutePart(A, B, RouteType.BARGE);
        RoutePart bc = new RoutePart(B, C, RouteType.TRUCK);
        RoutePart cb = new RoutePart(C, B, RouteType.TRUCK);

        // A -> B | B -> C | C -> B
        List<RoutePart> routeParts = asList(ab, bc, cb);

        sut.setParts(routeParts);
        assertThat(sut.getRoutePartsOfType(RouteType.BARGE), hasItems(ab));
        assertThat(sut.getRoutePartsOfType(RouteType.TRUCK), hasItems(bc, cb));
        assertThat(sut.getRoutePartsOfType(RouteType.TRUCK), not(hasItems(ab)));
        assertThat(sut.getRoutePartsOfType(null), is(Collections.<RoutePart>emptyList()));
        assertThat(sut.getRoutePartsOfType(RouteType.RAIL), is(Collections.<RoutePart>emptyList()));
    }
}
