package net.contargo.iris.address.dto;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a list of addresses to the API as List of {@link AddressDto} with parent Address.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
public class AddressListDto {

    private final List<AddressDto> addresses;
    private AddressDto parentAddress;

    public AddressListDto(String name, List<AddressDto> addresses) {

        Address address = new Address(name);
        this.parentAddress = new AddressDto(address);
        this.addresses = addresses;
    }


    public AddressListDto() {

        addresses = new ArrayList<>();
    }


    public AddressListDto(AddressList addressList) {

        this.parentAddress = new AddressDto(addressList.getParentAddress());

        this.addresses = new ArrayList<>();

        for (Address adr : addressList.getAddresses()) {
            this.addresses.add(new AddressDto(adr));
        }
    }

    public AddressDto getParentAddress() {

        return parentAddress;
    }


    public List<AddressDto> getAddresses() {

        return addresses;
    }
}
