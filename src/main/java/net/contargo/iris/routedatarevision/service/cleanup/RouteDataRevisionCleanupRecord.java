package net.contargo.iris.routedatarevision.service.cleanup;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.truck.TruckRoute;

import java.math.BigDecimal;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteDataRevisionCleanupRecord {

    private final Long id;
    private final String terminal;
    private final String postalCode;
    private final String city;
    private final String country;
    private final String comment;
    private final BigDecimal revisionTruckDistance;
    private final BigDecimal revisionTollDistance;
    private final BigDecimal currentTruckDistance;
    private final BigDecimal currentTollDistance;

    public RouteDataRevisionCleanupRecord(RouteDataRevision revision, TruckRoute route) {

        id = revision.getId();
        terminal = revision.getTerminal().getName();
        postalCode = revision.getPostalCode();
        city = revision.getCity();
        country = revision.getCountry();
        comment = revision.getComment();
        revisionTruckDistance = revision.getTruckDistanceOneWayInKilometer();
        revisionTollDistance = revision.getTollDistanceOneWayInKilometer();
        currentTruckDistance = route.getDistance();
        currentTollDistance = route.getTollDistance();
    }

    public long getId() {

        return id;
    }


    public String getTerminal() {

        return terminal;
    }


    public String getPostalCode() {

        return postalCode;
    }


    public String getCity() {

        return city;
    }


    public String getCountry() {

        return country;
    }


    public String getComment() {

        return comment;
    }


    public BigDecimal getRevisionTruckDistance() {

        return revisionTruckDistance;
    }


    public BigDecimal getRevisionTollDistance() {

        return revisionTollDistance;
    }


    public BigDecimal getCurrentTruckDistance() {

        return currentTruckDistance;
    }


    public BigDecimal getCurrentTollDistance() {

        return currentTollDistance;
    }
}
