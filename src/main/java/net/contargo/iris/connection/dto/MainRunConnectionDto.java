package net.contargo.iris.connection.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SeaportSubConnection;
import net.contargo.iris.connection.SubConnection;
import net.contargo.iris.connection.TerminalSubConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import java.math.BigDecimal;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public final class MainRunConnectionDto {

    private final Long id;
    private final Long seaportId;
    private final Long terminalId;
    private final BigDecimal bargeDieselDistance;
    private final BigDecimal railDieselDistance;
    private final BigDecimal railElectricDistance;
    private final RouteType routeType;
    private final Boolean enabled = Boolean.TRUE;
    private final List<SubConnectionDto> subConnections;

    @JsonCreator
    public MainRunConnectionDto(@JsonProperty("id") Long id,
        @JsonProperty("seaportId") Long seaportId,
        @JsonProperty("terminalId") Long terminalId,
        @JsonProperty("bargeDieselDistance") BigDecimal bargeDieselDistance,
        @JsonProperty("railDieselDistance") BigDecimal railDieselDistance,
        @JsonProperty("railElectricDistance") BigDecimal railElectricDistance,
        @JsonProperty("routeType") RouteType routeType,
        @JsonProperty("subConnections") List<SubConnectionDto> subConnections) {

        this.id = id;
        this.seaportId = seaportId;
        this.terminalId = terminalId;
        this.bargeDieselDistance = bargeDieselDistance;
        this.railDieselDistance = railDieselDistance;
        this.railElectricDistance = railElectricDistance;
        this.routeType = routeType;
        this.subConnections = subConnections;
    }


    public MainRunConnectionDto(MainRunConnection mainRunConnection) {

        this.id = mainRunConnection.getId();
        this.seaportId = mainRunConnection.getSeaport().getId();
        this.terminalId = mainRunConnection.getTerminal().getId();
        this.bargeDieselDistance = mainRunConnection.getBargeDieselDistance();
        this.railDieselDistance = mainRunConnection.getRailDieselDistance();
        this.railElectricDistance = mainRunConnection.getRailElectricDistance();
        this.routeType = mainRunConnection.getRouteType();
        this.subConnections = mainRunConnection.getSubConnections().stream().map(toDto()).collect(toList());
    }

    private Function<SubConnection, SubConnectionDto> toDto() {

        return
            (SubConnection subConnection) -> {
            if (subConnection instanceof SeaportSubConnection) {
                return new SeaportSubConnectionDto((SeaportSubConnection) subConnection);
            } else {
                return new TerminalSubConnectionDto((TerminalSubConnection) subConnection);
            }
        };
    }


    public Long getId() {

        return id;
    }


    public Long getSeaportId() {

        return seaportId;
    }


    public Long getTerminalId() {

        return terminalId;
    }


    public BigDecimal getBargeDieselDistance() {

        return bargeDieselDistance;
    }


    public BigDecimal getRailDieselDistance() {

        return railDieselDistance;
    }


    public BigDecimal getRailElectricDistance() {

        return railElectricDistance;
    }


    public RouteType getRouteType() {

        return routeType;
    }


    public Boolean getEnabled() {

        return enabled;
    }


    public List<SubConnectionDto> getSubConnections() {

        return subConnections;
    }


    public MainRunConnection toEntity() {

        MainRunConnection connection = new MainRunConnection();

        Seaport seaport = new Seaport();
        seaport.setId(this.seaportId);
        connection.setSeaport(seaport);

        Terminal terminal = new Terminal();
        terminal.setId(this.terminalId);
        connection.setTerminal(terminal);

        connection.setRouteType(this.routeType);
        connection.setBargeDieselDistance(this.bargeDieselDistance);
        connection.setRailDieselDistance(this.railDieselDistance);
        connection.setRailElectricDistance(this.railElectricDistance);
        connection.setEnabled(this.enabled);
        connection.setId(this.id);

        connection.setSubConnections(this.subConnections.stream().map(s -> s.toEntity(connection)).collect(toList()));

        return connection;
    }
}
