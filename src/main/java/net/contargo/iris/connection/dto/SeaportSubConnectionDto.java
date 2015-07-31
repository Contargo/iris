package net.contargo.iris.connection.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SeaportSubConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
public final class SeaportSubConnectionDto extends AbstractSubConnectionDto {

    private final String seaportUid;

    @JsonCreator
    public SeaportSubConnectionDto(@JsonProperty("id") Long id,
        @JsonProperty("terminalUid") String terminalUid,
        @JsonProperty("bargeDieselDistance") BigDecimal bargeDieselDistance,
        @JsonProperty("railDieselDistance") BigDecimal railDieselDistance,
        @JsonProperty("railElectricDistance") BigDecimal railElectricDistance,
        @JsonProperty("seaportUid") String seaportUid) {

        super(id, terminalUid, bargeDieselDistance, railDieselDistance, railElectricDistance);
        this.seaportUid = seaportUid;
    }


    public SeaportSubConnectionDto(SeaportSubConnection subConnection) {

        super(subConnection);
        this.seaportUid = subConnection.getSeaport().getUniqueId().toString();
    }

    @Override
    public RouteType getRouteType() {

        return RouteType.BARGE;
    }


    public String getSeaportUid() {

        return seaportUid;
    }


    @Override
    public AbstractSubConnection toEntity(MainRunConnection parent) {

        SeaportSubConnection seaportSubConnection = new SeaportSubConnection();

        Seaport seaport = new Seaport();
        seaport.setUniqueId(new BigInteger(this.seaportUid));
        seaportSubConnection.setSeaport(seaport);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger(getTerminalUid()));
        seaportSubConnection.setTerminal(terminal);

        seaportSubConnection.setBargeDieselDistance(getBargeDieselDistance());
        seaportSubConnection.setRailDieselDistance(getRailDieselDistance());
        seaportSubConnection.setRailElectricDistance(getRailElectricDistance());
        seaportSubConnection.setId(getId());

        seaportSubConnection.setParentConnection(parent);

        return seaportSubConnection;
    }
}
