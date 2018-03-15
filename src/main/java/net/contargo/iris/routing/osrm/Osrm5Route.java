package net.contargo.iris.routing.osrm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class Osrm5Route {

    private static final BigDecimal METERS_PER_KILOMETER = new BigDecimal("1000.0");

    private final BigDecimal distance;
    private final BigDecimal duration;
    private final List<Osrm5Leg> legs;

    @JsonCreator
    public Osrm5Route(@JsonProperty("distance") BigDecimal distance,
        @JsonProperty("duration") BigDecimal duration,
        @JsonProperty("legs") List<Osrm5Leg> legs) {

        this.distance = distance;
        this.duration = duration;
        this.legs = legs;
    }

    public BigDecimal getDistance() {

        return distance;
    }


    public BigDecimal getDuration() {

        return duration;
    }


    BigDecimal getToll() {

        BigDecimal tollDistance = legs.stream().map(Osrm5Leg::getToll).reduce(BigDecimal.ZERO, BigDecimal::add);

        return tollDistance.divide(METERS_PER_KILOMETER);
    }


    List<String> getGeometries() {

        return this.legs.stream().map(Osrm5Leg::getGeometries).flatMap(List::stream).collect(toList());
    }

    private static final class Osrm5Leg {

        private final List<Osrm5Step> steps;

        @JsonCreator
        public Osrm5Leg(@JsonProperty("steps") List<Osrm5Step> steps) {

            this.steps = steps;
        }

        BigDecimal getToll() {

            return steps.stream().map(Osrm5Step::getToll).reduce(BigDecimal.ZERO, BigDecimal::add);
        }


        List<String> getGeometries() {

            return this.steps.stream().map(a -> a.geometry).collect(toList());
        }
    }

    private static final class Osrm5Step {

        private static final int COMPONENT_LENGTH = 4;
        private static final int TOLL_INDEX = 2;
        private static final int COUNTRY_INDEX = 3;
        private final String name;
        private final BigDecimal distance;
        private final String geometry;

        @JsonCreator
        public Osrm5Step(@JsonProperty("name") String name,
            @JsonProperty("distance") BigDecimal distance,
            @JsonProperty("geometry") String geometry) {

            this.name = name;
            this.distance = distance;
            this.geometry = geometry;
        }

        BigDecimal getToll() {

            String[] components = name.split("/;");

            if (components.length == COMPONENT_LENGTH
                    && ("yes".equalsIgnoreCase(components[TOLL_INDEX])
                        && "de".equalsIgnoreCase(components[COUNTRY_INDEX]))) {
                return distance;
            }

            return BigDecimal.ZERO;
        }
    }
}
