package net.contargo.iris.rounding;

import java.math.BigDecimal;

import static java.math.BigDecimal.ROUND_DOWN;
import static java.math.BigDecimal.ROUND_UP;


/**
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class RoundingServiceImpl implements RoundingService {

    @Override
    public BigDecimal roundDistance(BigDecimal bigDecimal) {

        return bigDecimal.setScale(DIGITS_TO_CUT_DISTANCE, ROUND_DOWN).setScale(DIGITS_TO_ROUND, ROUND_UP);
    }


    @Override
    public BigDecimal roundDuration(BigDecimal bigDecimal) {

        return bigDecimal.setScale(DIGITS_TO_CUT_DURATION, ROUND_DOWN).setScale(DIGITS_TO_ROUND, ROUND_UP);
    }
}
