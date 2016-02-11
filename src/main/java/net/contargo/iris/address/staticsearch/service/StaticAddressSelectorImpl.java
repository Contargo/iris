package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.gis.service.GisService;

import java.math.BigDecimal;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class StaticAddressSelectorImpl implements StaticAddressSelector {

    private final GisService gisService;

    public StaticAddressSelectorImpl(GisService gisService) {

        this.gisService = gisService;
    }

    @Override
    public StaticAddress select(List<StaticAddress> staticAddresses, Address address) {

        Map<BigDecimal, List<StaticAddress>> distanceToStaticAddressesMap = staticAddresses.stream()
            .collect(groupingBy(airlineDistance(address)));

        Optional<Map.Entry<BigDecimal, List<StaticAddress>>> shortest = distanceToStaticAddressesMap.entrySet()
            .stream()
            .sorted(byDistance())
            .findFirst();

        return shortest.get().getValue().get(0);
    }


    private Comparator<Map.Entry<BigDecimal, List<StaticAddress>>> byDistance() {

        return (a, b) -> a.getKey().compareTo(b.getKey());
    }


    private Function<StaticAddress, BigDecimal> airlineDistance(Address address) {

        return
            staticAddress -> {
            GeoLocation addressGeoLocation = new GeoLocation(address.getLatitude(), address.getLongitude());
            GeoLocation staticGeoLocation = new GeoLocation(staticAddress.getLatitude(), staticAddress.getLongitude());

            return gisService.calcAirLineDistInMeters(addressGeoLocation, staticGeoLocation);
        };
    }
}
