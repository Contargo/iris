package net.contargo.iris.route.dto;

import net.contargo.iris.address.dto.GeoLocationDto;

import java.math.BigDecimal;

import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class RoutingResultDto {

    private final BigDecimal co2;
    private final BigDecimal co2DirectTruck;
    private final BigDecimal distance;
    private final BigDecimal bargeDistance;
    private final BigDecimal onewayTruckDistance;
    private final BigDecimal realTollDistance;
    private final BigDecimal tollDistance;
    private final BigDecimal duration;
    private final List<GeoLocationDto> stops;

    private RoutingResultDto(Builder builder) {

        this.co2 = builder.co2;
        this.co2DirectTruck = builder.co2DirectTruck;
        this.distance = builder.distance;
        this.bargeDistance = builder.bargeDistance;
        this.onewayTruckDistance = builder.onewayTruckDistance;
        this.realTollDistance = builder.realTollDistance;
        this.tollDistance = builder.tollDistance;
        this.duration = builder.duration;
        this.stops = builder.stops;
    }

    public BigDecimal getCo2() {

        return co2;
    }


    public BigDecimal getCo2DirectTruck() {

        return co2DirectTruck;
    }


    public BigDecimal getDistance() {

        return distance;
    }


    public BigDecimal getBargeDistance() {

        return bargeDistance;
    }


    public BigDecimal getOnewayTruckDistance() {

        return onewayTruckDistance;
    }


    public BigDecimal getRealTollDistance() {

        return realTollDistance;
    }


    public BigDecimal getTollDistance() {

        return tollDistance;
    }


    public BigDecimal getDuration() {

        return duration;
    }


    public List<GeoLocationDto> getStops() {

        return stops;
    }

    public static class Builder {

        private BigDecimal co2;
        private BigDecimal co2DirectTruck;
        private BigDecimal distance;
        private BigDecimal bargeDistance;
        private BigDecimal onewayTruckDistance;
        private BigDecimal realTollDistance;
        private BigDecimal tollDistance;
        private BigDecimal duration;
        private List<GeoLocationDto> stops;

        public RoutingResultDto build() {

            return new RoutingResultDto(this);
        }


        public Builder withCo2(BigDecimal co2) {

            this.co2 = co2;

            return this;
        }


        public Builder withCo2DirectTruck(BigDecimal co2DirectTruck) {

            this.co2DirectTruck = co2DirectTruck;

            return this;
        }


        public Builder withDistance(BigDecimal distance) {

            this.distance = distance;

            return this;
        }


        public Builder withBargeDistance(BigDecimal bargeDistance) {

            this.bargeDistance = bargeDistance;

            return this;
        }


        public Builder withOnewayTruckDistance(BigDecimal onewayTruckDistance) {

            this.onewayTruckDistance = onewayTruckDistance;

            return this;
        }


        public Builder withRealTollDistance(BigDecimal realTollDistance) {

            this.realTollDistance = realTollDistance;

            return this;
        }


        public Builder withTollDistance(BigDecimal tollDistance) {

            this.tollDistance = tollDistance;

            return this;
        }


        public Builder withDuration(BigDecimal duration) {

            this.duration = duration;

            return this;
        }


        public Builder withStops(List<GeoLocationDto> stops) {

            this.stops = stops;

            return this;
        }
    }
}
