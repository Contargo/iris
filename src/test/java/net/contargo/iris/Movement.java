package net.contargo.iris;

import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.route.builder.RouteBuilder;


/**
 * Helper class for {@link net.contargo.iris.Movement}s in unit tests.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class Movement {

    private GeoLocation loc;
    private RouteType type;
    private ContainerType c;

    Movement(GeoLocation loc, RouteType type, ContainerType c) {

        this.loc = loc;
        this.type = type;
        this.c = c;
    }

    public static Movement to(GeoLocation loc, RouteType type) {

        return new Movement(loc, type, null);
    }


    public static Movement to(GeoLocation loc, RouteType type, ContainerType c) {

        return new Movement(loc, type, c);
    }


    public static Route move(GeoLocation start, ContainerType containerType, Movement... movements) {

        RouteBuilder b = new RouteBuilder(start, containerType, null);

        for (Movement m : movements) {
            if (m.c != null) {
                b.changeContainerType(m.c);
            }

            b.goTo(m.loc, m.type);
        }

        return b.getRoute();
    }
}
