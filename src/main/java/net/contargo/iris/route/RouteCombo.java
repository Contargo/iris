package net.contargo.iris.route;

import static net.contargo.iris.route.RouteType.BARGE;
import static net.contargo.iris.route.RouteType.DTRUCK;
import static net.contargo.iris.route.RouteType.RAIL;


/**
 * Enum describing which combo mode a Route has. Each combo has one or more route types.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public enum RouteCombo {

    WATERWAY(new RouteType[] { BARGE }),
    RAILWAY(new RouteType[] { RAIL }),
    ROAD(new RouteType[] { DTRUCK }),
    ALL(new RouteType[] { BARGE, RAIL });

    private RouteType[] routeTypes;

    RouteCombo(RouteType[] routeTypes) {

        this.routeTypes = routeTypes.clone();
    }

    public RouteType[] getRouteTypes() {

        return routeTypes;
    }
}
