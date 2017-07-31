package net.contargo.iris.connection;

import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import net.contargo.validation.bigdecimal.BigDecimalValidate;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import javax.validation.constraints.NotNull;

import static net.contargo.iris.route.RouteType.BARGE_RAIL;


/**
 * Represents a connection between a {@link Terminal} and a {@link Seaport}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Vincent Potucek - potucek@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Entity
@Table(
    name = "Connection", uniqueConstraints =
        @UniqueConstraint(columnNames = { "seaport_id", "terminal_id", "routeType" })
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
    private BigDecimal bargeDieselDistance;

    @NotNull
    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    private BigDecimal railDieselDistance;

    @NotNull
    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    private BigDecimal railElectricDistance;

    @NotNull
    @BigDecimalValidate(minValue = 0, minDecimalPlaces = 1L, maxDecimalPlaces = TEN, maxFractionalPlaces = TEN)
    private BigDecimal roadDistance;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RouteType routeType;

    @NotNull
    private Boolean enabled = Boolean.TRUE;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentConnection", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AbstractSubConnection> subConnections;

    public MainRunConnection() {

        // JPA needs no-arg constructor
        this.subConnections = new ArrayList<>();
    }


    public MainRunConnection(Seaport seaport) {

        super();
        this.seaport = seaport;
        this.subConnections = new ArrayList<>();
    }

    /**
     * Computes the total distance of this {@link MainRunConnection} as the sum of its barge distance, its diesel rail
     * distance and its electrical rail distance.
     *
     * @return  total distance
     */
    public BigDecimal getTotalDistance() {

        return getRailDieselDistance().add(getRailElectricDistance()).add(getBargeDieselDistance());
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


    public BigDecimal getBargeDieselDistance() {

        if (routeType == BARGE_RAIL) {
            BigDecimal distance = BigDecimal.ZERO;

            for (AbstractSubConnection subConnection : subConnections) {
                distance = distance.add(subConnection.getBargeDieselDistance());
            }

            return distance;
        } else {
            return bargeDieselDistance;
        }
    }


    public void setBargeDieselDistance(BigDecimal bargeDistance) {

        this.bargeDieselDistance = bargeDistance;
    }


    public BigDecimal getRailDieselDistance() {

        if (routeType == BARGE_RAIL) {
            BigDecimal distance = BigDecimal.ZERO;

            for (AbstractSubConnection subConnection : subConnections) {
                distance = distance.add(subConnection.getRailDieselDistance());
            }

            return distance;
        } else {
            return railDieselDistance;
        }
    }


    public void setRailDieselDistance(BigDecimal dieselDistance) {

        this.railDieselDistance = dieselDistance;
    }


    public BigDecimal getRailElectricDistance() {

        if (routeType == BARGE_RAIL) {
            BigDecimal distance = BigDecimal.ZERO;

            for (AbstractSubConnection subConnection : subConnections) {
                distance = distance.add(subConnection.getRailElectricDistance());
            }

            return distance;
        } else {
            return railElectricDistance;
        }
    }


    public void setRailElectricDistance(BigDecimal electricDistance) {

        this.railElectricDistance = electricDistance;
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


    public List<AbstractSubConnection> getSubConnections() {

        return subConnections;
    }


    public void setSubConnections(List<AbstractSubConnection> subConnections) {

        this.subConnections = subConnections;
    }


    public BigDecimal getRoadDistance() {

        return roadDistance;
    }


    public void setRoadDistance(BigDecimal roadDistance) {

        this.roadDistance = roadDistance;
    }


    /**
     * Checks whether this {@link MainRunConnection} is enabled along with its {@link Seaport} and {@link Terminal} and
     * all its {@link AbstractSubConnection}s.
     *
     * @return  true if each of them is enabled, false otherwise
     */
    public Boolean getEverythingEnabled() {

        if (!enabled) {
            return false;
        }

        if (!seaport.isEnabled()) {
            return false;
        }

        if (!terminal.isEnabled()) {
            return false;
        }

        if (!subConnections.isEmpty()) {
            for (AbstractSubConnection subConnection : subConnections) {
                if (!subConnection.isEnabled()) {
                    return false;
                }
            }
        }

        return true;
    }


    @Override
    public String toString() {

        return "MainRunConnection [id=" + id + ", seaport=" + seaport
            + ", location=" + terminal + ", bargeDieselDistance=" + bargeDieselDistance + ", railDieselDistance="
            + railDieselDistance
            + ", railElectricDistance=" + railElectricDistance
            + ", roadDistance=" + roadDistance
            + ", routeType=" + routeType + "]";
    }
}
