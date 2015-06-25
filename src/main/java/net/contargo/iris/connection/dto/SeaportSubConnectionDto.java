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
import java.math.BigInteger;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SeaportSubConnectionDto extends SubConnectionDto {

    private final BigInteger seaportUid;

    @JsonCreator
    public SeaportSubConnectionDto(@JsonProperty("id") Long id,
        @JsonProperty("terminalUid") BigInteger terminalUid,
        @JsonProperty("bargeDieselDistance") BigDecimal bargeDieselDistance,
        @JsonProperty("railDieselDistance") BigDecimal railDieselDistance,
        @JsonProperty("railElectricDistance") BigDecimal railElectricDistance,
        @JsonProperty("seaportUid") BigInteger seaportUid) {

        super(id, terminalUid, bargeDieselDistance, railDieselDistance, railElectricDistance);
        this.seaportUid = seaportUid;
    }


    public SeaportSubConnectionDto(SeaportSubConnection subConnection) {

        super(subConnection);
        this.seaportUid = subConnection.getSeaport().getUniqueId();
    }

    @Override
    public RouteType getRouteType() {

        return RouteType.BARGE;
    }


    public BigInteger getSeaportUid() {

        return seaportUid;
    }


    @Override
    public SubConnection toEntity(MainRunConnection parent) {

        SeaportSubConnection seaportSubConnection = new SeaportSubConnection();

        Seaport seaport = new Seaport();
        seaport.setUniqueId(this.seaportUid);
        seaportSubConnection.setSeaport(seaport);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(getTerminalUid());
        seaportSubConnection.setTerminal(terminal);

        seaportSubConnection.setBargeDieselDistance(getBargeDieselDistance());
        seaportSubConnection.setRailDieselDistance(getRailDieselDistance());
        seaportSubConnection.setRailElectricDistance(getRailElectricDistance());
        seaportSubConnection.setId(getId());

        seaportSubConnection.setParentConnection(parent);

        return seaportSubConnection;
    }
}
