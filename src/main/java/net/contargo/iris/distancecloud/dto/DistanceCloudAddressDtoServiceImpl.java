package net.contargo.iris.distancecloud.dto;

import net.contargo.iris.distancecloud.service.DistanceCloudAddressService;

import java.math.BigInteger;


/**
 * DTO service for distance cloud addresses.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class DistanceCloudAddressDtoServiceImpl implements DistanceCloudAddressDtoService {

    private final DistanceCloudAddressService distanceCloudAddressService;

    public DistanceCloudAddressDtoServiceImpl(DistanceCloudAddressService distanceCloudAddressService) {

        this.distanceCloudAddressService = distanceCloudAddressService;
    }

    @Override
    public DistanceCloudAddressDto getAddressInCloud(BigInteger terminalUid, BigInteger staticAddressUid) {

        return new DistanceCloudAddressDto(distanceCloudAddressService.getAddressInCloud(terminalUid,
                    staticAddressUid));
    }
}
