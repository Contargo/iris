package net.contargo.iris.connection.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SeaportSubConnection;
import net.contargo.iris.connection.SubConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SeaportSubConnectionDto extends SubConnectionDto {

    private final Long seaportId;

    @JsonCreator
    public SeaportSubConnectionDto(@JsonProperty("id") Long id,
        @JsonProperty("terminalId") Long terminalId,
        @JsonProperty("bargeDieselDistance") BigDecimal bargeDieselDistance,
        @JsonProperty("railDieselDistance") BigDecimal railDieselDistance,
        @JsonProperty("railElectricDistance") BigDecimal railElectricDistance,
        @JsonProperty("seaportId") Long seaportId) {

        super(id, terminalId, bargeDieselDistance, railDieselDistance, railElectricDistance);
        this.seaportId = seaportId;
    }


    public SeaportSubConnectionDto(SeaportSubConnection subConnection) {

        super(subConnection);
        this.seaportId = subConnection.getSeaport().getId();
    }

    @Override
    public RouteType getRouteType() {

        return RouteType.BARGE;
    }


    public Long getSeaportId() {

        return seaportId;
    }


    @Override
    public SubConnection toEntity(MainRunConnection parent) {

        SeaportSubConnection seaportSubConnection = new SeaportSubConnection();

        Seaport seaport = new Seaport();
        seaport.setId(this.seaportId);
        seaportSubConnection.setSeaport(seaport);

        Terminal terminal = new Terminal();
        terminal.setId(getTerminalId());
        seaportSubConnection.setTerminal(terminal);

        seaportSubConnection.setBargeDieselDistance(getBargeDieselDistance());
        seaportSubConnection.setRailDieselDistance(getRailDieselDistance());
        seaportSubConnection.setRailElectricDistance(getRailElectricDistance());
        seaportSubConnection.setId(getId());

        seaportSubConnection.setParentConnection(parent);

        return seaportSubConnection;
    }
}
