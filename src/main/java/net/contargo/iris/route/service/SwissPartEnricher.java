package net.contargo.iris.route.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.service.NominatimToStaticAddressMapper;
import net.contargo.iris.address.staticsearch.service.NominatimToStaticAddressMapperException;
import net.contargo.iris.route.RoutePart;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Enricher responsible for replacing swiss nomainatim loadingplaces with static addresses.
 *
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class SwissPartEnricher implements RoutePartEnricher {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final NominatimToStaticAddressMapper nominatimToStaticAddressMapper;

    public SwissPartEnricher(NominatimToStaticAddressMapper nominatimToStaticAddressMapper) {

        this.nominatimToStaticAddressMapper = nominatimToStaticAddressMapper;
    }

    @Override
    public void enrich(RoutePart routePart, EnricherContext context) throws CriticalEnricherException {

        GeoLocation origin = routePart.getOrigin();
        GeoLocation destination = routePart.getDestination();

        try {
            if (isLoadingPlace(origin)) {
                Address address = (Address) origin;

                if (address.inSwitzerland() && !address.isStatic()) {
                    routePart.setOrigin(nominatimToStaticAddressMapper.map(address).toAddress());
                }
            }

            if (isLoadingPlace(destination)) {
                Address address = (Address) destination;

                if (address.inSwitzerland() && !address.isStatic()) {
                    routePart.setDestination(nominatimToStaticAddressMapper.map(address).toAddress());
                }
            }
        } catch (NominatimToStaticAddressMapperException e) {
            context.addError("swiss-route", "could not map from nominatim to static address");
            LOG.warn("Failed to map from nominatim address to static address: {}", e.getAddress());
        }
    }


    private boolean isLoadingPlace(GeoLocation geoLocation) {

        return geoLocation instanceof Address;
    }
}
