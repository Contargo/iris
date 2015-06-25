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
import java.math.BigInteger;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public final class MainRunConnectionDto {

    private final Long id;
    private final String seaportUid;
    private final String terminalUid;
    private final BigDecimal bargeDieselDistance;
    private final BigDecimal railDieselDistance;
    private final BigDecimal railElectricDistance;
    private final RouteType routeType;
    private final Boolean enabled;
    private final List<SubConnectionDto> subConnections;

    @JsonCreator
    public MainRunConnectionDto(@JsonProperty("id") Long id,
        @JsonProperty("seaportId") String seaportUid,
        @JsonProperty("terminalId") String terminalUid,
        @JsonProperty("bargeDieselDistance") BigDecimal bargeDieselDistance,
        @JsonProperty("railDieselDistance") BigDecimal railDieselDistance,
        @JsonProperty("railElectricDistance") BigDecimal railElectricDistance,
        @JsonProperty("routeType") RouteType routeType,
        @JsonProperty("enabled") Boolean enabled,
        @JsonProperty("subConnections") List<SubConnectionDto> subConnections) {

        this.id = id;
        this.seaportUid = seaportUid;
        this.terminalUid = terminalUid;
        this.bargeDieselDistance = bargeDieselDistance;
        this.railDieselDistance = railDieselDistance;
        this.railElectricDistance = railElectricDistance;
        this.routeType = routeType;
        this.enabled = enabled;
        this.subConnections = subConnections;
    }


    public MainRunConnectionDto(MainRunConnection mainRunConnection) {

        this.id = mainRunConnection.getId();
        this.seaportUid = mainRunConnection.getSeaport().getUniqueId().toString();
        this.terminalUid = mainRunConnection.getTerminal().getUniqueId().toString();
        this.bargeDieselDistance = mainRunConnection.getBargeDieselDistance();
        this.railDieselDistance = mainRunConnection.getRailDieselDistance();
        this.railElectricDistance = mainRunConnection.getRailElectricDistance();
        this.routeType = mainRunConnection.getRouteType();
        this.enabled = mainRunConnection.getEnabled();
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


    public String getSeaportUid() {

        return seaportUid;
    }


    public String getTerminalUid() {

        return terminalUid;
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
        seaport.setUniqueId(new BigInteger(this.seaportUid));
        connection.setSeaport(seaport);

        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger(this.terminalUid));
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
