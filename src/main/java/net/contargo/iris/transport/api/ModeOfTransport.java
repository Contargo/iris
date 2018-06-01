package net.contargo.iris.transport.api;

import net.contargo.iris.route.RouteType;
import net.contargo.iris.routing.osrm.OSRMProfile;

import static net.contargo.iris.route.RouteType.TRUCK;
import static net.contargo.iris.routing.osrm.OSRMProfile.DRIVING;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public enum ModeOfTransport {

    RAIL(OSRMProfile.RAIL, RouteType.RAIL),
    WATER(OSRMProfile.WATER, RouteType.BARGE),
    ROAD(DRIVING, TRUCK);

    private final RouteType routeType;
    private final OSRMProfile osrmProfile;

    ModeOfTransport(OSRMProfile osrmProfile, RouteType routeType) {

        this.routeType = routeType;
        this.osrmProfile = osrmProfile;
    }

    public RouteType getRouteType() {

        return routeType;
    }


    public OSRMProfile getOsrmProfile() {

        return osrmProfile;
    }


    public static ModeOfTransport fromRouteType(RouteType type) {

        switch (type) {
            case BARGE:
                return WATER;

            case RAIL:
                return RAIL;

            case TRUCK:
                return ROAD;

            default:
                throw new IllegalArgumentException();
        }
    }
}
