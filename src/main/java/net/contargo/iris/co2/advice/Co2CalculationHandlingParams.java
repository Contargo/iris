package net.contargo.iris.co2.advice;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.terminal.Terminal;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2CalculationHandlingParams implements Co2CalculationParams.Handling {

    private final int numberOfTerminals;

    public Co2CalculationHandlingParams(RoutePart routePart) {

        int numberOfTerminals = 0;

        numberOfTerminals += isTerminal(routePart.getOrigin()) ? 1 : 0;
        numberOfTerminals += isTerminal(routePart.getDestination()) ? 1 : 0;

        this.numberOfTerminals = numberOfTerminals;
    }

    private static boolean isTerminal(GeoLocation location) {

        return location instanceof Terminal;
    }


    @Override
    public int numberOfTerminals() {

        return numberOfTerminals;
    }


    @Override
    public boolean isPartOfRoundtrip() {

        return false;
    }
}
