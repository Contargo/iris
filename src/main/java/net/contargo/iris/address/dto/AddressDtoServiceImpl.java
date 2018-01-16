package net.contargo.iris.address.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.nominatim.service.AddressService;
import net.contargo.iris.address.service.AddressServiceWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public class AddressDtoServiceImpl implements AddressDtoService {

    private final AddressService addressService;
    private final AddressServiceWrapper addressServiceWrapper;

    public AddressDtoServiceImpl(AddressService addressService, AddressServiceWrapper addressServiceWrapper) {

        this.addressService = addressService;
        this.addressServiceWrapper = addressServiceWrapper;
    }

    @Override
    public AddressDto getAddressByOsmId(long osmId) {

        Address address = addressService.getAddressByOsmId(osmId);

        return address == null ? null : new AddressDto(address);
    }


    @Override
    public List<AddressListDto> wrapInListOfAddressLists(AddressDto addressDto) {

        List<AddressListDto> listOfLists;

        AddressListDto osmList = new AddressListDto("Result", singletonList(addressDto));

        listOfLists = singletonList(osmList);

        return listOfLists;
    }


    @Override
    public AddressDto getAddressForGeoLocation(GeoLocation location) {

        Address address = addressServiceWrapper.getAddressForGeoLocation(location);

        return address == null ? null : new AddressDto(address);
    }


    @Override
    public List<AddressListDto> getAddressesByDetails(Map<String, String> addressDetails) {

        return addressServiceWrapper.getAddressesByDetails(addressDetails)
            .stream()
            .map(AddressListDto::new)
            .collect(toList());
    }


    @Override
    public List<AddressDto> getAddressesByDetailsPlain(Map<String, String> addressDetails) {

        List<AddressListDto> addressListList = getAddressesByDetails(addressDetails);
        List<AddressDto> addressDtoList = new ArrayList<>();

        for (AddressListDto addressListDto : addressListList) {
            addressDtoList.addAll(addressListDto.getAddresses());
        }

        return addressDtoList;
    }


    @Override
    public List<AddressDto> getAddressesWherePlaceIsIn(Long placeId) {

        return addressService.getAddressesWherePlaceIsIn(placeId).stream().map(AddressDto::new).collect(toList());
    }


    @Override
    public AddressDto getAddressesByHashKey(String hashKey) {

        return new AddressDto(addressServiceWrapper.getByHashKey(hashKey));
    }


    @Override
    public List<AddressDto> getAddressesByQuery(String query) {

        return addressServiceWrapper.getAddressesByQuery(query).stream().map(AddressDto::new).collect(toList());
    }
}
