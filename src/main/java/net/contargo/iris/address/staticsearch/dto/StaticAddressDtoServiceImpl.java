package net.contargo.iris.address.staticsearch.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.AddressListDto;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


/**
 * Default implementation of the {@link StaticAddressDtoService}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class StaticAddressDtoServiceImpl implements StaticAddressDtoService {

    private final StaticAddressService staticAddressService;

    public StaticAddressDtoServiceImpl(StaticAddressService staticAddressService) {

        this.staticAddressService = staticAddressService;
    }

    @Override
    public List<AddressDto> getAddressesByDetails(String postalCode, String city, String country) {

        return staticAddressService.getAddressesByDetails(postalCode, city, country).stream().map(staticAddress ->
                    new AddressDto(staticAddress.toAddress())).collect(toList());
    }


    @Override
    public List<AddressListDto> getStaticAddressByGeolocation(GeoLocation location) {

        return staticAddressService.getAddressListListForGeolocation(location)
            .stream()
            .map(AddressListDto::new)
            .collect(toList());
    }


    @Override
    public List<StaticAddressDto> getStaticAddressByBoundingBox(GeoLocation location, Double distance) {

        return staticAddressService.getAddressesInBoundingBox(location, distance)
            .stream()
            .map(StaticAddressDto::new)
            .collect(Collectors.toList());
    }
}
