package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.MainRunConnection;

import org.junit.Test;

import java.math.BigInteger;

import static net.contargo.iris.route.RouteType.BARGE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;


/**
 * Unit test for {@link MainRunConnectionDto}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class MainRunConnectionDtoUnitTest {

    private static final String SEAPORT_UID = "123";
    private static final String TERMINAL_UID = "456";
    private static final Long CONNECTION_ID = 42L;

    @Test
    public void toEntity() {

        MainRunConnectionDto sut = createDto();

        MainRunConnection entity = sut.toEntity();

        assertThat(entity.getId(), is(CONNECTION_ID));
        assertThat(entity.getRailElectricDistance(), is(ZERO));
        assertThat(entity.getRailDieselDistance(), is(ZERO));
        assertThat(entity.getBargeDieselDistance(), is(ZERO));
        assertThat(entity.getRouteType(), is(BARGE));
        assertThat(entity.getEnabled(), is(true));
        assertThat(entity.getSeaport().getUniqueId(), is(new BigInteger(SEAPORT_UID)));
        assertThat(entity.getTerminal().getUniqueId(), is(new BigInteger(TERMINAL_UID)));
    }


    private MainRunConnectionDto createDto() {

        return new MainRunConnectionDto(CONNECTION_ID, SEAPORT_UID, TERMINAL_UID, ZERO, ZERO, ZERO, ONE, BARGE, true);
    }
}
