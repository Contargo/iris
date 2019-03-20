package net.contargo.iris.co2.advice;

import net.contargo.iris.co2.Co2CalculationParams;
import net.contargo.iris.route.RouteDirection;
import net.contargo.iris.route.RoutePart;

import static java.math.RoundingMode.UP;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class Co2CalculationRailParams implements Co2CalculationParams.Rail {

    private final int dieselDistance;
    private final int electricDistance;
    private final Direction direction;

    Co2CalculationRailParams(RouteDirection routeDirection, RoutePart routePart) {

        dieselDistance = routePart.getData().getRailDieselDistance().setScale(0, UP).intValue();
        electricDistance = routePart.getData().getElectricDistance().setScale(0, UP).intValue();

        switch (routeDirection) {
            case IMPORT:
                direction = Direction.IMPORT;
                break;

            case EXPORT:
                direction = Direction.EXPORT;
                break;

            default:
                throw new IllegalArgumentException("Illegal route direction: " + routeDirection);
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
