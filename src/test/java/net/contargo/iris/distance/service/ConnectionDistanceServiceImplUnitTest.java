package net.contargo.iris.distance.service;

import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.TerminalSubConnection;
import net.contargo.iris.rounding.RoundingService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.argThat;

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

    private AbstractSubConnection subConnection;

    @Before
    public void setUp() {

        sut = new ConnectionDistanceServiceImpl(roundingServiceMock);

        subConnection = new TerminalSubConnection();
    }


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
    public void getBargeDieselDistanceForSubConnection() {

        subConnection.setBargeDieselDistance(TEN);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(ONE);
        assertThat(sut.getBargeDieselDistance(subConnection), is(ONE));
    }


    @Test
    public void getRailDieselDistanceForSubConnection() {

        subConnection.setRailDieselDistance(TEN);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(ONE);
        assertThat(sut.getRailDieselDistance(subConnection), is(ONE));
    }


    @Test
    public void getRailElectricDistanceForSubConnection() {

        subConnection.setRailElectricDistance(TEN);
        when(roundingServiceMock.roundDistance(TEN)).thenReturn(ONE);
        assertThat(sut.getRailElectricDistance(subConnection), is(ONE));
    }


    @Test
    public void getDieselDistanceForSubConnection() {

        subConnection.setBargeDieselDistance(TEN);
        subConnection.setRailDieselDistance(TEN);
        when(roundingServiceMock.roundDistance(argThat(comparesEqualTo(new BigDecimal(20))))).thenReturn(ONE);
        assertThat(sut.getDieselDistance(subConnection), is(ONE));
    }


    @Test
    public void getDistanceForSubConnection() {

        subConnection.setBargeDieselDistance(TEN);
        subConnection.setRailDieselDistance(TEN);
        subConnection.setRailElectricDistance(TEN);
        when(roundingServiceMock.roundDistance(argThat(comparesEqualTo(new BigDecimal(30))))).thenReturn(ONE);
        assertThat(sut.getDistance(subConnection), is(ONE));
    }
}
