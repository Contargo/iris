package net.contargo.iris.co2.service;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.co2.Co2Calculator;
import net.contargo.iris.co2.advice.Co2CalculationHandlingParams;
import net.contargo.iris.co2.advice.Co2PartStrategy;
import net.contargo.iris.co2.advice.Co2PartStrategyAdvisor;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.route.builder.DirectTruckRouteBuilder;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

import java.math.BigDecimal;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
class Co2ServiceImpl implements Co2Service {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final DirectTruckRouteBuilder directTruckRouteBuilder;
    private final Co2PartStrategyAdvisor co2PartStrategyAdvisor;

    Co2ServiceImpl(DirectTruckRouteBuilder directTruckRouteBuilder, Co2PartStrategyAdvisor co2PartStrategyAdvisor) {

        this.directTruckRouteBuilder = directTruckRouteBuilder;
        this.co2PartStrategyAdvisor = co2PartStrategyAdvisor;
    }

    @Override
    public BigDecimal getEmission(Route route) {

        BigDecimal co2 = BigDecimal.ZERO;

        List<RoutePart> parts = route.getData().getParts();

        for (RoutePart part : parts) {
            Co2PartStrategy strategy = co2PartStrategyAdvisor.advice(part.getRouteType());
            co2 = co2.add(strategy.getEmissionForRoutePart(part));

            Co2CalculationParams.Handling params = new Co2CalculationHandlingParams(part);
            co2 = co2.add(Co2Calculator.handling(params));
        }

        LOG.debug("Setting CO2 for route {}: {} kg", route.getName(), co2);

        return co2;
    }


    @Override
    public BigDecimal getEmissionDirectTruck(Route route) {

        Route truckRoute = directTruckRouteBuilder.getCorrespondingDirectTruckRoute(route);

        BigDecimal co2 = BigDecimal.ZERO;
        List<RoutePart> parts = truckRoute.getData().getParts();

        setCorrespondingDirectTruckDistance(parts);

        for (RoutePart part : parts) {
            Co2PartStrategy strategy = co2PartStrategyAdvisor.advice(RouteType.DTRUCK);
            co2 = co2.add(strategy.getEmissionForRoutePart(part));

            Co2CalculationParams.Handling params = new Co2CalculationHandlingParams(part);
            co2 = co2.add(Co2Calculator.handling(params));
        }

        LOG.debug("Setting CO2 Direct Truck for route {}: {} kg", route.getName(), co2);

        return co2;
    }


    private void setCorrespondingDirectTruckDistance(List<RoutePart> parts) {

        parts.forEach(p -> p.getData().setDtruckDistance(p.getData().getDistance()));
    }
}
