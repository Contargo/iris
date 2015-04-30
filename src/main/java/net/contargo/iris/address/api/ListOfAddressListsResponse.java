package net.contargo.iris.address.api;

import net.contargo.iris.address.dto.AddressListDto;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
public class ListOfAddressListsResponse extends ResourceSupport {

    private final List<AddressListDto> addresses;

    public ListOfAddressListsResponse(List<AddressListDto> addresses) {

        this.addresses = addresses;
    }


    public ListOfAddressListsResponse() {

        addresses = new ArrayList<>();
    }

    public List<AddressListDto> getAddresses() {

        return addresses;
    }
}
