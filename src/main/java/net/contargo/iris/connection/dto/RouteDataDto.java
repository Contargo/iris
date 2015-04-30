package net.contargo.iris.connection.dto;

import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RoutePart;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;


/**
 * Dto layer for {@link RouteData}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteDataDto {

    private BigDecimal co2;
    private BigDecimal co2DirectTruck;
    private BigDecimal totalDistance;
    private BigDecimal totalOnewayTruckDistance;
    private BigDecimal totalRealTollDistance;
    private BigDecimal totalTollDistance;
    private BigDecimal totalDuration;
    private List<RoutePartDto> parts;

    public RouteDataDto() {

        // needed for Spring MVC instantiation of Controller parameter
    }


    public RouteDataDto(RouteData data) {

        parts = new ArrayList<>();

        if (data != null) {
            this.co2 = data.getCo2();
            this.co2DirectTruck = data.getCo2DirectTruck();
            this.totalDistance = data.getTotalDistance();
            this.totalDuration = data.getTotalDuration();
            this.totalOnewayTruckDistance = data.getTotalOnewayTruckDistance();
            this.totalRealTollDistance = data.getTotalRealTollDistance();
            this.totalTollDistance = data.getTotalTollDistance();

            for (RoutePart part : data.getParts()) {
                parts.add(new RoutePartDto(part));
            }
        }
    }

    public RouteData toRouteData() {

        RouteData routeData = new RouteData();
        routeData.setCo2(co2);
        routeData.setCo2DirectTruck(co2DirectTruck);
        routeData.setTotalDistance(totalDistance);
        routeData.setTotalOnewayTruckDistance(totalOnewayTruckDistance);
        routeData.setTotalRealTollDistance(totalRealTollDistance);
        routeData.setTotalTollDistance(totalTollDistance);
        routeData.setTotalDuration(totalDuration);

        List<RoutePart> routeParts = new ArrayList<>();

        for (RoutePartDto part : parts) {
            routeParts.add(part.toRoutePart());
        }

        routeData.setParts(routeParts);

        return routeData;
    }


    public void setCo2(BigDecimal co2) {

        this.co2 = co2;
    }


    public void setCo2DirectTruck(BigDecimal co2DirectTruck) {

        this.co2DirectTruck = co2DirectTruck;
    }


    public void setTotalDistance(BigDecimal totalDistance) {

        this.totalDistance = totalDistance;
    }


    public void setTotalOnewayTruckDistance(BigDecimal totalOnewayTruckDistance) {

        this.totalOnewayTruckDistance = totalOnewayTruckDistance;
    }


    public void setTotalRealTollDistance(BigDecimal totalRealTollDistance) {

        this.totalRealTollDistance = totalRealTollDistance;
    }


    public void setTotalTollDistance(BigDecimal totalTollDistance) {

        this.totalTollDistance = totalTollDistance;
    }


    public void setTotalDuration(BigDecimal totalDuration) {

        this.totalDuration = totalDuration;
    }


    public void setParts(List<RoutePartDto> parts) {

        this.parts = parts;
    }


    public BigDecimal getCo2() {

        return co2;
    }


    public BigDecimal getCo2DirectTruck() {

        return co2DirectTruck;
    }


    public List<RoutePartDto> getParts() {

        return parts;
    }


    public BigDecimal getTotalDistance() {

        return totalDistance;
    }


    public BigDecimal getTotalOnewayTruckDistance() {

        return totalOnewayTruckDistance;
    }


    public BigDecimal getTotalRealTollDistance() {

        return totalRealTollDistance;
    }


    public BigDecimal getTotalTollDistance() {

        return totalTollDistance;
    }


    public BigDecimal getTotalDuration() {

        return totalDuration;
    }
}
