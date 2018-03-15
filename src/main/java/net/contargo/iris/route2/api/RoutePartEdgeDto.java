package net.contargo.iris.route2.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.route2.ModeOfTransport;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class RoutePartEdgeDto {

    private final RoutePartNodeDto start;
    private final RoutePartNodeDto end;
    private final ModeOfTransport modeOfTransport;

    @JsonCreator
    public RoutePartEdgeDto(@JsonProperty("start") RoutePartNodeDto start,
        @JsonProperty("end") RoutePartNodeDto end,
        @JsonProperty("modeOfTransport") ModeOfTransport modeOfTransport) {

        this.start = start;
        this.end = end;
        this.modeOfTransport = modeOfTransport;
    }

    public RoutePartNodeDto getStart() {

        return start;
    }


    public RoutePartNodeDto getEnd() {

        return end;
    }


    public ModeOfTransport getModeOfTransport() {

        return modeOfTransport;
    }


    @Override
    public String toString() {

        return "RoutePartEdgeDto{"
            + "start=" + start
            + ", end=" + end
            + ", modeOfTransport=" + modeOfTransport + '}';
    }
}
