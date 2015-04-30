package net.contargo.iris.route;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 */
public class RouteDirectionUnitTest {

    @Test
    public void createsImportFromIsImportTrue() {

        assertThat(RouteDirection.fromIsImport(true), is(RouteDirection.IMPORT));
    }


    @Test
    public void createsExportFromIsImportFalse() {

        assertThat(RouteDirection.fromIsImport(false), is(RouteDirection.EXPORT));
    }
}
