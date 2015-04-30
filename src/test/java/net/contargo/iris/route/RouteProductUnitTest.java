package net.contargo.iris.route;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 */
public class RouteProductUnitTest {

    @Test
    public void createsRoundtripFromIsRoundTripTrue() {

        assertThat(RouteProduct.fromIsRoundtrip(true), is(RouteProduct.ROUNDTRIP));
    }


    @Test
    public void createsExportFromIsImportFalse() {

        assertThat(RouteProduct.fromIsRoundtrip(false), is(RouteProduct.ONEWAY));
    }
}
