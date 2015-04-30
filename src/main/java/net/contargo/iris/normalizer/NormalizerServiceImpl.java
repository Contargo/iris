package net.contargo.iris.normalizer;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Normalizer for iris related normalizations.
 *
 * <ul>
 * <li>ä --> ae, ö --> oe, ü --> ue</li>
 * <li>ß --> ss</li>
 * <li>only chars, no numbers, hyphens, etc.</li>
 * <li>to upper case</li>
 * </ul>
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class NormalizerServiceImpl implements NormalizerService {

    private final CustomNormalizer normalizerUE = new CustomNormalizer("[üÜ]", "ue");
    private final CustomNormalizer normalizerOE = new CustomNormalizer("[öÖ]", "oe");
    private final CustomNormalizer normalizerAE = new CustomNormalizer("[äÄ]", "ae");
    private final CustomNormalizer normalizerSS = new CustomNormalizer("ß", "ss");
    private final CustomNormalizer normalizerRemoveNumbers = new CustomNormalizer("\\d", "");
    private final CustomNormalizer normalizerRemovePunct = new CustomNormalizer("\\p{Punct}", "");
    private final CustomNormalizer normalizerRemoveWhitespaces = new CustomNormalizer("\\s", "");

    @Override
    public String normalize(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        String normalizedText;

        normalizedText = replace(text, normalizerUE);
        normalizedText = replace(normalizedText, normalizerOE);
        normalizedText = replace(normalizedText, normalizerAE);
        normalizedText = replace(normalizedText, normalizerSS);
        normalizedText = replace(normalizedText, normalizerRemoveNumbers);
        normalizedText = replace(normalizedText, normalizerRemovePunct);
        normalizedText = replace(normalizedText, normalizerRemoveWhitespaces);

        normalizedText = normalizedText.toUpperCase(Locale.getDefault());

        return normalizedText;
    }


    private String replace(String text, CustomNormalizer normalizer) {

        Matcher matcher = Pattern.compile(normalizer.getRegex()).matcher(text);

        StringBuffer stringBuffer = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, normalizer.getReplacement());
        }

        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

    private static final class CustomNormalizer {

        private final String regex;

        private final String replacement;

        private CustomNormalizer(String regex, String replacement) {

            this.regex = regex;
            this.replacement = replacement;
        }

        public String getRegex() {

            return regex;
        }


        public String getReplacement() {

            return replacement;
        }
    }
}
