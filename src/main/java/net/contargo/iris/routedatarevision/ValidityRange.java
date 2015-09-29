package net.contargo.iris.routedatarevision;

import java.time.LocalDate;


/**
 * An immutable representing a time range. A time range may even be open ended.
 *
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public final class ValidityRange {

    private final LocalDate validFrom;
    private final LocalDate validTo;

    public ValidityRange(LocalDate validFrom, LocalDate validTo) {

        if (validFrom == null) {
            throw new IllegalArgumentException();
        }

        if (validTo != null && validTo.isBefore(validFrom)) {
            throw new IllegalArgumentException();
        }

        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    /**
     * Checks whether the given {@link ValidityRange} overlaps with this object.
     *
     * @param  other  the other {@code ValidityRange} to check against.
     *
     * @return  {@link true} if there is an overlap. Otherwise {@code false}
     */
    public boolean overlapWith(ValidityRange other) {

        return !notOverlapping(other);
    }


    private boolean notOverlapping(ValidityRange other) {

        if (this.isOpenEnded() && other.isOpenEnded()) {
            return false;
        } else if (this.isOpenEnded()) {
            return this.validFrom.isAfter(other.validTo);
        } else if (other.isOpenEnded()) {
            return other.validFrom.isAfter(this.validTo);
        } else {
            return other.validFrom.isAfter(this.validTo) || other.validTo.isBefore(this.validFrom);
        }
    }


    private boolean isOpenEnded() {

        return validTo == null;
    }
}
