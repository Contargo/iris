package net.contargo.iris.container;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class ContainerTypeUnitTest {

    @Test
    public void isUnknown() {

        assertThat(ContainerType.FORTY.isUnknown(), is(false));
        assertThat(ContainerType.NOT_SET.isUnknown(), is(true));
    }


    @Test
    public void getCsvString() {

        assertThat(ContainerType.FORTY.getCsvString(), is("forty"));
    }
}
