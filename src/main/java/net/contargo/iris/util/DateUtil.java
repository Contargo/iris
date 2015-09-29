package net.contargo.iris.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Date;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public final class DateUtil {

    private DateUtil() {

        // prevents instantiation
    }

    public static LocalDate asLocalDate(Date date) {

        if (date == null) {
            return null;
        }

        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        } else {
            return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }
}
