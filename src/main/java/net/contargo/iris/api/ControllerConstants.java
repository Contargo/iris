package net.contargo.iris.api;

import net.contargo.iris.Message;

import static net.contargo.iris.Message.error;


/**
 * @author  Vincent Potucek - potucek@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public final class ControllerConstants {

    public static final Message UNIQUEID_ERROR_MESSAGE = error("uniqueid.error.message");

    public static final String REDIRECT = "redirect:";
    public static final String RESPONSE = "response";
    public static final String WEBAPI_ROOT_URL = "/web/";

    private ControllerConstants() {

        // utility class constructor
    }
}
