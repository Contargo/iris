package net.contargo.iris.monitoring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@Aspect
@Component
public class PerformanceInterceptor {

    private final PerformanceStats performanceStats;

    @Autowired
    public PerformanceInterceptor(PerformanceStats performanceStats) {

        this.performanceStats = performanceStats;
    }

    @Around(
        "routeEnricherApiControllerGetEnrichedRoute() || "
        + "addressApiControllerAddressesByAddressDetails() || "
        + "truckRouteServiceRoute()"
    )
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            performanceStats.updateStats(joinPoint.getSourceLocation().getWithinType().getSimpleName() + "@"
                + joinPoint.getSignature().getName(), System.currentTimeMillis() - start);
        }
    }


    @Pointcut("execution(* net.contargo.iris.route.api.RouteEnricherApiController.getEnrichedRoute(..))")
    public void routeEnricherApiControllerGetEnrichedRoute() {

        // intercepts routeEnricherApiControllerGetEnrichedRoute
    }


    @Pointcut("execution(* net.contargo.iris.address.api.AddressApiController.addressesByAddressDetails(..))")
    public void addressApiControllerAddressesByAddressDetails() {

        // intercepts addressApiControllerAddressesByAddressDetails
    }


    @Pointcut("execution(* net.contargo.iris.truck.service.TruckRouteService.route(..))")
    public void truckRouteServiceRoute() {

        // intercepts truckRouteServiceRoute
    }
}
