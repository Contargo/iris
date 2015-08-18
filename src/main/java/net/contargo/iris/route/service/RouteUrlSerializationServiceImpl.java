package net.contargo.iris.route.service;

import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.connection.dto.RoutePartDto;
import net.contargo.iris.connection.dto.SubRoutePartDto;


/**
 * Default implementation of {@link RouteUrlSerializationService}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteUrlSerializationServiceImpl implements RouteUrlSerializationService {

    public static final String DATA_PARTS = "&data.parts[";
    private static final String SUB_ROUTE_PARTS = "].subRouteParts[";

    @Override
    public void serializeUrl(RouteDto route, String baseUrlRoute, String baseUrlRoutePart) {

        StringBuilder url = new StringBuilder(baseUrlRoute);

        url.append('?');
        url.append("terminal=").append(route.getResponsibleTerminal().getUniqueId()).append('&');

        for (int i = 0; i < route.getData().getParts().size(); i++) {
            if (i > 0) {
                url.append('&');
            }

            RoutePartDto routePart = route.getData().getParts().get(i);

            url.append("data.parts[").append(i).append("].origin.longitude=");
            url.append(routePart.getOrigin().getLongitude().doubleValue());

            url.append(DATA_PARTS).append(i).append("].origin.latitude=");
            url.append(routePart.getOrigin().getLatitude().doubleValue());

            url.append(DATA_PARTS).append(i).append("].destination.longitude=");
            url.append(routePart.getDestination().getLongitude().doubleValue());

            url.append(DATA_PARTS).append(i).append("].destination.latitude=");
            url.append(routePart.getDestination().getLatitude().doubleValue());

            url.append(DATA_PARTS).append(i).append("].routeType=");
            url.append(routePart.getRouteType());

            url.append(DATA_PARTS).append(i).append("].containerType=");
            url.append(routePart.getContainerType());

            url.append(DATA_PARTS).append(i).append("].containerState=");
            url.append(routePart.getContainerState());

            for (int j = 0; j < routePart.getSubRouteParts().size(); j++) {
                SubRoutePartDto subRoutePart = routePart.getSubRouteParts().get(j);

                url.append(DATA_PARTS).append(i).append(SUB_ROUTE_PARTS).append(j).append("].origin.longitude=");
                url.append(subRoutePart.getOrigin().getLongitude());

                url.append(DATA_PARTS).append(i).append(SUB_ROUTE_PARTS).append(j).append("].origin.latitude=");
                url.append(subRoutePart.getOrigin().getLatitude());

                url.append(DATA_PARTS).append(i).append(SUB_ROUTE_PARTS).append(j).append("].destination.longitude=");
                url.append(subRoutePart.getDestination().getLongitude());

                url.append(DATA_PARTS).append(i).append(SUB_ROUTE_PARTS).append(j).append("].destination.latitude=");
                url.append(subRoutePart.getDestination().getLatitude());

                url.append(DATA_PARTS).append(i).append(SUB_ROUTE_PARTS).append(j).append("].routeType=");
                url.append(subRoutePart.getRouteType());
            }
        }

        route.setUrl(url.toString());
    }
}
