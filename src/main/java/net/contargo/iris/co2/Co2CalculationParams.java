package net.contargo.iris.co2;

import net.contargo.iris.FlowDirection;
import net.contargo.iris.container.ContainerState;
import net.contargo.iris.terminal.Region;


/**
 * @author  Oliver Messner - messner@synyx.de
 * @author  Julian Schäfer - schaefer@synyx.de
 */
public interface Co2CalculationParams {

    interface Road {

        int getDistance();


        ContainerState getLoadingState();


        boolean isDirectTruck();
    }

    interface Rail {

        enum Direction {

            IMPORT,
            EXPORT
        }

        int getDieselDistance();


        int getElectricDistance();


        Direction getDirection();
    }

    interface Water {

        int getDistance();


        Region getRegion();


        ContainerState getLoadingState();


        FlowDirection getFlowDirection();
    }

    interface Handling {

        int numberOfTerminals();


        boolean isPartOfRoundtrip();
    }
}
