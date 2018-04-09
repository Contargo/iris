package net.contargo.iris.route2.api;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.service.AddressServiceWrapper;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.service.TerminalService;

import org.springframework.core.convert.converter.Converter;

import static net.contargo.iris.route2.api.RoutePartNodeDtoType.ADDRESS;
import static net.contargo.iris.route2.api.RoutePartNodeDtoType.GEOLOCATION;
import static net.contargo.iris.route2.api.RoutePartNodeDtoType.SEAPORT;
import static net.contargo.iris.route2.api.RoutePartNodeDtoType.TERMINAL;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
class NodeDtoToGeoLocationConverter implements Converter<RoutePartNodeDto, GeoLocation> {

    private final SeaportService seaportService;
    private final TerminalService terminalService;
    private final AddressServiceWrapper addressServiceWrapper;

    NodeDtoToGeoLocationConverter(SeaportService seaportService, TerminalService terminalService,
        AddressServiceWrapper addressServiceWrapper) {

        this.seaportService = seaportService;
        this.terminalService = terminalService;
        this.addressServiceWrapper = addressServiceWrapper;
    }

    @Override
    public GeoLocation convert(RoutePartNodeDto node) {

        GeoLocation geoLocation;

        switch (node.getType()) {
            case SEAPORT:
                geoLocation = seaportService.getByUniqueId(node.getUuid());
                break;

            case TERMINAL:
                geoLocation = terminalService.getByUniqueId(node.getUuid());
                break;

            case ADDRESS:
                geoLocation = addressServiceWrapper.getByHashKey(node.getHashKey());
                break;

            case GEOLOCATION:
                geoLocation = new GeoLocation(node.getLat(), node.getLon());
                break;

            default:
                throw new IllegalArgumentException("Unsupported node type: " + node.getType());
        }

        if (geoLocation == null) {
            throw new IllegalArgumentException("Could not convert to a GeoLocation: " + node);
        }

        return geoLocation;
    }
}
