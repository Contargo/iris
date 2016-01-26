package net.contargo.iris.route;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.terminal.Terminal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.route.RouteDirection.EXPORT;
import static net.contargo.iris.route.RouteDirection.IMPORT;
import static net.contargo.iris.route.RouteProduct.ONEWAY;
import static net.contargo.iris.route.RouteProduct.ROUNDTRIP;
import static net.contargo.iris.route.RouteType.TRUCK;


/**
 * Represents a combined routing, consisting of multiple RoutePart objects.
 *
 * <p>A typical example would be a Route consisting of a BargeRoute, followed by a TruckRoute.</p>
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Route {

    private RouteData data = new RouteData();
    private Terminal responsibleTerminal;
    private Map<String, String> errors = new HashMap<>();

    public RouteData getData() {

        return data;
    }


    public void setData(RouteData data) {

        this.data = data;
    }


    public Map<String, String> getErrors() {

        return errors;
    }


    public Terminal getResponsibleTerminal() {

        return responsibleTerminal;
    }


    public void setResponsibleTerminal(Terminal responsibleTerminal) {

        this.responsibleTerminal = responsibleTerminal;
    }


    public void setErrors(Map<String, String> errors) {

        this.errors = errors;
    }


    /**
     * Creates a name for this {@link Route} out of its parts.
     *
     * @return  a name representing this {@link Route}
     */
    public String getName() {

        StringBuilder routeName = new StringBuilder();

        for (int i = 0; i < getData().getParts().size(); i++) {
            if (i > 0) {
                routeName.append(" -> ");
            }

            routeName.append(getData().getParts().get(i).getOrigin().getNiceName());

            if (i == (getData().getParts().size() - 1)) {
                routeName.append(" -> ").append(getData().getParts().get(i).getDestination().getNiceName());
            }
        }

        return routeName.toString();
    }


    public String getShortName() {

        for (RoutePart routePart : getData().getParts()) {
            if (routePart.getOrigin() instanceof Terminal) {
                return "via " + ((Terminal) routePart.getOrigin()).getName();
            } else if (routePart.getDestination() instanceof Terminal) {
                return "via " + ((Terminal) routePart.getDestination()).getName();
            }
        }

        return getName();
    }


    public RouteProduct getProduct() {

        List<RoutePart> routeParts = getData().getParts();

        if (routeParts.isEmpty()) {
            return ONEWAY;
        }

        // if first part and last part of route are identical, than the route is a round trip
        GeoLocation start = routeParts.get(0).getOrigin();
        GeoLocation end = routeParts.get(routeParts.size() - 1).getDestination();

        if (start.equals(end)) {
            return ROUNDTRIP;
        }

        return ONEWAY;
    }


    public boolean isRoundTrip() {

        return ROUNDTRIP.equals(getProduct());
    }


    public RouteDirection getDirection() {

        List<RoutePart> parts = getData().getParts();

        if (parts.isEmpty()) {
            return null;
        }

        ContainerState currentState = parts.get(0).getContainerState();
        RouteDirection direction = null;

        for (RoutePart part : parts) {
            ContainerState newState = part.getContainerState();

            if (null != newState && null != currentState) {
                if (!currentState.equals(newState)) {
                    RouteDirection currentDirection = determineRouteDirection(currentState, newState);

                    if (hasDirectionChanged(direction, currentDirection)) {
                        return null;
                    } else {
                        direction = currentDirection;
                    }
                }

                currentState = newState;
            }
        }

        return direction;
    }


    private boolean hasDirectionChanged(RouteDirection direction, RouteDirection currentDirection) {

        return direction != null && !direction.equals(currentDirection);
    }


    private RouteDirection determineRouteDirection(ContainerState oldState, ContainerState newState) {

        if (EMPTY.equals(oldState) && FULL.equals(newState)) {
            return EXPORT;
        } else if (FULL.equals(oldState) && EMPTY.equals(newState)) {
            return IMPORT;
        } else {
            return null;
        }
    }


    /**
     * Returns true if this route is an triangle routing or false if it is not.
     *
     * @return  true if triangle routing, else false
     */
    @JsonIgnore
    public Boolean isTriangle() {

        TruckRouteParts onewayTruckParts = data.getOnewayTruckParts();
        List<RoutePart> truckParts = data.getRoutePartsOfType(TRUCK);

        return onewayTruckParts.getTruckRoutePartList().size() == truckParts.size();
    }


    /**
     * A direct truck route is a {@link Route} that only contains {@link RoutePart}s with the {@link RouteType} 'TRUCK'.
     *
     * @return  true if the {@link Route} is a direct truck route, otherwise false
     */
    public boolean isDirectTruckRoute() {

        return data.getRoutePartsOfType(TRUCK).size() == data.getParts().size();
    }
}
