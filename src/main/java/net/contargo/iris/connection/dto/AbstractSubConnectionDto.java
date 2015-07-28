package net.contargo.iris.connection.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.route.RouteType;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@JsonTypeInfo(use = NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "routeType")
@JsonSubTypes(
    {
        @JsonSubTypes.Type(value = SeaportSubConnectionDto.class, name = "BARGE"),
        @JsonSubTypes.Type(
            value = TerminalSubConnectionDto.class, name = "RAIL"
        )
    }
)
public abstract class AbstractSubConnectionDto {

    private final Long id;
    private final String terminalUid;
    private final BigDecimal bargeDieselDistance;
    private final BigDecimal railDieselDistance;
    private final BigDecimal railElectricDistance;

    public AbstractSubConnectionDto(Long id, String terminalUid, BigDecimal bargeDieselDistance,
        BigDecimal railDieselDistance, BigDecimal railElectricDistance) {

        this.id = id;
        this.terminalUid = terminalUid;
        this.bargeDieselDistance = bargeDieselDistance;
        this.railDieselDistance = railDieselDistance;
        this.railElectricDistance = railElectricDistance;
    }


    public AbstractSubConnectionDto(AbstractSubConnection subConnection) {

        this.id = subConnection.getId();
        this.terminalUid = subConnection.getTerminal().getUniqueId().toString();
        this.bargeDieselDistance = subConnection.getBargeDieselDistance();
        this.railDieselDistance = subConnection.getRailDieselDistance();
        this.railElectricDistance = subConnection.getRailElectricDistance();
    }

    public Long getId() {

        return id;
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


    public abstract RouteType getRouteType();


    public abstract AbstractSubConnection toEntity(MainRunConnection parent);
}
