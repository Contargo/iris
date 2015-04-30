package net.contargo.iris.enricher.service;

import net.contargo.iris.route.RouteDirection;
import net.contargo.iris.route.RouteType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
// NOSONAR trailing comment...
final class EnricherContext { // NOSONAR class can not be instantiated because correct use of builder pattern

    private final RouteDirection routeDirection;
    private final List<RouteType> routeTypes;
    private final Map<String, String> errors;

    private EnricherContext(Builder builder) {

        this.routeDirection = builder.routeDirection;
        this.routeTypes = builder.routeTypes;
        this.errors = new HashMap<>();
    }

    public RouteDirection getRouteDirection() {

        return routeDirection;
    }


    public List<RouteType> getRouteTypes() {

        return routeTypes;
    }


    public Map<String, String> getErrors() {

        return errors;
    }


    public void addError(String type, String errorMessage) {

        errors.put(type, errorMessage);
    }

    public static class Builder {

        private RouteDirection routeDirection;
        private List<RouteType> routeTypes;

        public Builder routeDirection(RouteDirection value) {

            routeDirection = value;

            return this;
        }


        public Builder routeTypes(List<RouteType> value) {

            routeTypes = value;

            return this;
        }


        public EnricherContext build() {

            return new EnricherContext(this);
        }
    }
}
