package net.contargo.iris.connection;

import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import net.contargo.validation.bigdecimal.BigDecimalValidate;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import javax.validation.constraints.NotNull;


/**
 * Represents a connection between a {@link Terminal} and a {@link Seaport}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Vincent Potucek - potucek@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@Entity
@Table(
    name = "Connection",
    uniqueConstraints = @UniqueConstraint(columnNames = { "seaport_id", "terminal_id", "routeType" })
)
public class MainRunConnection {

    private static final long TEN = 10L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private Seaport seaport;

    @NotNull
    @ManyToOne
    private Terminal terminal;

    @NotNull
    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    private BigDecimal dieselDistance;

    @NotNull
    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    private BigDecimal electricDistance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RouteType routeType;

    @NotNull
    private Boolean enabled = Boolean.TRUE;

    public MainRunConnection() {

        // JPA needs no-arg constructor
    }


    public MainRunConnection(Seaport seaport) {

        super();
        this.seaport = seaport;
    }

    /**
     * Computes the total distance of this {@link MainRunConnection} as the sum of its diesel distance and its
     * electrical distance.
     *
     * @return  total distance
     */
    public BigDecimal getTotalDistance() {

        return getDieselDistance().add(getElectricDistance());
    }


    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public Seaport getSeaport() {

        return seaport;
    }


    public void setSeaport(Seaport seaport) {

        this.seaport = seaport;
    }


    public Terminal getTerminal() {

        return terminal;
    }


    public void setTerminal(Terminal terminal) {

        this.terminal = terminal;
    }


    public BigDecimal getDieselDistance() {

        return dieselDistance;
    }


    public void setDieselDistance(BigDecimal dieselDistance) {

        this.dieselDistance = dieselDistance;
    }


    public BigDecimal getElectricDistance() {

        return electricDistance;
    }


    public void setElectricDistance(BigDecimal electricDistance) {

        this.electricDistance = electricDistance;
    }


    public RouteType getRouteType() {

        return routeType;
    }


    public void setRouteType(RouteType routeType) {

        this.routeType = routeType;
    }


    public Boolean getEnabled() {

        return enabled;
    }


    public void setEnabled(Boolean enabled) {

        this.enabled = enabled;
    }


    /**
     * Checks whether this {@link MainRunConnection} along with both its {@link Seaport} and {@link Terminal}.
     *
     * @return  true if each of them is enabled, false otherwise
     */
    public Boolean getEverythingEnabled() {

        if (!enabled) {
            return false;
        }

        if (seaport == null) {
            return false;
        }

        if (!seaport.isEnabled()) {
            return false;
        }

        if (null == terminal) {
            return false;
        }

        if (!terminal.isEnabled()) {
            return false;
        }

        return true;
    }


    @Override
    public String toString() {

        return "MainRunConnection [id=" + id + ", seaport=" + seaport
            + ", location=" + terminal + ", dieselDistance=" + dieselDistance + ", electricDistance=" + electricDistance
            + ", routeType=" + routeType + "]";
    }
}
