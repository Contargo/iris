package net.contargo.iris.connection.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import net.contargo.validation.bigdecimal.BigDecimalValidate;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.validation.constraints.NotNull;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
public final class MainRunConnectionDto {

    private static final long TEN = 10L;
    private final Long id;

    @NotNull
    private final String seaportUid;

    @NotNull
    private final String terminalUid;

    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    @NotNull
    private final BigDecimal bargeDieselDistance;

    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    @NotNull
    private final BigDecimal railDieselDistance;

    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    @NotNull
    private final BigDecimal railElectricDistance;

    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    @NotNull
    private final BigDecimal roadDistance;

    @NotNull
    private final RouteType routeType;

    @NotNull
    private final Boolean enabled;

    @JsonCreator
    public MainRunConnectionDto(@JsonProperty("id") Long id, // NOSONAR dtos should be final
        @JsonProperty("seaportId") String seaportUid,
        @JsonProperty("terminalId") String terminalUid,
        @JsonProperty("bargeDieselDistance") BigDecimal bargeDieselDistance,
        @JsonProperty("railDieselDistance") BigDecimal railDieselDistance,
        @JsonProperty("railElectricDistance") BigDecimal railElectricDistance,
        @JsonProperty("roadDistance") BigDecimal roadDistance,
        @JsonProperty("routeType") RouteType routeType,
        @JsonProperty("enabled") Boolean enabled) {

        this.id = id;
        this.seaportUid = seaportUid;
        this.terminalUid = terminalUid;
        this.bargeDieselDistance = bargeDieselDistance;
        this.railDieselDistance = railDieselDistance;
        this.railElectricDistance = railElectricDistance;
        this.roadDistance = roadDistance;
        this.routeType = routeType;
        this.enabled = enabled;
    }


    public MainRunConnectionDto(MainRunConnection mainRunConnection) {

        this.id = mainRunConnection.getId();
        this.seaportUid = mainRunConnection.getSeaport().getUniqueId().toString();
        this.terminalUid = mainRunConnection.getTerminal().getUniqueId().toString();
        this.bargeDieselDistance = mainRunConnection.getBargeDieselDistance();
        this.railDieselDistance = mainRunConnection.getRailDieselDistance();
        this.railElectricDistance = mainRunConnection.getRailElectricDistance();
        this.roadDistance = mainRunConnection.getRoadDistance();
        this.routeType = mainRunConnection.getRouteType();
        this.enabled = mainRunConnection.getEnabled();
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


    public BigDecimal getRoadDistance() {

        return roadDistance;
    }


    public RouteType getRouteType() {

        return routeType;
    }


    public Boolean getEnabled() {

        return enabled;
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
        connection.setRoadDistance(this.roadDistance);
        connection.setEnabled(this.enabled);
        connection.setId(this.id);

        return connection;
    }
}
