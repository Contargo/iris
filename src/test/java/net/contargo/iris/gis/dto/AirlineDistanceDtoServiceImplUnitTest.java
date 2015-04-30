package net.contargo.iris.gis.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.gis.service.GisService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static net.contargo.iris.gis.dto.AirlineDistanceUnit.METER;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class AirlineDistanceDtoServiceImplUnitTest {

    @Mock
    private GisService gisService;
    private AirlineDistanceDtoServiceImpl sut;

    @Before
    public void before() {

        sut = new AirlineDistanceDtoServiceImpl(gisService);
    }


    @Test
    public void calcAirLineDistInMeters() {

        GeoLocation a = new GeoLocation(new BigDecimal("1.1"), new BigDecimal("1.2"));
        GeoLocation b = new GeoLocation(new BigDecimal("2.1"), new BigDecimal("2.2"));

        when(gisService.calcAirLineDistInMeters(a, b)).thenReturn(new BigDecimal("7.89"));

        AirlineDistanceDto airlineDistanceDto = sut.calcAirLineDistInMeters("1.1", "1.2", "2.1", "2.2");

        assertThat(airlineDistanceDto.getUnit(), is(METER));
        assertThat(airlineDistanceDto.getDistance(), is(new BigDecimal("7.89")));
        assertThat(airlineDistanceDto.getScope(), is("?alat=1.1&alon=1.2&blat=2.1&blon=2.2"));
    }
}
