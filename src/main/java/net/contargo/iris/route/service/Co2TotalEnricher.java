package net.contargo.iris.route.service;

import net.contargo.iris.co2.service.Co2Service;
import net.contargo.iris.route.Route;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;


/**
 * Enricher to set emission information on {@link Route}s.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
class Co2TotalEnricher implements RouteTotalEnricher {

    private final Co2Service co2Service;

    @Autowired
    Co2TotalEnricher(Co2Service co2Service) {

        this.co2Service = co2Service;
    }

    @Override
    public void enrich(Route route, EnricherContext context) throws CriticalEnricherException {

        BigDecimal co2;
        BigDecimal co2DirectTruck;

        try {
            co2 = co2Service.getEmission(route);
            co2DirectTruck = co2Service.getEmissionDirectTruck(route);
        } catch (IllegalStateException e) {
            throw new CriticalEnricherException("Failed calculating co2 emission", e);
        }

        route.getData().setCo2(co2);
        route.getData().setCo2DirectTruck(co2DirectTruck);
    }
}
