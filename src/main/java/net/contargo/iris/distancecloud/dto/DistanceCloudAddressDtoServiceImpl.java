package net.contargo.iris.distancecloud.dto;

import net.contargo.iris.distancecloud.DistanceCloudAddress;
import net.contargo.iris.distancecloud.service.DistanceCloudAddressService;

import java.math.BigInteger;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class DistanceCloudAddressDtoServiceImpl implements DistanceCloudAddressDtoService {

    private final DistanceCloudAddressService service;

    public DistanceCloudAddressDtoServiceImpl(DistanceCloudAddressService service) {

        this.service = service;
    }

    @Override
    public DistanceCloudAddressDto getAddressInCloud(BigInteger terminalUid, BigInteger staticAddressUid) {

        DistanceCloudAddress address = service.getAddressInCloud(terminalUid, staticAddressUid);

        return new DistanceCloudAddressDto(address);
    }
}
