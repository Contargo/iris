package net.contargo.iris.util;

/**
 * Exception thrown if anything goes awry in {@link net.contargo.iris.util.HttpUtil}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class HttpUtilException extends RuntimeException {

    public HttpUtilException(String message, Throwable t) {

        super(message, t);
    }
}
