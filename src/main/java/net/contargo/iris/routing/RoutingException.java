/**
 *
 */
package net.contargo.iris.routing;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RoutingException extends RuntimeException {

    private static final long serialVersionUID = -659534689406599095L;

    public RoutingException(String message, Exception cause) {

        super(message, cause);
    }
}
