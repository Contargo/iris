package net.contargo.iris.route;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;


/**
 * This class represents a route part having two associated {@link GeoLocation}s, each denoting an origin or a
 * destination respectively. Note that the associated {@link GeoLocation}s may be any subtype of {@link GeoLocation}.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @see  GeoLocation
 */
public class RoutePart {

    public enum Direction {

        NOT_SET,
        DOWNSTREAM,
        UPSTREAM
    }

    private GeoLocation origin;
    private GeoLocation destination;
    private RouteType routeType;
    private ContainerType containerType;
    private ContainerState containerState;
    private RoutePartData data = new RoutePartData();

    public RoutePart() {

        // Needed for Spring MVC JSON mapping
    }


    public RoutePart(GeoLocation origin, GeoLocation destination, RouteType routeType) {

        this.origin = origin;
        this.destination = destination;
        this.routeType = routeType;
    }


    public RoutePart(RouteType routeType, GeoLocation origin, GeoLocation destination, ContainerType containerType,
        ContainerState containerState) {

        this.routeType = routeType;
        this.origin = origin;
        this.destination = destination;
        this.containerType = containerType;
        this.containerState = containerState;
    }

    /**
     * Computes this {@link RoutePart}'s direction.
     *
     * @return  <ul>
     *            <li>UPSTREAM if it's a {@link RoutePart} from {@link Seaport} to {@link Terminal}</li>
     *            <li>DOWNSTREAM if it's a {@link RoutePart} from {@link Terminal} to {@link Seaport}</li>
     *            <li>NOT_SET otherwise, or if it's not a main run part</li>
     *          </ul>
     */
    public Direction getDirection() {

        if (!(isOfType(RouteType.BARGE))) {
            return Direction.NOT_SET;
        }

        if (origin instanceof Seaport && destination instanceof Terminal) {
            return RoutePart.Direction.UPSTREAM;
        } else if (origin instanceof Terminal && destination instanceof Seaport) {
            return Direction.DOWNSTREAM;
        } else {
            return Direction.NOT_SET;
        }
    }


    public boolean isOfType(RouteType type) {

        return this.routeType == type && type != null;
    }


    public String getName() {

        return getOrigin().getNiceName() + " -> " + getDestination().getNiceName();
    }


    public GeoLocation getOrigin() {

        return origin;
    }


    public void setOrigin(GeoLocation origin) {

        this.origin = origin;
    }


    public GeoLocation getDestination() {

        return destination;
    }


    public void setDestination(GeoLocation destination) {

        this.destination = destination;
    }


    public RouteType getRouteType() {

        return routeType;
    }


    public void setRouteType(RouteType routeType) {

        this.routeType = routeType;
    }


    public ContainerType getContainerType() {

        return containerType;
    }


    public void setContainerType(ContainerType containerType) {

        this.containerType = containerType;
    }


    public ContainerState getContainerState() {

        return containerState;
    }


    public void setContainerState(ContainerState containerState) {

        this.containerState = containerState;
    }


    public RoutePartData getData() {

        return data;
    }


    public void setData(RoutePartData data) {

        this.data = data;
    }


    /**
     * Checks whether this {@link RoutePart} has a {@link Terminal}.
     *
     * @return  true if origin or destination are a {@link Terminal}, false otherwise
     */
    public boolean hasTerminal() {

        return getOrigin() instanceof Terminal || getDestination() instanceof Terminal;
    }


    /**
     * Checks whether this {@link RoutePart} has a {@link Seaport}.
     *
     * @return  true if origin or destination are a {@link Seaport}, false otherwise
     */
    public boolean hasSeaport() {

        return getOrigin() instanceof Seaport || getDestination() instanceof Seaport;
    }


    /**
     * Finds this {@link RoutePart}'s {@link Terminal}.
     *
     * @return  the associated {@link Terminal}
     *
     * @throws  IllegalStateException  if neither origin nor destination are a {@link Terminal}
     */
    public Terminal findTerminal() {

        if (getOrigin() instanceof Terminal) {
            return (Terminal) getOrigin();
        } else if (getDestination() instanceof Terminal) {
            return (Terminal) getDestination();
        }

        throw new IllegalStateException("Neither origin nor destination is a Terminal");
    }


    /**
     * Finds this {@link RoutePart}'s {@link Seaport}.
     *
     * @return  the associated {@link Seaport}
     *
     * @throws  IllegalStateException  if neither origin nor destination are a {@link Seaport}
     */
    public Seaport findSeaport() {

        if (getOrigin() instanceof Seaport) {
            return (Seaport) getOrigin();
        } else if (getDestination() instanceof Seaport) {
            return (Seaport) getDestination();
        }

        throw new IllegalStateException("Neither origin nor destination is a SeaPort");
    }


    /**
     * Returns a copy of the {@link RoutePart} without {@link RouteData}.
     *
     * @return  {@link net.contargo.iris.route.RoutePart} without {@link net.contargo.iris.route.RouteData}
     */
    public RoutePart copyWithoutData() {

        return new RoutePart(routeType, origin, destination, containerType, containerState);
    }
}
