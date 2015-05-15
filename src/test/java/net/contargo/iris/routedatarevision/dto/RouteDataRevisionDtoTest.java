package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Region;
import net.contargo.iris.terminal.dto.TerminalDto;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;


public class RouteDataRevisionDtoTest {

    @Test
    public void testToEntity() throws Exception {

        Long terminalId = 23L;
        Long id = 25L;
        BigDecimal truckDistanceOneWayInMeter = new BigDecimal("1001");
        BigDecimal tollDistanceOneWayInMeter = new BigDecimal("1002");
        BigDecimal airlineDistanceInMeter = new BigDecimal("1003");
        BigDecimal latitude = new BigDecimal("1004");
        BigDecimal longitude = new BigDecimal("1005");
        BigDecimal radiusInMeter = new BigDecimal("1006");

        TerminalDto terminalDto = createTerminalDto();

        RouteDataRevisionDto routeDataRevisionDto = new RouteDataRevisionDto();
        routeDataRevisionDto.setId(id);
        routeDataRevisionDto.setTerminal(terminalDto);
        routeDataRevisionDto.setTruckDistanceOneWayInMeter(truckDistanceOneWayInMeter);
        routeDataRevisionDto.setTollDistanceOneWayInMeter(tollDistanceOneWayInMeter);
        routeDataRevisionDto.setAirlineDistanceInMeter(airlineDistanceInMeter);
        routeDataRevisionDto.setLatitude(latitude);
        routeDataRevisionDto.setLongitude(longitude);
        routeDataRevisionDto.setRadiusInMeter(radiusInMeter);

        RouteDataRevision result = routeDataRevisionDto.toEntity(terminalId);

        assertThat(result.getTerminal().getId(), is(terminalId));
        assertThat(result.getId(), is(id));
        assertThat(result.getTruckDistanceOneWayInMeter(), is(truckDistanceOneWayInMeter));
        assertThat(result.getTollDistanceOneWayInMeter(), is(tollDistanceOneWayInMeter));
        assertThat(result.getAirlineDistanceInMeter(), is(airlineDistanceInMeter));
        assertThat(result.getLatitude(), is(latitude));
        assertThat(result.getLongitude(), is(longitude));
        assertThat(result.getRadiusInMeter(), is(radiusInMeter));
    }


    private TerminalDto createTerminalDto() {

        TerminalDto terminalDto = new TerminalDto();
        terminalDto.setEnabled(true);
        terminalDto.setName("terminalName");
        terminalDto.setUniqueId("1234567");
        terminalDto.setRegion(Region.NIEDERRHEIN);

        return terminalDto;
    }
}
