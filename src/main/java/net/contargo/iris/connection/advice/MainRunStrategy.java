package net.contargo.iris.connection.advice;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;


/**
 * Strategy component for building a concrete route.
 *
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 * @see  MainRunAdvisor
 */
public interface MainRunStrategy {

    /**
     * Builds a {@link Route} based on given parameters.
     *
     * @param  connection  the connection the {@link Route} shall be based on
     * @param  destination  the {@link Route}'s destination
     * @param  containerType  the {@link Route}'s {@link ContainerType}
     *
     * @return  a {@link Route} matching the given criteria
     */
    Route getRoute(MainRunConnection connection, GeoLocation destination, ContainerType containerType);
}
