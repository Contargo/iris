package net.contargo.iris.distancecloud.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.persistence.StaticAddressRepository;
import net.contargo.iris.distancecloud.DistanceCloudAddress;
import net.contargo.iris.gis.service.GisService;
import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.OSRMNonRoutableRouteException;
import net.contargo.iris.truck.service.TruckRouteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class DistanceCloudAddressServiceImpl implements DistanceCloudAddressService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final BigDecimal MULTIPLIER = new BigDecimal(2);

    private final RoundingService roundingService;

    private final TruckRouteService truckRouteService;
    private final StaticAddressRepository repository;
    private final GisService gisService;
    private final TerminalService terminalService;

    public DistanceCloudAddressServiceImpl(TruckRouteService truckRouteService, StaticAddressRepository repository,
        RoundingService roundingService, GisService gisService, TerminalService terminalService) {

        this.truckRouteService = truckRouteService;
        this.repository = repository;
        this.roundingService = roundingService;
        this.gisService = gisService;
        this.terminalService = terminalService;
    }

    @Override
    public DistanceCloudAddress getAddressInCloud(BigInteger terminalUid, BigInteger staticAddressUid) {

        Terminal terminal = terminalService.getByUniqueId(terminalUid);
        StaticAddress staticAddress = repository.findByUniqueId(staticAddressUid);

        try {
            return createDistanceCloudAddress(terminal, staticAddress);
        } catch (OSRMNonRoutableRouteException e) {
            LOG.error("Cold not determine route, adding information to distance cloud address bean", e);

            DistanceCloudAddress distanceCloudAddress = new DistanceCloudAddress(staticAddress);
            distanceCloudAddress.setErrorMessage("Routing not possible");

            return distanceCloudAddress;
        }
    }


    public DistanceCloudAddress createDistanceCloudAddress(GeoLocation geoLocation, StaticAddress staticAddress) {

        LOG.debug("Creating distance-cloud-address item for {} and {}", geoLocation, staticAddress);

        TruckRoute terminalToAddressRoute = truckRouteService.route(geoLocation, staticAddress);
        DistanceCloudAddress cloudAddress = new DistanceCloudAddress(staticAddress);

        // distance from geoLocation -> address
        BigDecimal distanceRounded = terminalToAddressRoute.getDistance();
        distanceRounded = roundingService.roundDistance(distanceRounded);
        cloudAddress.setDistance(distanceRounded);

        // toll distance from geoLocation -> address
        BigDecimal tollDistanceRounded = terminalToAddressRoute.getTollDistance();
        tollDistanceRounded = roundingService.roundDistance(tollDistanceRounded);
        cloudAddress.setTollDistance(tollDistanceRounded.multiply(MULTIPLIER));

        // air-line distance
        BigDecimal airLineDistanceInMeters = gisService.calcAirLineDistInMeters(geoLocation, staticAddress);
        cloudAddress.setAirLineDistanceMeter(roundingService.roundDistance(airLineDistanceInMeters));

        return cloudAddress;
    }
}
