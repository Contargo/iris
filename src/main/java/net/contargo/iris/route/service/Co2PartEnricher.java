package net.contargo.iris.route.service;

import net.contargo.iris.co2.advice.Co2PartStrategy;
import net.contargo.iris.co2.advice.Co2PartStrategyAdvisor;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Vincent Potucek - potucek@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
class Co2PartEnricher implements RoutePartEnricher {

    private final Co2PartStrategyAdvisor co2PartStrategyAdvisor;

    @Autowired
    Co2PartEnricher(Co2PartStrategyAdvisor co2PartStrategyAdvisor) {

        this.co2PartStrategyAdvisor = co2PartStrategyAdvisor;
    }

    @Override
    public void enrich(RoutePart routePart, EnricherContext context) throws CriticalEnricherException {

        RouteType routeType = routePart.getRouteType();

        Co2PartStrategy strategy;

        try {
            strategy = co2PartStrategyAdvisor.advice(routeType);
        } catch (IllegalStateException e) {
            throw new CriticalEnricherException("Co2 part enrichment failed", e);
        }

        BigDecimal emission = strategy.getEmissionForRoutePart(routePart);

        routePart.getData().setCo2(emission);
    }
}
