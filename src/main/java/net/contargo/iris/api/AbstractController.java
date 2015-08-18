package net.contargo.iris.api;

import net.contargo.iris.Message;

import static net.contargo.iris.Message.error;


/**
 * @author  Vincent Potucek - potucek@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public abstract class AbstractController {

    public static final Message UNIQUEID_ERROR_MESSAGE = error("uniqueid.error.message");

    // Domain specific constants
    public static final String TERMINALS = "terminals";
    public static final String SEAPORTS = "seaports";
    public static final String STATIC_ADDRESSES = "staticaddresses";
    public static final String CONNECTIONS = "connections";
    public static final String LOGIN = "login";
    public static final String ROUTE_DETAILS = "routedetails";
    public static final String OSM_ADDRESSES = "osmaddresses";
    public static final String COUNTRIES = "countries";
    public static final String REVERSE_GEOCODE = "reversegeocode";
    public static final String ADDRESSES = "addresses";
    public static final String PLACES = "places";
    public static final String GEOCODES = "geocodes";
    public static final String SIMPLE_GEOCODES = "simplegeocodes";
    public static final String TRIANGLE = "triangle";
    public static final String TRIANGLE_VIEW = "routing/triangle";

    // parameter constants
    public static final String ID_PARAM = "{id}";
    public static final String ID = "id";
    public static final String PARAM_LATITUDE = "{lat}";
    public static final String PARAM_LONGITUDE = "{lon}";

    // character constants
    public static final String SLASH = "/";
    public static final String COLON = ":";
    public static final String STAR = "*";

    // navigation constants
    public static final String MESSAGE = "message";
    public static final String REDIRECT = "redirect:";
    public static final String RESPONSE = "response";
    public static final String WEB = "web";
    public static final String INDEX = "index";
    public static final String WEBAPI_ROOT_URL = SLASH + WEB + SLASH;
}
