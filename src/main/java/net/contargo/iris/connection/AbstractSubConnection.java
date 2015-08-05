package net.contargo.iris.connection;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.terminal.Terminal;

import net.contargo.validation.bigdecimal.BigDecimalValidate;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.validation.constraints.NotNull;


/**
 * Represents a part of a {@link MainRunConnection}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Entity(name = "SubConnection")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subconnectionType")
public abstract class AbstractSubConnection {

    private static final long TEN = 10L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Terminal terminal;

    @NotNull
    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    private BigDecimal bargeDieselDistance;

    @NotNull
    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    private BigDecimal railDieselDistance;

    @NotNull
    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    private BigDecimal railElectricDistance;

    @ManyToOne
    @JoinColumn(name = "parentConnection_id")
    private MainRunConnection parentConnection;

    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public Terminal getTerminal() {

        return terminal;
    }


    public void setTerminal(Terminal terminal) {

        this.terminal = terminal;
    }


    public BigDecimal getBargeDieselDistance() {

        return bargeDieselDistance;
    }


    public void setBargeDieselDistance(BigDecimal bargeDieselDistance) {

        this.bargeDieselDistance = bargeDieselDistance;
    }


    public BigDecimal getRailDieselDistance() {

        return railDieselDistance;
    }


    public void setRailDieselDistance(BigDecimal railDieselDistance) {

        this.railDieselDistance = railDieselDistance;
    }


    public BigDecimal getRailElectricDistance() {

        return railElectricDistance;
    }


    public void setRailElectricDistance(BigDecimal railElectricDistance) {

        this.railElectricDistance = railElectricDistance;
    }


    public abstract RouteType getRouteType();


    public MainRunConnection getParentConnection() {

        return parentConnection;
    }


    public void setParentConnection(MainRunConnection parentConnection) {

        this.parentConnection = parentConnection;
    }


    boolean isEnabled() {

        return terminal != null && terminal.isEnabled();
    }


    public BigDecimal getTotalDistance() {

        return getBargeDieselDistance().add(getRailDieselDistance()).add(getRailElectricDistance());
    }


    public abstract boolean matchesOriginAndDestination(GeoLocation origin, GeoLocation destination, boolean reverse);
}
