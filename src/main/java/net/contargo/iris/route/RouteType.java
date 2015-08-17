package net.contargo.iris.route;

/**
 * Enum to mark a {@link Route}.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Aljona Murygina - murygina@synyx.de
 */
public enum RouteType {

    BARGE("route.type.barge"),
    RAIL("route.type.rail"),
    TRUCK("route.type.truck"),
    BARGE_RAIL("route.type.bargerail");

    private String messageKey;

    RouteType(String messageKey) {

        this.messageKey = messageKey;
    }

    public String getMessageKey() {

        return messageKey;
    }
}
