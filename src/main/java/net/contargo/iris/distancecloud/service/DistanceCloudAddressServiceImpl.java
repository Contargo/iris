package net.contargo.iris.distancecloud.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressNotFoundException;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;
import net.contargo.iris.distancecloud.DistanceCloudAddress;
import net.contargo.iris.gis.service.GisService;
import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;
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
    private static final BigDecimal BY_TWO = new BigDecimal(2);

    private final RoundingService roundingService;
    private final TruckRouteService truckRouteService;
    private final StaticAddressService staticAddressService;
    private final GisService gisService;
    private final TerminalService terminalService;
    private final RouteDataRevisionService routeRevisionService;

    public DistanceCloudAddressServiceImpl(TruckRouteService truckRouteService,
        StaticAddressService staticAddressService, RoundingService roundingService, GisService gisService,
        TerminalService terminalService, RouteDataRevisionService routeRevisionService) {

        this.truckRouteService = truckRouteService;
        this.staticAddressService = staticAddressService;
        this.roundingService = roundingService;
        this.gisService = gisService;
        this.terminalService = terminalService;
        this.routeRevisionService = routeRevisionService;
    }

    @Override
    public DistanceCloudAddress getAddressInCloud(BigInteger terminalUid, BigInteger staticAddressUid) {

        Terminal terminal = terminalService.getByUniqueId(terminalUid);
        StaticAddress staticAddress;

        try {
            staticAddress = staticAddressService.findByUId(staticAddressUid);
        } catch (StaticAddressNotFoundException e) {
            LOG.debug("DistanceCloud: StaticAddress with id {} not found", staticAddressUid);

            StaticAddress exceptionStaticAddress = new StaticAddress();
            exceptionStaticAddress.setUniqueId(staticAddressUid);

            return createError(exceptionStaticAddress, "StaticAddress with given id not found");
        }

        try {
            LOG.debug("DistanceCloud: Creating distance-cloud-address item for {} and {}", terminal, staticAddress);

            DistanceCloudAddress distanceCloudAddress;
            RouteDataRevision routeDataRevision = routeRevisionService.getRouteDataRevision(terminal, staticAddress);

            if (routeDataRevision == null) {
                distanceCloudAddress = createDistanceCloudAddress(terminal, staticAddress);
            } else {
                distanceCloudAddress = createDistanceCloudAddressWithRouteRevision(routeDataRevision, staticAddress);
            }

            return distanceCloudAddress;
        } catch (OSRMNonRoutableRouteException e) {
            LOG.debug("DistanceCloud: Could not determine route, adding information to distance cloud address bean", e);

            return createError(staticAddress, "Routing not possible");
        }
    }


    private DistanceCloudAddress createDistanceCloudAddress(GeoLocation geoLocation, StaticAddress staticAddress) {

        LOG.debug("DistanceCloud: Use maps routing as base for the distance values ");

        TruckRoute terminalToAddressRoute = truckRouteService.route(geoLocation, staticAddress);
        DistanceCloudAddress cloudAddress = new DistanceCloudAddress(staticAddress);

        // distance from geoLocation -> address
        BigDecimal distanceRounded = terminalToAddressRoute.getDistance();
        distanceRounded = roundingService.roundDistance(distanceRounded);
        cloudAddress.setDistance(distanceRounded);

        // toll distance from geoLocation -> address
        BigDecimal tollDistanceRounded = terminalToAddressRoute.getTollDistance();
        tollDistanceRounded = roundingService.roundDistance(tollDistanceRounded);
        cloudAddress.setTollDistance(tollDistanceRounded.multiply(BY_TWO));

        // air-line distance
        BigDecimal airLineDistanceInMeters = gisService.calcAirLineDistInMeters(geoLocation, staticAddress);
        cloudAddress.setAirLineDistanceMeter(roundingService.roundDistance(airLineDistanceInMeters));

        return cloudAddress;
    }


    private DistanceCloudAddress createDistanceCloudAddressWithRouteRevision(RouteDataRevision routeDataRevision,
        StaticAddress staticAddress) {

        LOG.debug("DistanceCloud: Use route revision as base for the distance values");

        DistanceCloudAddress cloudAddress = new DistanceCloudAddress(staticAddress);
        cloudAddress.setDistance(routeDataRevision.getTruckDistanceOneWayInKilometer());
        cloudAddress.setTollDistance(routeDataRevision.getTollDistanceOneWayInKilometer().multiply(BY_TWO));
        cloudAddress.setAirLineDistanceMeter(routeDataRevision.getAirlineDistanceInKilometer());

        return cloudAddress;
    }


    private DistanceCloudAddress createError(StaticAddress staticAddress, String errorMessage) {

        DistanceCloudAddress distanceCloudAddress = new DistanceCloudAddress(staticAddress);
        distanceCloudAddress.setErrorMessage(errorMessage);

        return distanceCloudAddress;
    }
}
