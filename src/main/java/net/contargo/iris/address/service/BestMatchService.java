package net.contargo.iris.address.service;

import java.util.Optional;


/**
 * @author  David Schilling - schilling@synyx.de
 */
public interface BestMatchService {

    Optional<BestMatch> bestMatch(String postalcode, String city, String countryCode);
}
