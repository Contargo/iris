package net.contargo.iris.route.service;

import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.route.RoutePartData;

import org.slf4j.Logger;

import org.springframework.util.Assert;

import java.lang.invoke.MethodHandles;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Implementation of interface {@link EnricherService}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class EnricherServiceImpl implements EnricherService {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final List<RoutePartEnricher> routePartEnricherList;
    private final List<RouteTotalEnricher> routeTotalEnricherList;

    public EnricherServiceImpl(List<RoutePartEnricher> routePartEnricherList,
        List<RouteTotalEnricher> routeTotalEnricherList) {

        Assert.notNull(routePartEnricherList);
        Assert.notNull(routeTotalEnricherList);

        this.routePartEnricherList = routePartEnricherList;
        this.routeTotalEnricherList = routeTotalEnricherList;
    }

    @Override
    public Route enrich(Route route) {

        EnricherContext enricherContext = new EnricherContext.Builder().routeDirection(route.getDirection()).build();

        try {
            for (RoutePart routePart : route.getData().getParts()) {
                enrich(routePart, enricherContext, routePartEnricherList);
            }

            enrich(route, enricherContext, routeTotalEnricherList);
        } catch (CriticalEnricherException e) {
            handleError(route, enricherContext);

            return route;
        } finally {
            route.setErrors(enricherContext.getErrors());
        }

        return route;
    }


    private void enrich(RoutePart routePart, EnricherContext context, List<RoutePartEnricher> routePartEnrichers)
        throws CriticalEnricherException {

        for (RoutePartEnricher routePartEnricher : routePartEnrichers) {
            LOG.debug("Enriching route using RoutePartEnricher {}", routePartEnricher.getClass().getSimpleName());

            routePartEnricher.enrich(routePart, context);
        }
    }


    private void enrich(Route route, EnricherContext context, List<RouteTotalEnricher> routeTotalEnrichers)
        throws CriticalEnricherException {

        for (RouteTotalEnricher routeTotalEnricher : routeTotalEnrichers) {
            LOG.debug("Enriching route using RouteTotalEnricher {}", routeTotalEnricher.getClass().getSimpleName());

            routeTotalEnricher.enrich(route, context);
        }
    }


    private void handleError(Route route, EnricherContext context) {

        List<RoutePart> parts = route.getData().getParts();

        for (RoutePart part : parts) {
            part.setData(new RoutePartData());
        }

        RouteData data = new RouteData();
        data.setParts(parts);

        route.setData(data);

        context.addError("route", "Routing failed");
    }
}
