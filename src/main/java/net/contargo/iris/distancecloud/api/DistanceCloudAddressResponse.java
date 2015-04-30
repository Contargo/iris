package net.contargo.iris.distancecloud.api;

import net.contargo.iris.distancecloud.dto.DistanceCloudAddressDto;

import org.springframework.hateoas.ResourceSupport;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class DistanceCloudAddressResponse extends ResourceSupport {

    private DistanceCloudAddressDto address;

    public DistanceCloudAddressResponse(DistanceCloudAddressDto address) {

        this.address = address;
    }

    public DistanceCloudAddressDto getAddress() {

        return address;
    }


    public void setAddress(DistanceCloudAddressDto address) {

        this.address = address;
    }
}
