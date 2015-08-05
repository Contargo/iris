package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SeaportSubConnection;

import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SeaportSubConnectionDtoUnitTest {

    private static final Long ID = 42L;
    private static final String SEAPORT_UID = "42";
    private static final String TERMINAL_UID = "23";

    @Test
    public void toEntity() {

        MainRunConnection parent = new MainRunConnection();

        SeaportSubConnectionDto sut = new SeaportSubConnectionDto(ID, TERMINAL_UID, TEN, ZERO, ZERO, SEAPORT_UID);

        SeaportSubConnection entity = (SeaportSubConnection) sut.toEntity(parent);

        assertThat(entity.getId(), is(ID));
        assertThat(entity.getBargeDieselDistance(), is(TEN));
        assertThat(entity.getRailDieselDistance(), is(ZERO));
        assertThat(entity.getRailElectricDistance(), is(ZERO));
        assertThat(entity.getSeaport().getUniqueId(), is(new BigInteger(SEAPORT_UID)));
        assertThat(entity.getTerminal().getUniqueId(), is(new BigInteger(TERMINAL_UID)));
        assertThat(entity.getParentConnection(), is(parent));
    }
}
