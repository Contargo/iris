package net.contargo.iris.connection.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.TerminalSubConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
public final class TerminalSubConnectionDto extends AbstractSubConnectionDto {

    private final String terminal2Uid;

    @JsonCreator
    public TerminalSubConnectionDto(@JsonProperty("id") Long id,
        @JsonProperty("terminalId") String terminalUid,
        @JsonProperty("bargeDieselDistance") BigDecimal bargeDieselDistance,
        @JsonProperty("railDieselDistance") BigDecimal railDieselDistance,
        @JsonProperty("railElectricDistance") BigDecimal railElectricDistance,
        @JsonProperty("terminal2Id") String terminal2Uid) {

        super(id, terminalUid, bargeDieselDistance, railDieselDistance, railElectricDistance);
        this.terminal2Uid = terminal2Uid;
    }


    public TerminalSubConnectionDto(TerminalSubConnection subConnection) {

        super(subConnection);
        this.terminal2Uid = subConnection.getTerminal2().getUniqueId().toString();
    }

    @Override
    public RouteType getRouteType() {

        return RouteType.RAIL;
    }


    public String getTerminal2Uid() {

        return terminal2Uid;
    }


    @Override
    public AbstractSubConnection toEntity(MainRunConnection parent) {

        TerminalSubConnection terminalSubConnection = new TerminalSubConnection();

        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger(getTerminalUid()));
        terminalSubConnection.setTerminal(terminal);

        Terminal terminal2 = new Terminal();
        terminal2.setUniqueId(new BigInteger(getTerminal2Uid()));
        terminalSubConnection.setTerminal2(terminal2);

        terminalSubConnection.setBargeDieselDistance(getBargeDieselDistance());
        terminalSubConnection.setRailDieselDistance(getRailDieselDistance());
        terminalSubConnection.setRailElectricDistance(getRailElectricDistance());
        terminalSubConnection.setId(getId());

        terminalSubConnection.setParentConnection(parent);

        return terminalSubConnection;
    }
}
