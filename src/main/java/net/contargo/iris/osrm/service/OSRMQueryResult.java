package net.contargo.iris.osrm.service;

import java.util.Arrays;


/**
 * Represents the result of a OSRM Query to the application. So we dont have to use the OSRM Response returned by the
 * OSRM service
 *
 * @author  Arnold Franke - franke@synyx.de
 */
public final class OSRMQueryResult {

    private final int status;
    private final double totalDistance;
    private final double totalTime;
    private final String[][] routeInstructions;

    public OSRMQueryResult(int status, double totalDistance, double totalTime, String[][] instructions) {

        this.status = status;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;

        if (instructions == null) {
            this.routeInstructions = new String[0][0];
        } else {
            this.routeInstructions = instructions.clone();
        }
    }

    public int getStatus() {

        return status;
    }


    public double getTotalDistance() {

        return totalDistance;
    }


    public double getTotalTime() {

        return totalTime;
    }


    public String[][] getRouteInstructions() {

        return Arrays.copyOf(routeInstructions, routeInstructions.length);
    }
}
