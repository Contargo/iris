package net.contargo.iris;

import net.contargo.iris.route.RoutePart;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public enum FlowDirection {

    UPSTREAM,
    DOWNSTREAM;

    public static FlowDirection from(RoutePart.Direction direction) {

        switch (direction) {
            case DOWNSTREAM:
                return DOWNSTREAM;

            case UPSTREAM:
                return UPSTREAM;

            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }
}
