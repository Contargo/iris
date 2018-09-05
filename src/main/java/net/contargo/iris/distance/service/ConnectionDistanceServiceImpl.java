package net.contargo.iris.distance.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.rounding.RoundingService;

import java.math.BigDecimal;


/**
 * Default implementation of {@link ConnectionDistanceService}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
public class ConnectionDistanceServiceImpl implements ConnectionDistanceService {

    private final RoundingService roundingService;

    public ConnectionDistanceServiceImpl(RoundingService roundingService) {

        this.roundingService = roundingService;
    }

    /**
     * @see  ConnectionDistanceService#getDistance(net.contargo.iris.connection.MainRunConnection)
     */
    @Override
    public BigDecimal getDistance(MainRunConnection mainrunConnection) {

        return roundingService.roundDistance(mainrunConnection.getTotalDistance());
    }


    /**
     * @see  ConnectionDistanceService#getDieselDistance(net.contargo.iris.connection.MainRunConnection)
     */
    @Override
    public BigDecimal getDieselDistance(MainRunConnection mainrunConnection) {

        return roundingService.roundDistance(mainrunConnection.getRailDieselDistance()
                .add(mainrunConnection.getBargeDieselDistance()));
    }


    /**
     * @see  ConnectionDistanceService#getElectricDistance(net.contargo.iris.connection.MainRunConnection)
     */
    @Override
    public BigDecimal getElectricDistance(MainRunConnection mainrunConnection) {

        return roundingService.roundDistance(mainrunConnection.getRailElectricDistance());
    }


    @Override
    public BigDecimal getRailDieselDistance(MainRunConnection mainrunConnection) {

        return roundingService.roundDistance(mainrunConnection.getRailDieselDistance());
    }


    @Override
    public BigDecimal getBargeDieselDistance(MainRunConnection mainrunConnection) {

        return roundingService.roundDistance(mainrunConnection.getBargeDieselDistance());
    }


    @Override
    public BigDecimal getDtruckDistance(MainRunConnection mainrunConnection) {

        return roundingService.roundDistance(mainrunConnection.getRoadDistance());
    }
}
