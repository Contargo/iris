package net.contargo.iris.distancecloud.dto;

import java.math.BigInteger;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public interface DistanceCloudAddressDtoService {

    DistanceCloudAddressDto getAddressInCloud(BigInteger terminalUid, BigInteger staticAddressUid);
}
