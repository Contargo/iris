package net.contargo.iris.transport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportStop;
import net.contargo.iris.transport.api.TransportTemplateDto;

import org.junit.Test;

import java.math.BigInteger;

import java.util.List;

import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.transport.api.StopType.ADDRESS;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class AddressOnlyTransportChainGeneratorUnitTest {

    private AddressOnlyTransportChainGenerator sut = new AddressOnlyTransportChainGenerator();

    @Test
    public void addressOnly() {

        Terminal woerth = new Terminal(new GeoLocation(TEN, TEN));
        woerth.setUniqueId(new BigInteger("1234565789"));

        Terminal malu = new Terminal(new GeoLocation(TEN, ZERO));
        malu.setUniqueId(new BigInteger("987654321"));

        TransportTemplateDto.TransportTemplateSegment addressToAddress =
            new TransportTemplateDto.TransportTemplateSegment(new TransportStop(ADDRESS, null, TEN, ONE),
                new TransportStop(ADDRESS, null, ONE, TEN), FULL, true, null);

        TransportTemplateDto template = new TransportTemplateDto(singletonList(addressToAddress));

        List<TransportDescriptionDto> descriptions = sut.from(template);

        assertThat(descriptions, hasSize(1));
    }
}
