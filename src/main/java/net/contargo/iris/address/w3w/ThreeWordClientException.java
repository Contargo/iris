package net.contargo.iris.address.w3w;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class ThreeWordClientException extends RuntimeException {

    ThreeWordClientException(Integer code, String message, String threeWords) {

        super("API of w3w returned error code " + code + " with message '" + message
            + "' for three word address '" + threeWords + "'");
    }
}
