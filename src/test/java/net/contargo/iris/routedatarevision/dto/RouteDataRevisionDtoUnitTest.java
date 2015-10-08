package net.contargo.iris.routedatarevision.dto;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Region;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;

import org.joda.time.DateTime;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;


/**
 * Unit test of {@link RouteDataRevisionDto}.
 */
public class RouteDataRevisionDtoUnitTest {

    private Long id;
    private Long terminalId;
    private BigDecimal truckDistanceOneWayInMeter;
    private BigDecimal tollDistanceOneWayInMeter;
    private BigDecimal airlineDistanceInMeter;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal radiusInMeter;
    private String comment;
    private Date validFrom;
    private Date validTo;

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
        validFrom = DateTime.now().toDate();
        validTo = DateTime.now().plusDays(1).toDate();
    }


    @Test
    public void instantiation() {

        String uniqueId = "12345";
        Terminal terminal = new Terminal();
        terminal.setUniqueId(new BigInteger(uniqueId));

        RouteDataRevision routeDataRevision = new RouteDataRevision();
        routeDataRevision.setId(id);
        routeDataRevision.setTerminal(terminal);
        routeDataRevision.setTruckDistanceOneWayInKilometer(truckDistanceOneWayInMeter);
        routeDataRevision.setTollDistanceOneWayInKilometer(tollDistanceOneWayInMeter);
        routeDataRevision.setAirlineDistanceInKilometer(airlineDistanceInMeter);
        routeDataRevision.setLatitude(latitude);
        routeDataRevision.setLongitude(longitude);
        routeDataRevision.setRadiusInMeter(radiusInMeter);
        routeDataRevision.setComment(comment);
        routeDataRevision.setValidFrom(validFrom);
        routeDataRevision.setValidTo(validTo);

        RouteDataRevisionDto result = new RouteDataRevisionDto(routeDataRevision);

        assertThat(result.getId(), is(id));
        assertThat(result.getTerminal().getUniqueId(), equalTo(uniqueId));
        assertThat(result.getTruckDistanceOneWayInKilometer(), is(truckDistanceOneWayInMeter));
        assertThat(result.getTollDistanceOneWayInKilometer(), is(tollDistanceOneWayInMeter));
        assertThat(result.getAirlineDistanceInKilometer(), is(airlineDistanceInMeter));
        assertThat(result.getLatitude(), is(latitude));
        assertThat(result.getLongitude(), is(longitude));
        assertThat(result.getRadiusInMeter(), is(radiusInMeter));
        assertThat(result.getComment(), is(comment));
        assertThat(result.getValidFrom(), is(validFrom));
        assertThat(result.getValidTo(), is(validTo));
    }


    @Test
    public void toEntity() throws Exception {

        TerminalDto terminalDto = createTerminalDto();

        RouteDataRevisionDto routeDataRevisionDto = new RouteDataRevisionDto();
        routeDataRevisionDto.setId(id);
        routeDataRevisionDto.setTerminal(terminalDto);
        routeDataRevisionDto.setTruckDistanceOneWayInKilometer(truckDistanceOneWayInMeter);
        routeDataRevisionDto.setTollDistanceOneWayInKilometer(tollDistanceOneWayInMeter);
        routeDataRevisionDto.setAirlineDistanceInKilometer(airlineDistanceInMeter);
        routeDataRevisionDto.setLatitude(latitude);
        routeDataRevisionDto.setLongitude(longitude);
        routeDataRevisionDto.setRadiusInMeter(radiusInMeter);
        routeDataRevisionDto.setComment(comment);
        routeDataRevisionDto.setValidFrom(validFrom);
        routeDataRevisionDto.setValidTo(validTo);

        RouteDataRevision result = routeDataRevisionDto.toEntity(terminalId);

        assertThat(result.getTerminal().getId(), is(terminalId));
        assertThat(result.getId(), is(id));
        assertThat(result.getTruckDistanceOneWayInKilometer(), is(truckDistanceOneWayInMeter));
        assertThat(result.getTollDistanceOneWayInKilometer(), is(tollDistanceOneWayInMeter));
        assertThat(result.getAirlineDistanceInKilometer(), is(airlineDistanceInMeter));
        assertThat(result.getLatitude(), is(latitude));
        assertThat(result.getLongitude(), is(longitude));
        assertThat(result.getRadiusInMeter(), is(radiusInMeter));
        assertThat(result.getComment(), is(comment));
        assertThat(result.getValidFrom(), is(validFrom));
        assertThat(result.getValidTo(), is(validTo));
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
