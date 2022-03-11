package net.contargo.iris.transport;

import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.api.TransportStop;
import net.contargo.iris.units.MassUnit;
import net.contargo.iris.units.Weight;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.mockito.stubbing.Answer;

import java.math.BigDecimal;

import java.util.List;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.transport.api.ModeOfTransport.RAIL;
import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;
import static net.contargo.iris.transport.api.StopType.ADDRESS;
import static net.contargo.iris.transport.api.StopType.SEAPORT;
import static net.contargo.iris.transport.api.StopType.TERMINAL;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import static java.math.BigDecimal.ONE;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Bjoern Martin - martin@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TransportDescriptionExtenderUnitTest {

    private static final TransportStop TERMINAL_STOP = new TransportStop(TERMINAL, "42", null, null);
    private static final TransportStop SEAPORT_STOP = new TransportStop(SEAPORT, "62", null, null);
    private static final TransportStop ADDRESS_STOP = //
        new TransportStop(ADDRESS, null, new BigDecimal("42.34234"), new BigDecimal("8.0023"));

    @InjectMocks
    private TransportDescriptionExtender sut;

    @Mock
    private TransportDescriptionMainRunExtender mainRunExtenderMock;

    @Mock
    private TransportDescriptionRoadExtender roadExtenderMock;

    @Captor
    private ArgumentCaptor<TransportResponseDto.TransportResponseSegment> segmentCaptor;

    @Before
    public void setup() {

        doAnswer(setCo2(1)).when(mainRunExtenderMock)
            .with(any(TransportResponseDto.TransportResponseSegment.class));
        doAnswer(setCo2(2)).when(roadExtenderMock)
            .forNebenlauf(any(TransportResponseDto.TransportResponseSegment.class));
        doAnswer(setCo2(1)).when(roadExtenderMock)
            .forAddressesOnly(any(TransportResponseDto.TransportResponseSegment.class));
    }


    @Test
    public void withRoutingInformation_flagsResponseSegmentsAsOneway() {

        TransportDescriptionDto.TransportDescriptionSegment seaportToTerminal =
            new TransportDescriptionDto.TransportDescriptionSegment(SEAPORT_STOP, TERMINAL_STOP, FULL, true, RAIL);
        TransportDescriptionDto.TransportDescriptionSegment terminalToAddress =
            new TransportDescriptionDto.TransportDescriptionSegment(TERMINAL_STOP, ADDRESS_STOP, FULL, true, ROAD);
        TransportDescriptionDto.TransportDescriptionSegment addressToTerminal =
            new TransportDescriptionDto.TransportDescriptionSegment(ADDRESS_STOP, TERMINAL_STOP, EMPTY, true, ROAD);

        List<TransportDescriptionDto.TransportDescriptionSegment> descriptions = //
            asList(seaportToTerminal, terminalToAddress, addressToTerminal);

        TransportDescriptionDto description = new TransportDescriptionDto(descriptions);

        TransportResponseDto result = sut.withRoutingInformation(description);

        assertThat(result.transportChain.stream().noneMatch(s -> s.partOfRoundtrip), is(true));
    }


    @Test
    public void withRoutingInformation_flagsResponseSegmentsAsRoundtrip() {

        TransportDescriptionDto.TransportDescriptionSegment seaportToTerminal =
            new TransportDescriptionDto.TransportDescriptionSegment(SEAPORT_STOP, TERMINAL_STOP, FULL, true, RAIL);
        TransportDescriptionDto.TransportDescriptionSegment terminalToAddress =
            new TransportDescriptionDto.TransportDescriptionSegment(TERMINAL_STOP, ADDRESS_STOP, FULL, true, ROAD);
        TransportDescriptionDto.TransportDescriptionSegment addressToTerminal =
            new TransportDescriptionDto.TransportDescriptionSegment(ADDRESS_STOP, TERMINAL_STOP, EMPTY, true, ROAD);
        TransportDescriptionDto.TransportDescriptionSegment terminalToSeaport =
            new TransportDescriptionDto.TransportDescriptionSegment(TERMINAL_STOP, SEAPORT_STOP, EMPTY, true, RAIL);

        List<TransportDescriptionDto.TransportDescriptionSegment> descriptions = //
            asList(seaportToTerminal, terminalToAddress, addressToTerminal, terminalToSeaport);

        TransportDescriptionDto description = new TransportDescriptionDto(descriptions);

        TransportResponseDto result = sut.withRoutingInformation(description);

        assertThat(result.transportChain.stream().allMatch(s -> s.partOfRoundtrip), is(true));
    }


    @Test
    public void withRoutingInformation() {

        TransportDescriptionDto.TransportDescriptionSegment seaportTerminal =
            new TransportDescriptionDto.TransportDescriptionSegment(SEAPORT_STOP, TERMINAL_STOP, FULL, true, RAIL);
        TransportDescriptionDto.TransportDescriptionSegment terminalAddress =
            new TransportDescriptionDto.TransportDescriptionSegment(TERMINAL_STOP, ADDRESS_STOP, FULL, true, ROAD);
        TransportDescriptionDto.TransportDescriptionSegment AddressTerminal =
            new TransportDescriptionDto.TransportDescriptionSegment(ADDRESS_STOP, TERMINAL_STOP, EMPTY, true, ROAD);

        List<TransportDescriptionDto.TransportDescriptionSegment> descriptions = //
            asList(seaportTerminal, terminalAddress, AddressTerminal);

        TransportDescriptionDto description = new TransportDescriptionDto(descriptions);

        TransportResponseDto result = sut.withRoutingInformation(description);

        verify(mainRunExtenderMock).with(segmentCaptor.capture());
        verify(roadExtenderMock, times(2)).forNebenlauf(segmentCaptor.capture());

        assertThat(segmentCaptor.getAllValues().get(0), is(result.transportChain.get(0)));
        assertThat(segmentCaptor.getAllValues().get(1), is(result.transportChain.get(1)));
        assertThat(segmentCaptor.getAllValues().get(2), is(result.transportChain.get(2)));

        assertThat(result.transportChain.get(0).duration, nullValue());
        assertThat(result.transportChain.get(0).distance, nullValue());
        assertThat(result.transportChain.get(0).tollDistance, nullValue());
        assertThat(result.transportChain.get(0).co2.value, comparesEqualTo(new BigDecimal("3.927")));

        assertThat(result.transportChain.get(1).duration, nullValue());
        assertThat(result.transportChain.get(1).distance, nullValue());
        assertThat(result.transportChain.get(1).tollDistance, nullValue());
        assertThat(result.transportChain.get(1).co2.value, comparesEqualTo(new BigDecimal("4.927")));

        assertThat(result.transportChain.get(2).duration, nullValue());
        assertThat(result.transportChain.get(2).distance, nullValue());
        assertThat(result.transportChain.get(2).tollDistance, nullValue());
        assertThat(result.transportChain.get(2).co2.value, comparesEqualTo(new BigDecimal("4.927")));
    }


    @Test
    public void withOnlyAddresses() {

        TransportDescriptionDto.TransportDescriptionSegment segment =
            new TransportDescriptionDto.TransportDescriptionSegment(ADDRESS_STOP, ADDRESS_STOP, FULL, true, ROAD);

        List<TransportDescriptionDto.TransportDescriptionSegment> descriptions = singletonList(segment);

        TransportDescriptionDto description = new TransportDescriptionDto(descriptions);

        TransportResponseDto result = sut.withRoutingInformation(description);

        verifyZeroInteractions(mainRunExtenderMock);
        verify(roadExtenderMock).forAddressesOnly(segmentCaptor.capture());

        assertThat(segmentCaptor.getValue(), is(result.transportChain.get(0)));

        assertThat(result.transportChain.get(0).duration, nullValue());
        assertThat(result.transportChain.get(0).distance, nullValue());
        assertThat(result.transportChain.get(0).tollDistance, nullValue());
        assertThat(result.transportChain.get(0).co2.value, comparesEqualTo(ONE));
    }


    @Test
    public void withDTruck() {

        TransportDescriptionDto.TransportDescriptionSegment segment =
            new TransportDescriptionDto.TransportDescriptionSegment(ADDRESS_STOP, SEAPORT_STOP, FULL, true, ROAD);

        List<TransportDescriptionDto.TransportDescriptionSegment> descriptions = singletonList(segment);

        TransportDescriptionDto description = new TransportDescriptionDto(descriptions);

        doAnswer(setCo2(1)).when(mainRunExtenderMock)
            .with(any(TransportResponseDto.TransportResponseSegment.class));

        TransportResponseDto result = sut.withRoutingInformation(description);

        verifyZeroInteractions(roadExtenderMock);
        verify(mainRunExtenderMock).with(segmentCaptor.capture());

        assertThat(segmentCaptor.getValue(), is(result.transportChain.get(0)));

        assertThat(result.transportChain.get(0).duration, nullValue());
        assertThat(result.transportChain.get(0).distance, nullValue());
        assertThat(result.transportChain.get(0).tollDistance, nullValue());
        assertThat(result.transportChain.get(0).co2.value, comparesEqualTo(ONE));
    }


    private static Answer<?> setCo2(int co2) {

        return
            invocation -> {
            TransportResponseDto.TransportResponseSegment segment = //
                (TransportResponseDto.TransportResponseSegment) invocation.getArguments()[0];
            segment.co2 = new Weight(new BigDecimal(co2), MassUnit.KILOGRAM);

            return null;
        };
    }
}
