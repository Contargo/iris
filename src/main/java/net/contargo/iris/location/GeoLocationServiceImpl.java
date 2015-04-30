package net.contargo.iris.location;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.service.AddressServiceWrapper;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.service.TerminalService;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Provides implementations related to {@link net.contargo.iris.GeoLocation} entities.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class GeoLocationServiceImpl implements GeoLocationService {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final TerminalService terminalService;
    private final SeaportService seaportService;
    private final AddressServiceWrapper addressServiceWrapper;

    public GeoLocationServiceImpl(TerminalService terminalService, SeaportService seaportService,
        AddressServiceWrapper addressServiceWrapper) {

        this.terminalService = terminalService;
        this.seaportService = seaportService;
        this.addressServiceWrapper = addressServiceWrapper;
    }

    @Override
    public GeoLocation getDetailedGeoLocation(GeoLocation location) {

        GeoLocation enrichedLocation = seaportService.getByGeoLocation(location);

        if (enrichedLocation == null) {
            enrichedLocation = terminalService.getByGeoLocation(location);
        }

        // try to resolve address from cache
        if (enrichedLocation == null) {
            Address address = addressServiceWrapper.getAddressForGeoLocation(location);

            if (null == address) {
                LOG.warn("Couldn't resolve address for {}", location);
            } else {
                enrichedLocation = address;
            }
        }

        // if nothing found, set given location to enriched one
        if (enrichedLocation == null) {
            enrichedLocation = location;
        }

        return enrichedLocation;
    }
}
