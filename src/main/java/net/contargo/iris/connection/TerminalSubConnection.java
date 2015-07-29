package net.contargo.iris.connection;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.terminal.Terminal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import javax.validation.constraints.NotNull;


/**
 * Represents a rail part of a {@link MainRunConnection} that's between a {@link Terminal} and a {@link Terminal}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Entity
@DiscriminatorValue("T")
public class TerminalSubConnection extends AbstractSubConnection {

    @NotNull
    @ManyToOne
    private Terminal terminal2;

    public Terminal getTerminal2() {

        return terminal2;
    }


    public void setTerminal2(Terminal terminal2) {

        this.terminal2 = terminal2;
    }


    @Override
    public RouteType getRouteType() {

        return RouteType.RAIL;
    }


    @Override
    public boolean isEnabled() {

        return super.isEnabled() && terminal2 != null && terminal2.isEnabled();
    }


    @Override
    public boolean matchesOriginAndDestination(GeoLocation origin, GeoLocation destination, boolean reverse) {

        if (reverse) {
            return getTerminal().equals(destination) && terminal2.equals(origin);
        } else {
            return getTerminal().equals(origin) && terminal2.equals(destination);
        }
    }
}
