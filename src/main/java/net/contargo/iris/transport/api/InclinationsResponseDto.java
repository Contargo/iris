package net.contargo.iris.transport.api;

import net.contargo.iris.units.Distance;

import static net.contargo.iris.units.LengthUnit.METER;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class InclinationsResponseDto {

    private final Distance up;
    private final Distance down;

    InclinationsResponseDto(int up, int down) {

        this.up = new Distance(up, METER);
        this.down = new Distance(down, METER);
    }

    public Distance getUp() {

        return up;
    }


    public Distance getDown() {

        return down;
    }
}
