package net.contargo.iris.distance.service;

import net.contargo.iris.connection.MainRunConnection;

import java.math.BigDecimal;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface ConnectionDistanceService {

    /**
     * Extracts the distance of a {@link net.contargo.iris.connection.MainRunConnection}.
     *
     * @param  mainrunConnection
     *
     * @return  BigDecimal
     */
    BigDecimal getDistance(MainRunConnection mainrunConnection);


    /**
     * Extracts the dieselDistance of a {@link net.contargo.iris.connection.MainRunConnection}.
     *
     * @param  mainrunConnection
     *
     * @return  BigDecimal
     */
    BigDecimal getDieselDistance(MainRunConnection mainrunConnection);


    /**
     * Extracts the electricDistance of a {@link net.contargo.iris.connection.MainRunConnection}.
     *
     * @param  mainrunConnection
     *
     * @return  BigDecimal
     */
    BigDecimal getElectricDistance(MainRunConnection mainrunConnection);
}
