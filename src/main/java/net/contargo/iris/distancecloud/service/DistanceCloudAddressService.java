package net.contargo.iris.distancecloud.service;

import net.contargo.iris.distancecloud.DistanceCloudAddress;

import java.math.BigInteger;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public interface DistanceCloudAddressService {

    DistanceCloudAddress getAddressInCloud(BigInteger terminalUid, BigInteger staticAddressUid);
}
