package net.contargo.iris.connection;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import javax.validation.constraints.NotNull;


/**
 * Represents a barge part of a {@link MainRunConnection} that's between a {@link net.contargo.iris.seaport.Seaport} and
 * a {@link net.contargo.iris.terminal.Terminal}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Entity
@DiscriminatorValue("S")
public class SeaportSubConnection extends AbstractSubConnection {

    @NotNull
    @ManyToOne
    private Seaport seaport;

    public Seaport getSeaport() {

        return seaport;
    }


    public void setSeaport(Seaport seaport) {

        this.seaport = seaport;
    }


    @Override
    public RouteType getRouteType() {

        return RouteType.BARGE;
    }


    @Override
    public boolean isEnabled() {

        return super.isEnabled() && seaport != null && seaport.isEnabled();
    }


    @Override
    public boolean matchesOriginAndDestination(GeoLocation origin, GeoLocation destination, boolean reverse) {

        if (reverse) {
            return seaport.equals(destination) && getTerminal().equals(origin);
        } else {
            return seaport.equals(origin) && getTerminal().equals(destination);
        }
    }
}
