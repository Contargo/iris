package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SubConnection;
import net.contargo.iris.route.RouteType;

import java.math.BigDecimal;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public abstract class SubConnectionDto {

    private final Long id;
    private final Long terminalId;
    private final BigDecimal bargeDieselDistance;
    private final BigDecimal railDieselDistance;
    private final BigDecimal railElectricDistance;

    public SubConnectionDto(Long id, Long terminalId, BigDecimal bargeDieselDistance, BigDecimal railDieselDistance,
        BigDecimal railElectricDistance) {

        this.id = id;
        this.terminalId = terminalId;
        this.bargeDieselDistance = bargeDieselDistance;
        this.railDieselDistance = railDieselDistance;
        this.railElectricDistance = railElectricDistance;
    }


    public SubConnectionDto(SubConnection subConnection) {

        this.id = subConnection.getId();
        this.terminalId = subConnection.getTerminal().getId();
        this.bargeDieselDistance = subConnection.getBargeDieselDistance();
        this.railDieselDistance = subConnection.getRailDieselDistance();
        this.railElectricDistance = subConnection.getRailElectricDistance();
    }

    public Long getId() {

        return id;
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


    public abstract RouteType getRouteType();


    public abstract SubConnection toEntity(MainRunConnection parent);
}
