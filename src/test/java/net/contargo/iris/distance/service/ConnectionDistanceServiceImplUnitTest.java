package net.contargo.iris.distance.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.rounding.RoundingService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class ConnectionDistanceServiceImplUnitTest {

    @Mock
    private MainRunConnection mainrunConnectionMock;
    @Mock
    private RoundingService roundingServiceMock;

    private ConnectionDistanceServiceImpl sut;

    @Before
    public void setUp() {

        sut = new ConnectionDistanceServiceImpl(roundingServiceMock);
    }


    @Test
    public void getDistance() {

        when(mainrunConnectionMock.getTotalDistance()).thenReturn(TEN);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(ONE);

        assertThat(sut.getDistance(mainrunConnectionMock), is(ONE));
    }


    @Test
    public void getDieselDistance() {

        when(mainrunConnectionMock.getDieselDistance()).thenReturn(TEN);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(ONE);

        assertThat(sut.getDieselDistance(mainrunConnectionMock), is(ONE));
    }


    @Test
    public void getElectricDistance() {

        when(mainrunConnectionMock.getElectricDistance()).thenReturn(TEN);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(ONE);

        assertThat(sut.getElectricDistance(mainrunConnectionMock), is(ONE));
    }
}
