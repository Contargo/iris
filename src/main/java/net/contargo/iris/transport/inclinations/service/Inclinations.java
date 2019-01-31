package net.contargo.iris.transport.inclinations.service;

import static java.lang.Math.abs;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class Inclinations {

    private final int up;
    private final int down;

    private Inclinations(int up, int down) {

        this.up = up;
        this.down = down;
    }

    public int getUp() {

        return up;
    }


    public int getDown() {

        return down;
    }


    Inclinations add(Inclinations other) {

        return this.add(other.up, other.down);
    }


    Inclinations with(int elevationDifference) {

        if (elevationDifference > 0) {
            return this.add(elevationDifference, 0);
        } else {
            return this.add(0, abs(elevationDifference));
        }
    }


    private Inclinations add(int up, int down) {

        return new Inclinations(this.up + up, this.down + down);
    }


    static Inclinations zero() {

        return new Inclinations(0, 0);
    }
}
