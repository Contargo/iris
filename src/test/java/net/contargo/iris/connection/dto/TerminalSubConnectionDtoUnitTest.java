package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.TerminalSubConnection;

import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TerminalSubConnectionDtoUnitTest {

    private static final Long ID = 42L;
    private static final String TERMINAL1_UID = "23";
    private static final String TERMINAL2_UID = "42";

    @Test
    public void toEntity() {

        MainRunConnection parent = new MainRunConnection();

        TerminalSubConnectionDto sut = new TerminalSubConnectionDto(ID, TERMINAL1_UID, ZERO, TEN, ONE, TERMINAL2_UID);

        TerminalSubConnection entity = (TerminalSubConnection) sut.toEntity(parent);

        assertThat(entity.getId(), is(ID));
        assertThat(entity.getBargeDieselDistance(), is(ZERO));
        assertThat(entity.getRailDieselDistance(), is(TEN));
        assertThat(entity.getRailElectricDistance(), is(ONE));
        assertThat(entity.getTerminal().getUniqueId(), is(new BigInteger(TERMINAL1_UID)));
        assertThat(entity.getTerminal2().getUniqueId(), is(new BigInteger(TERMINAL2_UID)));
        assertThat(entity.getParentConnection(), is(parent));
    }
}
