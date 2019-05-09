package net.contargo.iris.transport;

import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportStop;
import net.contargo.iris.transport.api.TransportTemplateDto;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.List;

import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.StopType.ADDRESS;
import static net.contargo.iris.transport.api.StopType.TERMINAL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TransportChainGeneratorStrategyAdvisorUnitTest {

    private TransportChainGeneratorStrategyAdvisor sut;

    @Mock
    private TransportChainGenerator terminalGeneratorMock;
    @Mock
    private TransportChainGenerator addressGeneratorMock;

    @Before
    public void setUp() {

        sut = new TransportChainGeneratorStrategyAdvisor(terminalGeneratorMock, addressGeneratorMock);
    }


    @Test
    public void addressOnly() {

        TransportStop from = new TransportStop(ADDRESS, null, new BigDecimal("52"), new BigDecimal("8"));
        TransportStop to = new TransportStop(ADDRESS, null, new BigDecimal("52.9"), new BigDecimal("8.9"));
        TransportTemplateDto.TransportTemplateSegment segment = new TransportTemplateDto.TransportTemplateSegment(from,
                to, FULL, true, null);
        TransportTemplateDto template = new TransportTemplateDto(singletonList(segment));

        TransportDescriptionDto.TransportDescriptionSegment expectedSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, FULL, true, ROAD);
        TransportDescriptionDto expectedDescription = new TransportDescriptionDto(singletonList(expectedSegment));

        when(addressGeneratorMock.from(template)).thenReturn(singletonList(expectedDescription));

        List<TransportDescriptionDto> descriptions = sut.advice(template).get();

        assertThat(descriptions, hasSize(1));
        assertThat(descriptions.get(0), is(expectedDescription));

        verifyZeroInteractions(terminalGeneratorMock);
    }


    @Test
    public void withTerminal() {

        TransportStop from = new TransportStop(ADDRESS, null, new BigDecimal("52"), new BigDecimal("8"));
        TransportStop to = new TransportStop(TERMINAL, "12345", null, null);
        TransportTemplateDto.TransportTemplateSegment segment = new TransportTemplateDto.TransportTemplateSegment(from,
                to, FULL, true, null);
        TransportTemplateDto template = new TransportTemplateDto(singletonList(segment));

        TransportDescriptionDto.TransportDescriptionSegment expectedSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(from, to, FULL, true, ROAD);
        TransportDescriptionDto expectedDescription = new TransportDescriptionDto(singletonList(expectedSegment));

        when(terminalGeneratorMock.from(template)).thenReturn(singletonList(expectedDescription));

        List<TransportDescriptionDto> descriptions = sut.advice(template).get();

        assertThat(descriptions, hasSize(1));
        assertThat(descriptions.get(0), is(expectedDescription));

        verifyZeroInteractions(addressGeneratorMock);
    }
}
