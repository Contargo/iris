package net.contargo.iris.route;

import net.contargo.iris.address.Address;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Container Class for Routepartlist, containing only Truck-RouteParts.
 *
 * @author  Arnold Franke - franke@synyx.de
 */
public class TruckRouteParts {

    private List<RoutePart> truckRoutePartList = new ArrayList<>();

    public TruckRouteParts(List<RoutePart> truckRoutePartList) {

        for (RoutePart truckRoutePart : truckRoutePartList) {
            if (!truckRoutePart.isOfType(RouteType.TRUCK)) {
                throw new IllegalArgumentException("Can't add non-Truck Routepart to TruckRoutParts");
            }
        }

        this.truckRoutePartList = truckRoutePartList;
    }

    public List<RoutePart> getTruckRoutePartList() {

        return Collections.unmodifiableList(truckRoutePartList);
    }


    public void setTruckRoutePartList(List<RoutePart> truckRoutePartList) {

        this.truckRoutePartList = truckRoutePartList;
    }


    public void addTruckRoutePart(RoutePart truckRoutePart) {

        if (truckRoutePart.isOfType(RouteType.TRUCK)) {
            truckRoutePartList.add(truckRoutePart);
        } else {
            throw new IllegalArgumentException("Can't add non-Truck Routepart to TruckRoutParts");
        }
    }


    public Address extractDestinationAddress() {

        for (RoutePart routePart : truckRoutePartList) {
            if (routePart.getDestination() instanceof Address) {
                return (Address) routePart.getDestination();
            }
        }

        return null;
    }


    boolean isEqualRoundTrip() {

        if (truckRoutePartList.size() % 2 != 0) {
            return false;
        }

        for (int i = 0; i < truckRoutePartList.size(); i++) {
            RoutePart routePart = truckRoutePartList.get(i);
            RoutePart contraRoutePart = truckRoutePartList.get(truckRoutePartList.size() - 1 - i);

            if (!routePart.getOrigin().equals(contraRoutePart.getDestination())) {
                return false;
            }
        }

        return true;
    }


    public void reduceToOneway() {

        if (isEqualRoundTrip()) {
            truckRoutePartList.subList(truckRoutePartList.size() / 2, truckRoutePartList.size()).clear();
        }
    }
}
