package net.contargo.iris.address.dto;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * Represents a list of addresses to the API as List of {@link AddressDto} with parent Address.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
public class AddressListDto {

    private final List<AddressDto> addresses;
    private AddressDto parentAddress;

    public AddressListDto() {

        addresses = new ArrayList<>();
    }


    public AddressListDto(String name, List<AddressDto> addresses) {

        this.parentAddress = new AddressDto(new Address(name));
        this.addresses = addresses;
    }


    public AddressListDto(AddressList addressList) {

        this.parentAddress = new AddressDto(addressList.getParentAddress());
        this.addresses = new ArrayList<>();
        this.addresses.addAll(addressList.getAddresses().stream().map(AddressDto::new).collect(toList()));
    }

    public AddressDto getParentAddress() {

        return parentAddress;
    }


    public List<AddressDto> getAddresses() {

        return addresses;
    }
}
