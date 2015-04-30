package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.gis.service.GisService;

import java.math.BigDecimal;

import java.util.Comparator;


/**
 * Comparator to sort Addresses by distance from the Center of EU to sort the ones outside of the EU to the end.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
class AddressSorter implements Comparator<Address> {

    private final GisService gisService;

    public AddressSorter(GisService gisService) {

        this.gisService = gisService;
    }

    @Override
    public int compare(Address o1, Address o2) {

        BigDecimal distanceOfO1 = gisService.calcAirLineDistInMeters(o1, GisService.CENTER_OF_THE_EUROPEAN_UNION);
        BigDecimal distanceOfO2 = gisService.calcAirLineDistInMeters(o2, GisService.CENTER_OF_THE_EUROPEAN_UNION);

        return distanceOfO1.compareTo(distanceOfO2);
    }
}
