package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.staticsearch.StaticAddress;

import java.util.function.Predicate;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
final class PostcodeMappingProcessorUtil {

    private PostcodeMappingProcessorUtil() {

        // prevents instantiation
    }

    static Predicate<StaticAddress> isEmptySuburb() {

        return (StaticAddress s) -> s.getSuburb() == null || s.getSuburb().trim().isEmpty();
    }


    static Predicate<StaticAddress> byCity(String normalizedCity) {

        return (StaticAddress s) -> s.getCityNormalized() != null && s.getCityNormalized().equals(normalizedCity);
    }


    static Predicate<StaticAddress> bySuburb(String normalizedSuburb) {

        return (StaticAddress s) -> s.getSuburbNormalized() != null && s.getSuburbNormalized().equals(normalizedSuburb);
    }
}
