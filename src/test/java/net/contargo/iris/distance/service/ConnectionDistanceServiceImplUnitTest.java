package net.contargo.iris.distance.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.rounding.RoundingService;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class ConnectionDistanceServiceImplUnitTest {

    @InjectMocks
    private ConnectionDistanceServiceImpl sut;

    @Mock
    private MainRunConnection mainrunConnectionMock;
    @Mock
    private RoundingService roundingServiceMock;

    @Test
    public void getDistance() {

        when(mainrunConnectionMock.getTotalDistance()).thenReturn(TEN);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(ONE);

        assertThat(sut.getDistance(mainrunConnectionMock), is(ONE));
    }


    @Test
    public void getDieselDistance() {

        when(mainrunConnectionMock.getRailDieselDistance()).thenReturn(TEN);
        when(mainrunConnectionMock.getBargeDieselDistance()).thenReturn(TEN);
        when(roundingServiceMock.roundDistance(TEN.add(TEN))).thenReturn(ONE);

        assertThat(sut.getDieselDistance(mainrunConnectionMock), is(ONE));
    }


    @Test
    public void getRailDieselDistance() {

        when(mainrunConnectionMock.getRailDieselDistance()).thenReturn(TEN);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(ONE);

        assertThat(sut.getRailDieselDistance(mainrunConnectionMock), is(ONE));
    }


    @Test
    public void getBargeDieselDistance() {

        when(mainrunConnectionMock.getBargeDieselDistance()).thenReturn(TEN);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(ONE);

        assertThat(sut.getBargeDieselDistance(mainrunConnectionMock), is(ONE));
    }


    @Test
    public void getElectricDistance() {

        when(mainrunConnectionMock.getRailElectricDistance()).thenReturn(TEN);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(ONE);

        assertThat(sut.getElectricDistance(mainrunConnectionMock), is(ONE));
    }


    @Test
    public void getDtruckDistance() {

        BigDecimal roadDistance = new BigDecimal("466.2");
        when(mainrunConnectionMock.getRoadDistance()).thenReturn(roadDistance);
        when(roundingServiceMock.roundDistance(roadDistance)).thenReturn(new BigDecimal("467"));

        assertThat(sut.getDtruckDistance(mainrunConnectionMock), comparesEqualTo(new BigDecimal("467")));
    }
}
