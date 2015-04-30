package net.contargo.iris.address.staticsearch.web;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * Unit test for {@link StaticAddressRequest}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class StaticAddressRequestUnitTest {

    private StaticAddressRequest staticAddressRequest;

    @Before
    public void before() {

        staticAddressRequest = new StaticAddressRequest();
    }


    @Test
    public void getPostalcode() throws Exception {

        staticAddressRequest.setPostalcode("");
        assertThat(null, is(staticAddressRequest.getPostalcode()));

        staticAddressRequest.setPostalcode("76137");
        assertThat("76137", is(staticAddressRequest.getPostalcode()));

        staticAddressRequest.setPostalcode(null);
        assertThat(null, is(staticAddressRequest.getPostalcode()));
    }


    @Test
    public void getCity() throws Exception {

        staticAddressRequest.setCity("");
        assertThat(null, is(staticAddressRequest.getCity()));

        staticAddressRequest.setCity(null);
        assertThat(null, is(staticAddressRequest.getCity()));

        staticAddressRequest.setCity("Schonach im Schwarzwald");
        assertThat("Schonach im Schwarzwald", is(staticAddressRequest.getCity()));
    }


    @Test
    public void isEmpty() throws Exception {

        staticAddressRequest.setCity("");
        staticAddressRequest.setPostalcode("");
        assertThat(true, is(staticAddressRequest.isEmpty()));

        staticAddressRequest.setCity(null);
        staticAddressRequest.setPostalcode(null);
        assertThat(true, is(staticAddressRequest.isEmpty()));

        staticAddressRequest.setCity("Schonach im Schwarzwald");
        staticAddressRequest.setPostalcode("78136");
        assertThat(false, is(staticAddressRequest.isEmpty()));

        staticAddressRequest.setCity(null);
        staticAddressRequest.setPostalcode("78136");
        assertThat(false, is(staticAddressRequest.isEmpty()));

        staticAddressRequest.setCity("Schonach im Schwarzwald");
        staticAddressRequest.setPostalcode(null);
        assertThat(false, is(staticAddressRequest.isEmpty()));
    }
}
