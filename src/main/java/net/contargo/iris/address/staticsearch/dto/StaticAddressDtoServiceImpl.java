package net.contargo.iris.address.staticsearch.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.AddressListDto;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public class StaticAddressDtoServiceImpl implements StaticAddressDtoService {

    private final StaticAddressService staticAddressService;

    public StaticAddressDtoServiceImpl(StaticAddressService staticAddressService) {

        this.staticAddressService = staticAddressService;
    }

    @Override
    public List<AddressDto> getAddressesByDetails(String postalCode, String city, String country) {

        List<StaticAddress> addressList = staticAddressService.getAddressesByDetails(postalCode, city, country);

        return convertStaticAddressListToDTOList(addressList);
    }


    @Override
    public List<AddressDto> getAll() {

        List<StaticAddress> staticAddresses = staticAddressService.getAll();

        return convertStaticAddressListToDTOList(staticAddresses);
    }


    private List<AddressDto> convertStaticAddressListToDTOList(List<StaticAddress> addressList) {

        List<AddressDto> addressDtoList = new ArrayList<>();

        for (StaticAddress staticAddress : addressList) {
            addressDtoList.add(new AddressDto(staticAddress.toAddress()));
        }

        return addressDtoList;
    }


    @Override
    public List<AddressListDto> getStaticAddressByGeolocation(GeoLocation location) {

        List<AddressList> addressListList = staticAddressService.getAddressListListForGeolocation(location);

        List<AddressListDto> addressListDtoList = new ArrayList<>();

        for (AddressList addressList : addressListList) {
            addressListDtoList.add(new AddressListDto(addressList)); // NOSONAR
        }

        return addressListDtoList;
    }


    @Override
    public List<BigInteger> getStaticAddressByBoundingBox(GeoLocation location, Double distance) {

        List<StaticAddress> staticAddresses = staticAddressService.getAddressesInBoundingBox(location, distance);

        List<BigInteger> result = new ArrayList<>();

        for (StaticAddress staticAddress : staticAddresses) {
            result.add(staticAddress.getUniqueId());
        }

        return result;
    }
}
