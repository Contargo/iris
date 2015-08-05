package net.contargo.iris.connection.service;

import com.google.common.collect.Lists;

import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.SubRoutePart;
import net.contargo.iris.seaport.Seaport;

import java.util.List;


/**
 * Service providing functionality to find a {@link MainRunConnection} corresponding to {@link SubRoutePart}s.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class BargeRailConnectionFinderService {

    /**
     * Checks whether a list of {@link MainRunConnection}s contains one such connection that matches with a given list
     * of {@link SubRoutePart}s in regard of origin, destination and direction.
     *
     * @param  connections  a list of {@link MainRunConnection} candidates to be checked
     * @param  subRouteParts  a list of {@link SubRoutePart}s to check against
     *
     * @return  a matching {@link MainRunConnection} or null
     */
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
