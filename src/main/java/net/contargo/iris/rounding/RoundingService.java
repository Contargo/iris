package net.contargo.iris.rounding;

import java.math.BigDecimal;


/**
 * Interface for instances that helps to round data informations.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public interface RoundingService {

    int DIGITS_TO_CUT_DISTANCE = 3;
    int DIGITS_TO_CUT_DURATION = 1;
    int DIGITS_TO_ROUND = 0;

    /**
     * @param  bigDecimal  BigDecimal to round
     *
     * @return  bigDecimal rounded BigDecimal
     */
    BigDecimal roundDistance(BigDecimal bigDecimal);


    /**
     * @param  bigDecimal  BigDecimal to round
     *
     * @return  bigDecimal rounded BigDecimal
     */
    BigDecimal roundDuration(BigDecimal bigDecimal);
}
