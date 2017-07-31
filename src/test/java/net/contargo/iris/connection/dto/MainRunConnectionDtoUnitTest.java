package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SeaportSubConnection;
import net.contargo.iris.connection.TerminalSubConnection;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;

import static net.contargo.iris.route.RouteType.BARGE_RAIL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link MainRunConnectionDto}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class MainRunConnectionDtoUnitTest {

    private static final String SEAPORT_UID = "123";
    private static final String TERMINAL_UID = "456";
    private static final String HUB_TERMINAL_UID = "789";
    private static final Long CONNECTION_ID = 42L;
    private static final Long SUBCONNECTION1_ID = 23L;
    private static final Long SUBCONNECTION2_ID = 65L;

    @Test
    public void toEntity() {

        MainRunConnectionDto sut = createDto();

        MainRunConnection entity = sut.toEntity();

        assertThat(entity.getId(), is(CONNECTION_ID));
        assertThat(entity.getRailElectricDistance(), is(new BigDecimal("11")));
        assertThat(entity.getRailDieselDistance(), is(ONE));
        assertThat(entity.getBargeDieselDistance(), is(TEN));
        assertThat(entity.getRouteType(), is(BARGE_RAIL));
        assertThat(entity.getEnabled(), is(true));
        assertThat(entity.getSeaport().getUniqueId(), is(new BigInteger(SEAPORT_UID)));
        assertThat(entity.getTerminal().getUniqueId(), is(new BigInteger(TERMINAL_UID)));
        assertThat(entity.getSubConnections(), hasSize(2));
        assertThat(entity.getSubConnections().get(0), instanceOf(SeaportSubConnection.class));
        assertThat(entity.getSubConnections().get(1), instanceOf(TerminalSubConnection.class));

        SeaportSubConnection seaportSubConnection = (SeaportSubConnection) entity.getSubConnections().get(0);
        assertThat(seaportSubConnection.getId(), is(SUBCONNECTION1_ID));
        assertThat(seaportSubConnection.getRailElectricDistance(), is(ONE));
        assertThat(seaportSubConnection.getRailDieselDistance(), is(ZERO));
        assertThat(seaportSubConnection.getBargeDieselDistance(), is(TEN));
        assertThat(seaportSubConnection.getSeaport().getUniqueId(), is(new BigInteger(SEAPORT_UID)));
        assertThat(seaportSubConnection.getTerminal().getUniqueId(), is(new BigInteger(HUB_TERMINAL_UID)));

        TerminalSubConnection terminalSubConnection = (TerminalSubConnection) entity.getSubConnections().get(1);
        assertThat(terminalSubConnection.getId(), is(SUBCONNECTION2_ID));
        assertThat(terminalSubConnection.getRailElectricDistance(), is(TEN));
        assertThat(terminalSubConnection.getRailDieselDistance(), is(ONE));
        assertThat(terminalSubConnection.getBargeDieselDistance(), is(ZERO));
        assertThat(terminalSubConnection.getTerminal().getUniqueId(), is(new BigInteger(HUB_TERMINAL_UID)));
        assertThat(terminalSubConnection.getTerminal2().getUniqueId(), is(new BigInteger(TERMINAL_UID)));
    }


    private MainRunConnectionDto createDto() {

        AbstractSubConnectionDto subConnectionDto1 = new SeaportSubConnectionDto(SUBCONNECTION1_ID, HUB_TERMINAL_UID,
                TEN, ZERO, ONE, SEAPORT_UID);

        AbstractSubConnectionDto subConnectionDto2 = new TerminalSubConnectionDto(SUBCONNECTION2_ID, HUB_TERMINAL_UID,
                ZERO, ONE, TEN, TERMINAL_UID);

        List<AbstractSubConnectionDto> subConnections = asList(subConnectionDto1, subConnectionDto2);

        return new MainRunConnectionDto(CONNECTION_ID, SEAPORT_UID, TERMINAL_UID, ZERO, ZERO, ZERO, ONE, BARGE_RAIL,
                true, subConnections);
    }
}
