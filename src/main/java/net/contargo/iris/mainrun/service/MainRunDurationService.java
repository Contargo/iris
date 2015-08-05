package net.contargo.iris.mainrun.service;

import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.SubRoutePart;

import java.math.BigDecimal;


/**
 * @author  Sven Mueller - mueller@synyx.de
 */
public interface MainRunDurationService {

    /**
     * Computes the duration of a {@link RoutePart}.
     *
     * @param  routePart  a {@link RoutePart}
     * @param  mainrunConnection
     *
     * @return  the duration of {@code routePart} if it's a main run, 0 if it's not
     */
    BigDecimal getMainRunRoutePartDuration(MainRunConnection mainrunConnection, RoutePart routePart);


    BigDecimal getSubRoutePartDuration(AbstractSubConnection subConnection, SubRoutePart subRoutePart,
        RoutePart.Direction direction);
}
