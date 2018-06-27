package net.contargo.iris.units;

import java.math.BigDecimal;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class Weight {

    public final BigDecimal value;
    public final MassUnit unit;

    public Weight(BigDecimal value, MassUnit unit) {

        this.value = value;
        this.unit = unit;
    }

    public Weight add(BigDecimal value) {

        return new Weight(this.value.add(value), unit);
    }
}
