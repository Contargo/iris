package net.contargo.iris.transport.inclinations.client.maps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class AnnotatedOsrmResponse {

    private final List<AnnotatedOsrmResponse.Route> routes;

    @JsonCreator
    public AnnotatedOsrmResponse(@JsonProperty("routes") List<Route> routes) {

        this.routes = routes;
    }

    List<Route> getRoutes() {

        return routes;
    }

    public static class Route {

        private final Route.Geometry geometry;
        private final List<Route.Leg> legs;

        @JsonCreator
        public Route(@JsonProperty("geometry") Geometry geometry,
            @JsonProperty("legs") List<Leg> legs) {

            this.geometry = geometry;
            this.legs = legs;
        }

        Geometry getGeometry() {

            return geometry;
        }


        List<Leg> getLegs() {

            return legs;
        }

        public static class Geometry {

            private final List<BigDecimal[]> coordinates;

            @JsonCreator
            public Geometry(@JsonProperty("coordinates") List<BigDecimal[]> coordinates) {

                this.coordinates = coordinates;
            }

            List<BigDecimal[]> getCoordinates() {

                return coordinates;
            }
        }

        public static class Leg {

            private final Leg.Annotation annotation;

            @JsonCreator
            public Leg(@JsonProperty("annotation") Annotation annotation) {

                this.annotation = annotation;
            }

            Annotation getAnnotation() {

                return annotation;
            }

            static class Annotation {

                private final List<Long> nodes;

                @JsonCreator
                public Annotation(@JsonProperty("nodes") List<Long> nodes) {

                    this.nodes = nodes;
                }

                List<Long> getNodes() {

                    return nodes;
                }
            }
        }
    }
}
