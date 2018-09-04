
package net.contargo.iris.route.service;

import net.contargo.iris.address.nominatim.service.AddressResolutionException;
import net.contargo.iris.location.GeoLocationService;
import net.contargo.iris.route.RoutePart;


/**
 * {@link RoutePartEnricher} for {@link net.contargo.iris.GeoLocation}s.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
class GeoLocationPartEnricher implements RoutePartEnricher {

    private final GeoLocationService geoLocationService;

    GeoLocationPartEnricher(GeoLocationService geoLocationService) {

        this.geoLocationService = geoLocationService;
    }

    @Override
    public void enrich(RoutePart part, EnricherContext context) throws CriticalEnricherException {

        try {
            part.setOrigin(geoLocationService.getDetailedGeoLocation(part.getOrigin()));
            part.setDestination(geoLocationService.getDetailedGeoLocation(part.getDestination()));
        } catch (AddressResolutionException e) {
            throw new CriticalEnricherException("Not possible to enrich geolocation", e);
        }
    }
}
