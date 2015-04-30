package net.contargo.iris.util;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Comparator;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public class BigDecimalComparator implements Comparator<BigDecimal>, Serializable {

    private static final long serialVersionUID = -4798283898023294179L;

    @Override
    public int compare(BigDecimal o1, BigDecimal o2) {

        if (o1 == null && o2 == null) {
            return 0;
        }

        if (o1 == null) {
            return 1;
        }

        if (o2 == null) {
            return -1;
        }

        return o1.compareTo(o2);
    }
}
