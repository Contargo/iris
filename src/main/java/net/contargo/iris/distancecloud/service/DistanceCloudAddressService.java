package net.contargo.iris.distancecloud.service;

import net.contargo.iris.distancecloud.DistanceCloudAddress;

import java.math.BigInteger;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public interface DistanceCloudAddressService {

    /**
     * Calculates the {@link DistanceCloudAddress} between the given {@link net.contargo.iris.terminal.Terminal} by the
     * represented terminalUid and a {@link net.contargo.iris.address.staticsearch.StaticAddress} represented by the
     * staticAddressUid.
     *
     * <h1>RouteDataRevision</h1> {@link net.contargo.iris.routedatarevision.RouteDataRevision} will be used for the
     * distance attributes if available
     *
     * @param  terminalUid  represents the {@link net.contargo.iris.terminal.Terminal}
     * @param  staticAddressUid  represents the {@link net.contargo.iris.address.staticsearch.StaticAddress}
     *
     * @return  a {@link DistanceCloudAddress} which contains address and distance information
     */
    DistanceCloudAddress getAddressInCloud(BigInteger terminalUid, BigInteger staticAddressUid);
}
