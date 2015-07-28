package net.contargo.iris.connection.service;

import com.google.common.collect.Lists;

import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.SubRoutePart;
import net.contargo.iris.seaport.Seaport;

import java.util.List;


/**
 * Checks whether a list of {@link MainRunConnection}s contains one such connection that matches with a given list of
 * {@link SubRoutePart} in regard of origin, destination and direction.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class BargeRailConnectionFinderService {

    MainRunConnection findMatchingBargeRailConnection(List<MainRunConnection> connections,
        List<SubRoutePart> subRouteParts) {

        for (MainRunConnection connection : connections) {
            if (connection.getSubConnections().size() != subRouteParts.size()) {
                continue;
            }

            List<AbstractSubConnection> subConnectionList = connection.getSubConnections();

            boolean reverse = false;

            if (!(subRouteParts.get(0).getOrigin() instanceof Seaport)) {
                subConnectionList = Lists.reverse(connection.getSubConnections());
                reverse = true;
            }

            if (subRoutePartsAndSubconnectionsMatch(subRouteParts, subConnectionList, reverse)) {
                return connection;
            }
        }

        return null;
    }


    private boolean subRoutePartsAndSubconnectionsMatch(List<SubRoutePart> subRouteParts,
        List<AbstractSubConnection> subConnections, boolean reverse) {

        int i = 0;

        while (i < subRouteParts.size()) {
            SubRoutePart subRoutePart = subRouteParts.get(i);
            AbstractSubConnection subConnection = subConnections.get(i);

            if (!subConnection.matchesOriginAndDestination(subRoutePart.getOrigin(), subRoutePart.getDestination(),
                        reverse)) {
                return false;
            }

            i++;
        }

        return true;
    }
}
