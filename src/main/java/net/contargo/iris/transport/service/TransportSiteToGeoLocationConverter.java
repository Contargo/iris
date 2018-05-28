package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.service.TerminalService;
import net.contargo.iris.transport.api.TransportResponseDto;

import org.springframework.core.convert.converter.Converter;

import java.math.BigInteger;


/**
 * @author  Oliver Messner - messner@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
class TransportSiteToGeoLocationConverter implements Converter<TransportResponseDto.TransportSite, GeoLocation> {

    private final SeaportService seaportService;
    private final TerminalService terminalService;

    TransportSiteToGeoLocationConverter(SeaportService seaportService, TerminalService terminalService) {

        this.seaportService = seaportService;
        this.terminalService = terminalService;
    }

    @Override
    public GeoLocation convert(TransportResponseDto.TransportSite site) {

        GeoLocation geoLocation;

        switch (site.type) {
            case SEAPORT:
                geoLocation = seaportService.getByUniqueId(new BigInteger(site.uuid));
                break;

            case TERMINAL:
                geoLocation = terminalService.getByUniqueId(new BigInteger(site.uuid));
                break;

            case ADDRESS:
                geoLocation = new GeoLocation(site.lat, site.lon);
                break;

            default:
                throw new IllegalArgumentException("Unsupported site type: " + site.type);
        }

        if (geoLocation == null) {
            throw new IllegalArgumentException("Could not convert to a GeoLocation: " + site);
        }

        return geoLocation;
    }
}
