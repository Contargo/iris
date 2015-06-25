package net.contargo.iris.connection.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SubConnection;
import net.contargo.iris.connection.TerminalSubConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TerminalSubConnectionDto extends SubConnectionDto {

    private final Long terminal2Id;

    @JsonCreator
    public TerminalSubConnectionDto(@JsonProperty("id") Long id,
        @JsonProperty("terminalId") Long terminalId,
        @JsonProperty("bargeDieselDistance") BigDecimal bargeDieselDistance,
        @JsonProperty("railDieselDistance") BigDecimal railDieselDistance,
        @JsonProperty("railElectricDistance") BigDecimal railElectricDistance,
        @JsonProperty("terminal2Id") Long terminal2Id) {

        super(id, terminalId, bargeDieselDistance, railDieselDistance, railElectricDistance);
        this.terminal2Id = terminal2Id;
    }


    public TerminalSubConnectionDto(TerminalSubConnection subConnection) {

        super(subConnection);
        this.terminal2Id = subConnection.getTerminal2().getId();
    }

    @Override
    public RouteType getRouteType() {

        return RouteType.RAIL;
    }


    public Long getTerminal2Id() {

        return terminal2Id;
    }


    @Override
    public SubConnection toEntity(MainRunConnection parent) {

        TerminalSubConnection terminalSubConnection = new TerminalSubConnection();

        Terminal terminal = new Terminal();
        terminal.setId(getTerminalId());
        terminalSubConnection.setTerminal(terminal);

        Terminal terminal2 = new Terminal();
        terminal2.setId(getTerminal2Id());
        terminalSubConnection.setTerminal2(terminal2);

        terminalSubConnection.setBargeDieselDistance(getBargeDieselDistance());
        terminalSubConnection.setRailDieselDistance(getRailDieselDistance());
        terminalSubConnection.setRailElectricDistance(getRailElectricDistance());
        terminalSubConnection.setId(getId());

        terminalSubConnection.setParentConnection(parent);

        return terminalSubConnection;
    }
}
