package net.contargo.iris.security;

/**
 * A Classification for users. It distinguishes between {@code INTERN} and {@code EXTERN} users. If the classification
 * is not clear {@code UNCLASSIFIED} can be used.
 *
 * @author  David Schilling - schilling@synyx.de
 */
public enum UserClassification {

    EXTERN,
    INTERN,
    UNCLASSIFIED;

    public static UserClassification getByName(String name) {

        return UserClassification.valueOf(name.toUpperCase());
    }
}
