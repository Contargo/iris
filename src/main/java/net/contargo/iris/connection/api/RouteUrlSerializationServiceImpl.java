package net.contargo.iris.connection.api;

import net.contargo.iris.connection.dto.RouteDto;


/**
 * Default implementation of {@link RouteUrlSerializationService}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteUrlSerializationServiceImpl implements RouteUrlSerializationService {

    public static final String DATA_PARTS = "&data.parts[";

    @Override
    public void serializeUrl(RouteDto route, String baseUrlRoute, String baseUrlRoutePart) {

        StringBuilder url = new StringBuilder(baseUrlRoute);

        url.append('?');
        url.append("terminal=").append(route.getResponsibleTerminal().getUniqueId()).append('&');

        for (int i = 0; i < route.getData().getParts().size(); i++) {
            if (i > 0) {
                url.append('&');
            }

            url.append("data.parts[").append(i).append("].origin.longitude=");
            url.append(route.getData().getParts().get(i).getOrigin().getLongitude().doubleValue());

            url.append(DATA_PARTS).append(i).append("].origin.latitude=");
            url.append(route.getData().getParts().get(i).getOrigin().getLatitude().doubleValue());

            url.append(DATA_PARTS).append(i).append("].destination.longitude=");
            url.append(route.getData().getParts().get(i).getDestination().getLongitude().doubleValue());

            url.append(DATA_PARTS).append(i).append("].destination.latitude=");
            url.append(route.getData().getParts().get(i).getDestination().getLatitude().doubleValue());

            url.append(DATA_PARTS).append(i).append("].routeType=");
            url.append(route.getData().getParts().get(i).getRouteType());

            url.append(DATA_PARTS).append(i).append("].containerType=");
            url.append(route.getData().getParts().get(i).getContainerType());

            url.append(DATA_PARTS).append(i).append("].containerState=");
            url.append(route.getData().getParts().get(i).getContainerState());
        }

        route.setUrl(url.toString());
    }
}
