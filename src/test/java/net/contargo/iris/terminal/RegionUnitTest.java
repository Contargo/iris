package net.contargo.iris.terminal;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;


/**
 * Unit test of {@link net.contargo.iris.terminal.Region}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
public class RegionUnitTest {

    @Test
    public void getMessageKeyNotSet() {

        assertThat(Region.NOT_SET.getMessageKey(), is("region.no"));
    }


    @Test
    public void getMessageKeyNiederrhein() {

        assertThat(Region.NIEDERRHEIN.getMessageKey(), is("region.niederrhein"));
    }


    @Test
    public void getMessageKeyOberrhein() {

        assertThat(Region.OBERRHEIN.getMessageKey(), is("region.oberrhein"));
    }


    @Test
    public void getMessageKeySchelde() {

        assertThat(Region.SCHELDE.getMessageKey(), is("region.schelde"));
    }
}
