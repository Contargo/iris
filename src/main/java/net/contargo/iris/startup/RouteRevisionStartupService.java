package net.contargo.iris.startup;

import net.contargo.iris.routedatarevision.service.RouteDataRevisionService;

import javax.annotation.PostConstruct;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public class RouteRevisionStartupService {

    private final RouteDataRevisionService service;

    public RouteRevisionStartupService(RouteDataRevisionService service) {

        this.service = service;
    }

    @PostConstruct
    public void onPostConstruct() {

        service.enrichWithAddressInformation();
    }
}
