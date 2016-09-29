package net.contargo.iris.routing.osrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;


/**
 * Response of the osrm server. This response contains two important information. The first are the total kilometers and
 * time which is provided in the route_summary attribute. And the total toll kilometers which are provided in the
 * route_instructions.
 *
 * @author  Sven Mueller - mueller@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@JsonIgnoreProperties({ "hint_data", "route_name", "via_indices", "found_alternative", "via_points" })
class OSRM4Response {

    private static final BigDecimal METERS_PER_KILOMETER = new BigDecimal("1000.0");
    private static final int SCALE = 5;
    private static final int STANDARD_STREETDETAILS_LENGTH = 4;
    private static final int INDEX_3 = 3;
    private static final int INDEX_2 = 2;

    private int status;
    private OSRM4ResponseRouteSummary route_summary; // NOSONAR Field is legacy part of public API
    private String[][] route_instructions; // NOSONAR Field is legacy part of public API

    public void setRoute_instructions(String[][] routeInstructions) { // NOSONAR Field is legacy part of public API

        route_instructions = routeInstructions.clone();
    }


    public int getStatus() {

        return status;
    }


    public void setStatus(int status) {

        this.status = status;
    }


    public OSRM4ResponseRouteSummary getRoute_summary() { // NOSONAR Field is legacy part of public API

        return route_summary;
    }


    public void setRoute_summary(OSRM4ResponseRouteSummary routeSummary) { // NOSONAR Field is part of public API

        this.route_summary = routeSummary;
    }


    public BigDecimal getToll() {

        if (route_instructions == null) {
            return ZERO;
        } else {
            return exportToll(route_instructions);
        }
    }


    private BigDecimal exportToll(String[][] routeInstructions) {

        BigDecimal toll = ZERO;
        toll = toll.setScale(SCALE);

        for (String[] routeInstruction : routeInstructions) {
            /*
             * route_instructions:
             *
             * 0 - driving directions : integer numbers as defined in the source
             * file DataStructures/TurnInstructions.h.
             *
             * 1 - way name (string)
             *    [
             *      street_name ("A 65")
             *      street_type ("motorway")
             *      toll_route ("yes")
             *      country ("DE")
             *    ]
             *
             * 2 - length in meters (integer)
             *
             * 3 - position
             *
             * 4 - time
             *
             * 5 - length with unit (string)
             *
             * 6 - Direction abbreviation (string) N: north, S: south, E: east, W: west, NW: North West, ...
             *
             * 7 - azimuth (float)
             */
            BigDecimal distance = new BigDecimal(routeInstruction[2]);
            distance = distance.divide(METERS_PER_KILOMETER);

            String[] streetDetails = routeInstruction[1].split("/;");

            // currently, only german ("DE") toll roads are considered.
            if (streetDetails.length == STANDARD_STREETDETAILS_LENGTH
                    && ("yes".equalsIgnoreCase(streetDetails[INDEX_2])
                        && "de".equalsIgnoreCase(streetDetails[INDEX_3]))) {
                toll = toll.add(distance);
            }
        }

        return toll;
    }
}
