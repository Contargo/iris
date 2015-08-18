package net.contargo.iris.api.discover;

import com.mangofactory.swagger.annotations.ApiIgnore;

import net.contargo.iris.address.api.AddressApiController;
import net.contargo.iris.api.ControllerConstants;
import net.contargo.iris.connection.api.SeaportConnectionApiController;
import net.contargo.iris.countries.api.CountriesApiController;
import net.contargo.iris.route.RouteCombo;
import net.contargo.iris.route.api.RouteEnricherApiController;
import net.contargo.iris.route.api.RoutesApiController;
import net.contargo.iris.seaport.api.SeaportApiController;
import net.contargo.iris.terminal.api.TerminalApiController;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.math.BigInteger;

import static net.contargo.iris.container.ContainerType.TWENTY_LIGHT;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


/**
 * Controller which presents all public API's as link so they can be used to move through the API.
 *
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
@Controller
@RequestMapping
@ApiIgnore
public class DiscoverPublicApiController {

    static final String REL_COUNTRIES = "countries";
    static final String REL_REVERSE_GEOCODE = "reversegeocode";
    static final String REL_GEOCODE = "geocode";
    static final String REL_TERMINALS = "terminals";
    static final String REL_TERMINAL_EXAMPLE = "terminal (by uid)";
    static final String REL_SEAPORTS_OF_CONNECTIONS = "seaports (as part of connections)";
    static final String REL_SEAPORTS_OF_CONNECTIONS_FILTERED = "seaports (as part of connections, filtered)";
    static final String REL_SEAPORT_EXAMPLE = "seaport (by uid)";
    static final String REL_CONNECTIONS = "connections_url";
    static final String REL_ROUTES = "routes";
    static final String REL_SIMPLE_GEOCODES_EXAMPLE = "simplegeocodes_example";
    static final String REL_ROUTE_DETAILS_EXAMPLE = "route_details_example";
    static final String REL_OSM_ADDRESSES = "osmaddresses";

    private static final Double SEAPORTS_LAT = 49.0;
    private static final Double SEAPORTS_LON = 8.41;

    private static final BigInteger SEAPORT_UID = new BigInteger("1301000000000001");
    private static final BigInteger TERMINAL_UID = new BigInteger("1301000000000001");

    @Value(value = "${application.version}")
    private String applicationVersion;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ModelAttribute(ControllerConstants.RESPONSE)
    public DiscoverResponse discover() throws NoSuchMethodException {

        DiscoverResponse discoverResponse = new DiscoverResponse(applicationVersion);

        // routes
        discoverResponse.add(linkTo(
                    methodOn(RoutesApiController.class).getSeaportRoutes(SEAPORT_UID, SEAPORTS_LAT, SEAPORTS_LON, true,
                        TWENTY_LIGHT, false, RouteCombo.WATERWAY)).withRel(REL_CONNECTIONS));
        discoverResponse.add(linkTo(
                    methodOn(RoutesApiController.class).getRoutes(SEAPORT_UID, SEAPORTS_LAT, SEAPORTS_LON, true,
                        TWENTY_LIGHT, false, RouteCombo.WATERWAY)).withRel(REL_ROUTES));

        // countries
        discoverResponse.add(linkTo(CountriesApiController.class).withRel(REL_COUNTRIES));

        // osmaddresses
        discoverResponse.add(linkTo(AddressApiController.class).slash("osmaddresses").slash(
                "134631686?_=1381911583029").withRel(REL_OSM_ADDRESSES));

        // reverse_geocode
        discoverResponse.add(linkTo(
                    methodOn(AddressApiController.class).addressByGeolocation(new BigDecimal("49.123"),
                        new BigDecimal("8.12"))).withRel(REL_REVERSE_GEOCODE));

        // geocode
        discoverResponse.add(linkTo(AddressApiController.class).slash("geocodes?city=Karlsruhe&postalcode=76137")
            .withRel(REL_GEOCODE));

        // seaport
        discoverResponse.add(linkTo(methodOn(SeaportApiController.class).getSeaportById(SEAPORT_UID)).withRel(
                REL_SEAPORT_EXAMPLE));
        discoverResponse.add(linkTo(
                    methodOn(SeaportConnectionApiController.class).getSeaportsInConnections(RouteCombo.ALL)).withRel(
                REL_SEAPORTS_OF_CONNECTIONS));
        discoverResponse.add(linkTo(
                    methodOn(SeaportConnectionApiController.class).getSeaportsInConnections(RouteCombo.RAILWAY))
            .withRel(REL_SEAPORTS_OF_CONNECTIONS_FILTERED));

        // terminal (by uid)
        discoverResponse.add(linkTo(methodOn(TerminalApiController.class).getTerminalByUid(TERMINAL_UID)).withRel(
                REL_TERMINAL_EXAMPLE));

        // terminals
        discoverResponse.add(linkTo(methodOn(TerminalApiController.class).getTerminals(true)).withRel(REL_TERMINALS));
        discoverResponse.add(linkTo(AddressApiController.class).slash(
                "simplegeocodes?city=Karlsruhe&postalcode=76137").withRel(REL_SIMPLE_GEOCODES_EXAMPLE));

        discoverResponse.add(linkTo(RouteEnricherApiController.class).slash(
                    "?data.parts[0].origin.longitude=4.3&data.parts[0].origin.latitude=51.36833"
                    + "&data.parts[0].destination.longitude=8.2852700000"
                    + "&data.parts[0].destination.latitude=49.0690300000"
                    + "&data.parts[0].routeType=BARGE&data.parts[0].containerType=TWENTY_LIGHT"
                    + "&data.parts[0].containerState=FULL"
                    + "&data.parts[1].origin.longitude=8.2852700000&data.parts[1].origin.latitude=49.0690300000"
                    + "&data.parts[1].destination.longitude=8.41&data.parts[1].destination.latitude=49.0"
                    + "&data.parts[1].routeType=TRUCK&data.parts[1].containerType=TWENTY_LIGHT"
                    + "&data.parts[1].containerState=FULL"
                    + "&data.parts[2].origin.longitude=8.41&data.parts[2].origin.latitude=49.0"
                    + "&data.parts[2].destination.longitude=8.2852700000"
                    + "&data.parts[2].destination.latitude=49.0690300000"
                    + "&data.parts[2].routeType=TRUCK&data.parts[2].containerType=TWENTY_LIGHT"
                    + "&data.parts[2].containerState=EMPTY"
                    + "&data.parts[3].origin.longitude=8.2852700000&data.parts[3].origin.latitude=49.0690300000"
                    + "&data.parts[3].destination.longitude=4.3&data.parts[3].destination.latitude=51.36833"
                    + "&data.parts[3].routeType=BARGE"
                    + "&data.parts[3].containerType=TWENTY_LIGHT&data.parts[3].containerState=EMPTY")
            .withRel(REL_ROUTE_DETAILS_EXAMPLE));

        return discoverResponse;
    }
}
