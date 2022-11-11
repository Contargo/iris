package net.contargo.iris.rounding;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_DOWN;
import static java.math.BigDecimal.ROUND_UP;


/**
 * Interface for instances that helps to round data informations.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class RoundingService {

    private static final int DIGITS_TO_CUT_DISTANCE = 3;
    private static final int DIGITS_TO_CUT_DURATION = 1;
    private static final int DIGITS_TO_ROUND = 0;

    /**
     * @param  bigDecimal  BigDecimal to round
     *
     * @return  bigDecimal rounded BigDecimal
     */
    public static BigDecimal roundDistance(BigDecimal bigDecimal) {

        return bigDecimal.setScale(DIGITS_TO_CUT_DISTANCE, ROUND_DOWN).setScale(DIGITS_TO_ROUND, ROUND_UP);
    }


    /**
     * @param  bigDecimal  BigDecimal to round
     *
     * @return  bigDecimal rounded BigDecimal
     */
    public static BigDecimal roundDuration(BigDecimal bigDecimal) {

        return bigDecimal.setScale(DIGITS_TO_CUT_DURATION, ROUND_DOWN).setScale(DIGITS_TO_ROUND, ROUND_UP);
    }
}
