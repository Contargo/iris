package net.contargo.iris.connection;

import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import javax.validation.constraints.NotNull;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Entity
@DiscriminatorValue("S")
public class SeaportSubConnection extends SubConnection {

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
}
