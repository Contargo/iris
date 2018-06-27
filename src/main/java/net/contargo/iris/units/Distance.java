package net.contargo.iris.units;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Distance {

    public final Integer value;
    public final LengthUnit unit;

    public Distance(Integer value, LengthUnit unit) {

        this.value = value;
        this.unit = unit;
    }
}
