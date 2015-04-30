package net.contargo.iris.osrm.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * Response of the osrm server. This response contains two important information. The first are the total kilometers and
 * time which is provided in the route_summary attribute. And the total toll kilometers which are provided in the
 * route_instructions.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@JsonIgnoreProperties({ "hint_data", "route_name", "via_indices", "found_alternative", "via_points" })
class OSRMJsonResponse {

    private int status;
    private String status_message; // NOSONAR Field is legacy part of public API
    private OSRMJsonResponseRouteSummary route_summary; // NOSONAR Field is legacy part of public API
    private String[][] route_instructions; // NOSONAR Field is legacy part of public API

    public String[][] getRoute_instructions() { // NOSONAR Field is part of public API, therefore unchangable legacy

        String[][] routeInstructions = route_instructions;

        return routeInstructions;
    }


    public void setRoute_instructions(String[][] routeInstructions) { // NOSONAR Field is legacy part of public API

        route_instructions = routeInstructions.clone();
    }


    public int getStatus() {

        return status;
    }


    public void setStatus(int status) {

        this.status = status;
    }


    public String getStatus_message() { // NOSONAR Field is legacy part of public API

        return status_message;
    }


    public void setStatus_message(String statusMessage) { // NOSONAR Field is legacy part of public API

        this.status_message = statusMessage;
    }


    public OSRMJsonResponseRouteSummary getRoute_summary() { // NOSONAR Field is legacy part of public API

        return route_summary;
    }


    public void setRoute_summary(OSRMJsonResponseRouteSummary routeSummary) { // NOSONAR Field is part of public API

        this.route_summary = routeSummary;
    }
}
