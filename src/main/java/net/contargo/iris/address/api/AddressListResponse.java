package net.contargo.iris.address.api;

import net.contargo.iris.address.dto.AddressDto;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
class AddressListResponse extends ResourceSupport {

    private final List<AddressDto> addresses;

    public AddressListResponse(List<AddressDto> addresses) {

        this.addresses = addresses;
    }


    public AddressListResponse() {

        addresses = new ArrayList<>();
    }

    public List<AddressDto> getAddresses() {

        return addresses;
    }
}
