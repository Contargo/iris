package net.contargo.iris.co2.advice;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.route.RoutePart;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.terminal.Terminal;

import static java.math.RoundingMode.UP;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2CalculationRailParams implements Co2CalculationParams.Rail {

    private final int dieselDistance;
    private final int electricDistance;
    private final Direction direction;

    Co2CalculationRailParams(RoutePart routePart) {

        dieselDistance = routePart.getData().getRailDieselDistance().setScale(0, UP).intValue();
        electricDistance = routePart.getData().getElectricDistance().setScale(0, UP).intValue();

        if (routePart.getOrigin() instanceof Seaport && routePart.getDestination() instanceof Terminal) {
            direction = Direction.IMPORT;
        } else if (routePart.getOrigin() instanceof Terminal && routePart.getDestination() instanceof Seaport) {
            direction = Direction.EXPORT;
        } else {
            throw new IllegalArgumentException("Illegal route part: " + routePart);
        }
    }

    @Override
    public int getDieselDistance() {

        return dieselDistance;
    }


    @Override
    public int getElectricDistance() {

        return electricDistance;
    }


    @Override
    public Direction getDirection() {

        return direction;
    }
}
