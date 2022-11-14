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

    /**
     * @see  ConnectionDistanceService#getDistance(net.contargo.iris.connection.MainRunConnection)
     */
    @Override
    public BigDecimal getDistance(MainRunConnection mainrunConnection) {

        return RoundingService.roundDistance(mainrunConnection.getTotalDistance());
    }


    /**
     * @see  ConnectionDistanceService#getDieselDistance(net.contargo.iris.connection.MainRunConnection)
     */
    @Override
    public BigDecimal getDieselDistance(MainRunConnection mainrunConnection) {

        return RoundingService.roundDistance(mainrunConnection.getRailDieselDistance()
                .add(mainrunConnection.getBargeDieselDistance()));
    }


    /**
     * @see  ConnectionDistanceService#getElectricDistance(net.contargo.iris.connection.MainRunConnection)
     */
    @Override
    public BigDecimal getElectricDistance(MainRunConnection mainrunConnection) {

        return RoundingService.roundDistance(mainrunConnection.getRailElectricDistance());
    }


    @Override
    public BigDecimal getRailDieselDistance(MainRunConnection mainrunConnection) {

        return RoundingService.roundDistance(mainrunConnection.getRailDieselDistance());
    }


    @Override
    public BigDecimal getBargeDieselDistance(MainRunConnection mainrunConnection) {

        return RoundingService.roundDistance(mainrunConnection.getBargeDieselDistance());
    }


    @Override
    public BigDecimal getDtruckDistance(MainRunConnection mainrunConnection) {

        return RoundingService.roundDistance(mainrunConnection.getRoadDistance());
    }
}
