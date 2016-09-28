package net.contargo.iris.routing.osrm;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Reponse Object from the OSRM Route Summary.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
class OSRM4ResponseRouteSummary {

    private static final String TOTAL_DISTANCE = "total_distance";
    private static final String TOTAL_TIME = "total_time";
    private static final String START_POINT = "start_point";
    private static final String END_POINT = "end_point";

    @JsonProperty(TOTAL_DISTANCE)
    private double totalDistance;
    @JsonProperty(TOTAL_TIME)
    private double totalTime;
    @JsonProperty(START_POINT)
    private String startPoint;
    @JsonProperty(END_POINT)
    private String endPoint;

    public double getTotal_distance() { // NOSONAR Field is legacy part of public API

        return totalDistance;
    }


    public void setTotalDistance(double totalDistance) {

        this.totalDistance = totalDistance;
    }


    public double getTotalTime() {

        return totalTime;
    }


    public void setTotalTime(double totalTime) {

        this.totalTime = totalTime;
    }


    public String getStart_point() { // NOSONAR Field is legacy part of public API

        return startPoint;
    }


    public void setStartPoint(String startPoint) {

        this.startPoint = startPoint;
    }


    public String getEnd_point() { // NOSONAR Field is legacy part of public API

        return endPoint;
    }


    public void setEndPoint(String endPoint) {

        this.endPoint = endPoint;
    }
}
