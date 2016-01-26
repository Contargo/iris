package net.contargo.iris.route;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class represents a collection of attributes associated with a {@link Route}. Any of these attributes might be
 * set by one or more enricher object(s). Route data doesn't only consist of distance and duration related information,
 * but also encompasses a list of {@link RoutePart} objects.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @see  Route
 * @see  RoutePart
 * @see  net.contargo.iris.route.builder.RouteBuilder
 */
public class RouteData {

    private BigDecimal totalDistance;
    private BigDecimal totalTollDistance;
    private BigDecimal totalRealTollDistance;
    private BigDecimal totalOnewayTruckDistance;
    private BigDecimal totalDuration;
    private BigDecimal co2;
    private BigDecimal co2DirectTruck;
    private List<RoutePart> parts = new ArrayList<>();

    public BigDecimal getTotalDistance() {

        return totalDistance;
    }


    public void setTotalDistance(BigDecimal totalDistance) {

        this.totalDistance = totalDistance;
    }


    public BigDecimal getTotalTollDistance() {

        return totalTollDistance;
    }


    public void setTotalTollDistance(BigDecimal totalTollDistance) {

        this.totalTollDistance = totalTollDistance;
    }


    public BigDecimal getTotalRealTollDistance() {

        return totalRealTollDistance;
    }


    public void setTotalRealTollDistance(BigDecimal totalRealTollDistance) {

        this.totalRealTollDistance = totalRealTollDistance;
    }


    public BigDecimal getTotalOnewayTruckDistance() {

        return totalOnewayTruckDistance;
    }


    public void setTotalOnewayTruckDistance(BigDecimal totalOnewayTruckDistance) {

        this.totalOnewayTruckDistance = totalOnewayTruckDistance;
    }


    public BigDecimal getTotalDuration() {

        return totalDuration;
    }


    public void setTotalDuration(BigDecimal totalDuration) {

        this.totalDuration = totalDuration;
    }


    public BigDecimal getCo2() {

        return co2;
    }


    public void setCo2(BigDecimal co2) {

        this.co2 = co2;
    }


    public BigDecimal getCo2DirectTruck() {

        return co2DirectTruck;
    }


    public void setCo2DirectTruck(BigDecimal co2DirectTruck) {

        this.co2DirectTruck = co2DirectTruck;
    }


    public List<RoutePart> getParts() {

        return parts;
    }


    public void setParts(List<RoutePart> parts) {

        this.parts = parts;
    }


    @JsonIgnore
    public TruckRouteParts getOnewayTruckParts() {

        TruckRouteParts truckParts = new TruckRouteParts(getRoutePartsOfType(RouteType.TRUCK));

        truckParts.reduceToOneway();

        return truckParts;
    }


    public List<RoutePart> getRoutePartsOfType(final RouteType routeType) {

        return parts.stream().filter(part -> part.isOfType(routeType)).collect(Collectors.toList());
    }
}
