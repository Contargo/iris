package net.contargo.iris.address.staticsearch.validator;

import java.util.regex.Pattern;


/**
 * Validates if a given String is an iris provides static address hashkey.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class HashKeyValidator {

    private static final String HASHKEY_PATTERN = "^[A-Z0-9]{5}$";

    private final Pattern pattern;

    public HashKeyValidator() {

        pattern = Pattern.compile(HASHKEY_PATTERN);
    }

    /**
     * Validate hashKey with regular expression.
     *
     * @param  hashKey  hashKey for validation
     *
     * @return  true valid hashKey, false invalid hashKey
     */
    public boolean validate(final String hashKey) {

        return hashKey != null && pattern.matcher(hashKey).matches();
    }
}
