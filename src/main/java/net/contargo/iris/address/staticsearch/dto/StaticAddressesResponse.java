package net.contargo.iris.address.staticsearch.dto;

import net.contargo.iris.address.dto.AddressDto;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public class StaticAddressesResponse extends ResourceSupport {

    private List<AddressDto> addressDtoList;

    public List<AddressDto> getAddressDtoList() {

        return addressDtoList;
    }


    public void setAddressDtoList(List<AddressDto> addressDtoList) {

        this.addressDtoList = addressDtoList;
    }
}
