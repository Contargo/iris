package net.contargo.iris.address.staticsearch.service;

/**
 * Is thrown when new StaticAddresses are putted in the db which, are duplicates (coordinates) to others.
 *
 * @author  Michael Herbold - herbold@synyx.de
 */

public class StaticAddressCoordinatesDuplicationException extends RuntimeException {
}
