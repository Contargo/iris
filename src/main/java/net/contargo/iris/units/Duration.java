package net.contargo.iris.units;

/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Duration {

    public final Integer value;
    public final TimeUnit unit;

    public Duration(Integer value, TimeUnit unit) {

        this.value = value;
        this.unit = unit;
    }
}
