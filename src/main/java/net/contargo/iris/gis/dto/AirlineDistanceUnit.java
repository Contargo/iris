package net.contargo.iris.gis.dto;

/**
 * @author  Oliver Messner - messner@synyx.de
 */
public enum AirlineDistanceUnit {

    METER("meter");

    private final String name;

    private AirlineDistanceUnit(String name) {

        this.name = name;
    }

    public String getName() {

        return name;
    }
}
