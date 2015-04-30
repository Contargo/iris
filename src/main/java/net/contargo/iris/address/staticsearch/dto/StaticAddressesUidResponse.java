package net.contargo.iris.address.staticsearch.dto;

import org.springframework.hateoas.ResourceSupport;

import java.math.BigInteger;

import java.util.List;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public class StaticAddressesUidResponse extends ResourceSupport {

    private List<BigInteger> uids;

    public StaticAddressesUidResponse(List<BigInteger> staticAddressByBoundingBox) {

        uids = staticAddressByBoundingBox;
    }

    public List<BigInteger> getUids() {

        return uids;
    }


    public void setUids(List<BigInteger> uids) {

        this.uids = uids;
    }
}
