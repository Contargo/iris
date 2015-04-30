package net.contargo.iris.normalizer;

/**
 * Provides services to normalize Strings.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface NormalizerService {

    /**
     * Normalizes a given string by replacing umlaut, whitespaces, ...
     *
     * @param  text  to normalize
     *
     * @return  normalized string
     */
    String normalize(String text);
}
