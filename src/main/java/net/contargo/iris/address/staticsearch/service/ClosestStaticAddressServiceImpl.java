package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class ClosestStaticAddressServiceImpl implements ClosestStaticAddressService {

    private static final double UPPER_RADIUS_LIMIT = 20.0;
    private final NominatimToStaticAddressMapper nominatimToStaticAddressMapper;
    private final StaticAddressService staticAddressService;
    private final StaticAddressSelector staticAddressSelector;

    public ClosestStaticAddressServiceImpl(NominatimToStaticAddressMapper nominatimToStaticAddressMapper,
        StaticAddressService staticAddressService, StaticAddressSelector staticAddressSelector) {

        this.nominatimToStaticAddressMapper = nominatimToStaticAddressMapper;
        this.staticAddressService = staticAddressService;
        this.staticAddressSelector = staticAddressSelector;
    }

    @Override
    public StaticAddress get(String postalCode, String city, String country, BigDecimal latitude,
        BigDecimal longitude) {

        Address address = generateNominatimAddress(postalCode, city, country, latitude, longitude);

        return lookupByDetails(address).orElseGet(() -> lookupByAirDistance(address));
    }


    private Optional<StaticAddress> lookupByDetails(Address address) {

        try {
            return Optional.of(nominatimToStaticAddressMapper.map(address));
        } catch (NominatimToStaticAddressMapperException e) {
            return Optional.empty();
        }
    }


    private StaticAddress lookupByAirDistance(Address address) {

        StaticAddress result = null;
        double radius = 5.0;

        while (result == null) {
            List<StaticAddress> addresses = staticAddressService.getAddressesInBoundingBox(address, radius);

            if (!addresses.isEmpty()) {
                result = staticAddressSelector.select(addresses, address);
            }

            if (radius > UPPER_RADIUS_LIMIT) {
                throw new NominatimToStaticAddressMapperException(address);
            }

            radius += 5.0;
        }

        return result;
    }


    private static Address generateNominatimAddress(String postalCode, String city, String country,
        BigDecimal latitude, BigDecimal longitude) {

        Map<String, String> addressDetails = new HashMap<>();
        addressDetails.put("city", city);
        addressDetails.put("postcode", postalCode);
        addressDetails.put("country_code", country);

        Address address = new Address(latitude, longitude);
        address.setAddress(addressDetails);

        return address;
    }
}
