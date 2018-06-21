package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.service.TerminalService;
import net.contargo.iris.transport.api.TransportStop;

import org.springframework.core.convert.converter.Converter;

import java.math.BigInteger;


/**
 * @author  Oliver Messner - messner@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
class TransportStopToGeoLocationConverter implements Converter<TransportStop, GeoLocation> {

    private final SeaportService seaportService;
    private final TerminalService terminalService;

    TransportStopToGeoLocationConverter(SeaportService seaportService, TerminalService terminalService) {

        this.seaportService = seaportService;
        this.terminalService = terminalService;
    }

    @Override
    public GeoLocation convert(TransportStop stop) {

        GeoLocation geoLocation;

        switch (stop.type) {
            case SEAPORT:
                geoLocation = seaportService.getByUniqueId(new BigInteger(stop.uuid));
                break;

            case TERMINAL:
                geoLocation = terminalService.getByUniqueId(new BigInteger(stop.uuid));
                break;

            case ADDRESS:
                geoLocation = new GeoLocation(stop.lat, stop.lon);
                break;

            default:
                throw new IllegalArgumentException("Unsupported stop type: " + stop.type);
        }

        if (geoLocation == null) {
            throw new IllegalArgumentException("Could not convert to a GeoLocation: " + stop);
        }

        return geoLocation;
    }
}
