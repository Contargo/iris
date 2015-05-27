package net.contargo.iris.address.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.nominatim.service.AddressService;
import net.contargo.iris.address.service.AddressServiceWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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

        AddressListDto osmList = new AddressListDto("Result", Arrays.asList(addressDto));

        listOfLists = Arrays.asList(osmList);

        return listOfLists;
    }


    @Override
    public AddressDto getAddressForGeoLocation(GeoLocation location) {

        Address address = addressServiceWrapper.getAddressForGeoLocation(location);

        return address == null ? null : new AddressDto(address);
    }


    @Override
    public List<AddressListDto> getAddressesByDetails(Map<String, String> addressDetails) {

        List<AddressList> addressListList = addressServiceWrapper.getAddressesByDetails(addressDetails);

        List<AddressListDto> addressListDtoList = new ArrayList<>();

        for (AddressList addressList : addressListList) {
            addressListDtoList.add(new AddressListDto(addressList));
        }

        return addressListDtoList;
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

        List<Address> addressList = addressService.getAddressesWherePlaceIsIn(placeId);

        List<AddressDto> addressDtoList = new ArrayList<>();

        for (Address address : addressList) {
            addressDtoList.add(new AddressDto(address)); // NOSONAR Instantiating object is necessary
        }

        return addressDtoList;
    }
}
