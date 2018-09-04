package net.contargo.iris.route.service;

import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.connection.dto.RoutePartDto;


/**
 * Default implementation of {@link RouteUrlSerializationService}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteUrlSerializationServiceImpl implements RouteUrlSerializationService {

    private static final String DATA_PARTS = "&data.parts[";

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
        }

        route.setUrl(url.toString());
    }
}
