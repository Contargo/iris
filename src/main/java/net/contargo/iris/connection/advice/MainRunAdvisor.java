package net.contargo.iris.connection.advice;

import net.contargo.iris.route.RouteDirection;
import net.contargo.iris.route.RouteProduct;

import static net.contargo.iris.route.RouteDirection.IMPORT;
import static net.contargo.iris.route.RouteProduct.ROUNDTRIP;


/**
 * Determines {@link MainRunStrategy} strategy for building routes based on {@link RouteProduct} and
 * {@link RouteDirection}.
 *
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 */
public class MainRunAdvisor {

    private MainRunStrategy mainRunRoundTripImportAdvice;
    private MainRunStrategy mainRunRoundTripExportAdvice;
    private MainRunStrategy mainRunOneWayImportAdvice;
    private MainRunStrategy mainRunOneWayExportAdvice;

    MainRunAdvisor(MainRunStrategy mainRunRoundTripImportAdvice, MainRunStrategy mainRunRoundTripExportAdvice,
        MainRunStrategy mainRunOneWayImportAdvice, MainRunStrategy mainRunOneWayExportAdvice) {

        this.mainRunRoundTripImportAdvice = mainRunRoundTripImportAdvice;
        this.mainRunRoundTripExportAdvice = mainRunRoundTripExportAdvice;
        this.mainRunOneWayImportAdvice = mainRunOneWayImportAdvice;
        this.mainRunOneWayExportAdvice = mainRunOneWayExportAdvice;
    }

    void setMainRunRoundTripImportAdvice(MainRunStrategy mainRunRoundTripImportAdvice) {

        this.mainRunRoundTripImportAdvice = mainRunRoundTripImportAdvice;
    }


    void setMainRunRoundTripExportAdvice(MainRunStrategy mainRunRoundTripExportAdvice) {

        this.mainRunRoundTripExportAdvice = mainRunRoundTripExportAdvice;
    }


    void setMainRunOneWayImportAdvice(MainRunStrategy mainRunOneWayImportAdvice) {

        this.mainRunOneWayImportAdvice = mainRunOneWayImportAdvice;
    }


    void setMainRunOneWayExportAdvice(MainRunStrategy mainRunOneWayExportAdvice) {

        this.mainRunOneWayExportAdvice = mainRunOneWayExportAdvice;
    }


    /**
     * Determines concrete MainRunStrategy strategy based on given routeProduct and routeDirection.
     *
     * @param  routeProduct
     * @param  routeDirection
     *
     * @return
     */
    public MainRunStrategy advice(RouteProduct routeProduct, RouteDirection routeDirection) {

        if (ROUNDTRIP.equals(routeProduct)) {
            if (IMPORT.equals(routeDirection)) {
                return mainRunRoundTripImportAdvice;
            } else {
                return mainRunRoundTripExportAdvice;
            }
        } else {
            if (IMPORT.equals(routeDirection)) {
                return mainRunOneWayImportAdvice;
            } else {
                return mainRunOneWayExportAdvice;
            }
        }
    }
}
