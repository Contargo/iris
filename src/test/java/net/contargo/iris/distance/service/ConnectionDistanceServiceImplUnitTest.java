package net.contargo.iris.distance.service;

import net.contargo.iris.connection.MainRunConnection;

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

    @Test
    public void getDistance() {

        when(mainrunConnectionMock.getTotalDistance()).thenReturn(TEN);

        assertThat(sut.getDistance(mainrunConnectionMock), is(TEN));
    }


    @Test
    public void getDieselDistance() {

        when(mainrunConnectionMock.getRailDieselDistance()).thenReturn(TEN);
        when(mainrunConnectionMock.getBargeDieselDistance()).thenReturn(TEN);

        assertThat(sut.getDieselDistance(mainrunConnectionMock), is(BigDecimal.valueOf(20)));
    }


    @Test
    public void getRailDieselDistance() {

        when(mainrunConnectionMock.getRailDieselDistance()).thenReturn(TEN);

        assertThat(sut.getRailDieselDistance(mainrunConnectionMock), is(TEN));
    }


    @Test
    public void getBargeDieselDistance() {

        when(mainrunConnectionMock.getBargeDieselDistance()).thenReturn(TEN);

        assertThat(sut.getBargeDieselDistance(mainrunConnectionMock), is(TEN));
    }


    @Test
    public void getElectricDistance() {

        when(mainrunConnectionMock.getRailElectricDistance()).thenReturn(TEN);

        assertThat(sut.getElectricDistance(mainrunConnectionMock), is(TEN));
    }


    @Test
    public void getDtruckDistance() {

        BigDecimal roadDistance = new BigDecimal("466.2");
        when(mainrunConnectionMock.getRoadDistance()).thenReturn(roadDistance);

        assertThat(sut.getDtruckDistance(mainrunConnectionMock), comparesEqualTo(new BigDecimal("467")));
    }
}
