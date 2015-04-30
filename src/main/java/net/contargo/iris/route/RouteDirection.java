package net.contargo.iris.route;

/**
 * Direction of a Routing (is stuff exported or imported).
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public enum RouteDirection {

    IMPORT,
    EXPORT;

    /**
     * @param  isImport  boolean that indicates if it is an IMPORT
     *
     * @return  IMPORT if isImport is true, EXPORT otherwise
     */
    public static RouteDirection fromIsImport(boolean isImport) {

        if (isImport) {
            return IMPORT;
        } else {
            return EXPORT;
        }
    }
}
