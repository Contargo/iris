package net.contargo.iris.address.api;

import net.contargo.iris.address.dto.AddressDto;

import org.springframework.hateoas.ResourceSupport;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
class AddressResponse extends ResourceSupport {

    private AddressDto address;

    public AddressResponse(AddressDto address) {

        this.address = address;
    }


    public AddressResponse() {

        // Needed for Jackson Mapping
    }

    public AddressDto getAddress() {

        return address;
    }
}
