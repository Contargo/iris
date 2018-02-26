package net.contargo.iris.address.w3w;

import java.util.regex.Pattern;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class ThreeWordMatcher {

    private static final Pattern THREE_WORD_PATTERN = Pattern.compile("^\\p{L}+\\.\\p{L}+\\.\\p{L}+$");

    public static boolean isThreeWordAddress(String input) {

        return input != null && THREE_WORD_PATTERN.matcher(input.trim()).matches();
    }
}
