package net.contargo.iris.connection.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SubConnection;
import net.contargo.iris.connection.TerminalSubConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TerminalSubConnectionDto extends SubConnectionDto {

    private final BigInteger terminal2Uid;

    @JsonCreator
    public TerminalSubConnectionDto(@JsonProperty("id") Long id,
        @JsonProperty("terminalId") BigInteger terminalUid,
        @JsonProperty("bargeDieselDistance") BigDecimal bargeDieselDistance,
        @JsonProperty("railDieselDistance") BigDecimal railDieselDistance,
        @JsonProperty("railElectricDistance") BigDecimal railElectricDistance,
        @JsonProperty("terminal2Id") BigInteger terminal2Uid) {

        super(id, terminalUid, bargeDieselDistance, railDieselDistance, railElectricDistance);
        this.terminal2Uid = terminal2Uid;
    }


    public TerminalSubConnectionDto(TerminalSubConnection subConnection) {

        super(subConnection);
        this.terminal2Uid = subConnection.getTerminal2().getUniqueId();
    }

    @Override
    public RouteType getRouteType() {

        return RouteType.RAIL;
    }


    public BigInteger getTerminal2Uid() {

        return terminal2Uid;
    }


    @Override
    public SubConnection toEntity(MainRunConnection parent) {

        TerminalSubConnection terminalSubConnection = new TerminalSubConnection();

        Terminal terminal = new Terminal();
        terminal.setUniqueId(getTerminalUid());
        terminalSubConnection.setTerminal(terminal);

        Terminal terminal2 = new Terminal();
        terminal2.setUniqueId(getTerminal2Uid());
        terminalSubConnection.setTerminal2(terminal2);

        terminalSubConnection.setBargeDieselDistance(getBargeDieselDistance());
        terminalSubConnection.setRailDieselDistance(getRailDieselDistance());
        terminalSubConnection.setRailElectricDistance(getRailElectricDistance());
        terminalSubConnection.setId(getId());

        terminalSubConnection.setParentConnection(parent);

        return terminalSubConnection;
    }
}
