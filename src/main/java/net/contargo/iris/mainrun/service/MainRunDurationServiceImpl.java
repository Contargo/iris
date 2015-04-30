package net.contargo.iris.mainrun.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Computes duration of main runs and rounds results.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class MainRunDurationServiceImpl implements MainRunDurationService {

    // average speeds in km/h
    private static final BigDecimal AVERAGE_SPEED_BARGE_UPSTREAM = new BigDecimal("10.0");
    private static final BigDecimal AVERAGE_SPEED_BARGE_DOWNSTREAM = new BigDecimal("18.0");
    private static final BigDecimal AVERAGE_SPEED_RAIL = new BigDecimal("45.0");
    private static final BigDecimal SECONDS_IN_AN_HOUR = new BigDecimal("60.0");
    private static final int SCALE = 5;

    private final RoundingService roundingService;

    public MainRunDurationServiceImpl(RoundingService roundingService) {

        this.roundingService = roundingService;
    }

    /**
     * @see  MainRunDurationService#getMainRunRoutePartDuration(net.contargo.iris.connection.MainRunConnection,
     *       net.contargo.iris.route.RoutePart)
     */
    @Override
    public BigDecimal getMainRunRoutePartDuration(MainRunConnection mainrunConnection, RoutePart routePart) {

        RoutePart.Direction direction = routePart.getDirection();
        BigDecimal distance = mainrunConnection.getTotalDistance();
        BigDecimal divisor;

        if (routePart.isOfType(RouteType.BARGE)) {
            // electric distance is 0.0 if route part is of type barge, so you don't need to consider it in the
            // calculation
            if (direction == RoutePart.Direction.DOWNSTREAM) {
                divisor = AVERAGE_SPEED_BARGE_DOWNSTREAM;
            } else {
                divisor = AVERAGE_SPEED_BARGE_UPSTREAM;
            }
        } else if (routePart.isOfType(RouteType.RAIL)) {
            divisor = AVERAGE_SPEED_RAIL;
        } else {
            // not a main run route part, return 0.0
            return BigDecimal.ZERO;
        }

        BigDecimal duration = distance.divide(divisor, SCALE, RoundingMode.HALF_UP).multiply(SECONDS_IN_AN_HOUR);

        return roundingService.roundDuration(duration);
    }
}
