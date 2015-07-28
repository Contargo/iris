package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Region;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;


public class RouteDataRevisionDtoTest {

    private Long id;
    private Long terminalId;
    private BigDecimal truckDistanceOneWayInMeter;
    private BigDecimal tollDistanceOneWayInMeter;
    private BigDecimal airlineDistanceInMeter;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal radiusInMeter;
    private String comment;

    @Before
    public void setUp() throws Exception {

        id = 25L;
        terminalId = 23L;

        truckDistanceOneWayInMeter = new BigDecimal("1001");
        tollDistanceOneWayInMeter = new BigDecimal("1002");
        airlineDistanceInMeter = new BigDecimal("1003");
        latitude = new BigDecimal("1004");
        longitude = new BigDecimal("1005");
        radiusInMeter = new BigDecimal("1006");
        comment = "this is awesome";
    }


    @Test
    public void testInstantiation() {

        String uniqueId = "12345";
        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger(uniqueId));

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        routeDataRevision.setId(id);
        routeDataRevision.setTerminal(terminal);
        routeDataRevision.setTruckDistanceOneWayInMeter(truckDistanceOneWayInMeter);
        routeDataRevision.setTollDistanceOneWayInMeter(tollDistanceOneWayInMeter);
        routeDataRevision.setAirlineDistanceInMeter(airlineDistanceInMeter);
        routeDataRevision.setLatitude(latitude);
        routeDataRevision.setLongitude(longitude);
        routeDataRevision.setRadiusInMeter(radiusInMeter);
        routeDataRevision.setComment(comment);

        RouteDataRevisionDto result = new RouteDataRevisionDto(routeDataRevision);

        assertThat(result.getId(), is(id));
        assertThat(result.getTerminal().getUniqueId(), equalTo(uniqueId));
        assertThat(result.getTruckDistanceOneWayInMeter(), is(truckDistanceOneWayInMeter));
        assertThat(result.getTollDistanceOneWayInMeter(), is(tollDistanceOneWayInMeter));
        assertThat(result.getAirlineDistanceInMeter(), is(airlineDistanceInMeter));
        assertThat(result.getLatitude(), is(latitude));
        assertThat(result.getLongitude(), is(longitude));
        assertThat(result.getRadiusInMeter(), is(radiusInMeter));
        assertThat(result.getComment(), is(comment));
    }


    @Test
    public void testToEntity() throws Exception {

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
        routeDataRevisionDto.setComment(comment);

        RouteDataRevision result = routeDataRevisionDto.toEntity(terminalId);

        assertThat(result.getTerminal().getId(), is(terminalId));
        assertThat(result.getId(), is(id));
        assertThat(result.getTruckDistanceOneWayInMeter(), is(truckDistanceOneWayInMeter));
        assertThat(result.getTollDistanceOneWayInMeter(), is(tollDistanceOneWayInMeter));
        assertThat(result.getAirlineDistanceInMeter(), is(airlineDistanceInMeter));
        assertThat(result.getLatitude(), is(latitude));
        assertThat(result.getLongitude(), is(longitude));
        assertThat(result.getRadiusInMeter(), is(radiusInMeter));
        assertThat(result.getComment(), is(comment));
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
