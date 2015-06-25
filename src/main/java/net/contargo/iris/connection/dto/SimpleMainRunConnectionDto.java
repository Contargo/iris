package net.contargo.iris.connection.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.route.RouteType;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class SimpleMainRunConnectionDto {

    private final String seaportUid;
    private final String terminalUid;
    private final RouteType routeType;

    public SimpleMainRunConnectionDto(String seaportUid, String terminalUid, RouteType routeType) {

        this.seaportUid = seaportUid;
        this.terminalUid = terminalUid;
        this.routeType = routeType;
    }

    @JsonProperty("seaportUid")
    public String getSeaportUid() {

        return seaportUid;
    }


    @JsonProperty("terminalUid")
    public String getTerminalUid() {

        return terminalUid;
    }


    @JsonProperty("routeType")
    public RouteType getRouteType() {

        return routeType;
    }
}
