package net.contargo.iris.gis.api;

import net.contargo.iris.gis.dto.AirlineDistanceDto;

import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.gis.dto.AirlineDistanceUnit.METER;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class AirlineDistanceResponseUnitTest {

    @Test
    public void create() {

        AirlineDistanceDto airlineDistanceDto = new AirlineDistanceDto(BigDecimal.ONE, METER, "");
        AirlineDistanceResponse sut = new AirlineDistanceResponse(airlineDistanceDto);
        assertThat(sut.getDistance(), is(BigDecimal.ONE));
        assertThat(sut.getUnit(), is("meter"));
    }
}
