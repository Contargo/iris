package net.contargo.iris.connection.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.route.SubRoutePart;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SubRoutePartDtoUnitTest {

    private GeoLocation geo1 = new GeoLocation(ONE, ONE);
    private GeoLocation geo2 = new GeoLocation(TEN, TEN);
    private BigDecimal railDiesel = ONE;
    private BigDecimal railElectric = ZERO;
    private BigDecimal barge = TEN;
    private BigDecimal duration = ONE.add(TEN);
    private BigDecimal co2 = TEN.add(TEN);
    private BigDecimal distance = TEN.add(TEN).add(TEN);

    private SubRoutePart subRoutePart;
    private SubRoutePartDto subRoutePartDto;

    @Before
    public void setUp() {

        subRoutePart = new SubRoutePart();
        subRoutePart.setOrigin(geo1);
        subRoutePart.setDestination(geo2);
        subRoutePart.setRailDieselDistance(railDiesel);
        subRoutePart.setElectricDistance(railElectric);
        subRoutePart.setBargeDieselDistance(barge);
        subRoutePart.setRouteType(RouteType.BARGE);
        subRoutePart.setCo2(co2);
        subRoutePart.setDuration(duration);
        subRoutePart.setDistance(distance);
    }


    @Test
    public void createSubRoutePartDto() {

        SubRoutePartDto dto = new SubRoutePartDto(subRoutePart);
        assertThat(dto.getOrigin().getLatitude(), comparesEqualTo(ONE));
        assertThat(dto.getOrigin().getLongitude(), comparesEqualTo(ONE));
        assertThat(dto.getDestination().getLatitude(), comparesEqualTo(TEN));
        assertThat(dto.getDestination().getLongitude(), comparesEqualTo(TEN));
        assertThat(dto.getRailDieselDistance(), comparesEqualTo(railDiesel));
        assertThat(dto.getElectricDistance(), comparesEqualTo(railElectric));
        assertThat(dto.getBargeDieselDistance(), comparesEqualTo(barge));
        assertThat(dto.getRouteType(), is(RouteType.BARGE));
        assertThat(dto.getCo2(), comparesEqualTo(co2));
        assertThat(dto.getDuration(), comparesEqualTo(duration));
        assertThat(dto.getDistance(), comparesEqualTo(distance));
    }


    @Test
    public void toSubRoutePart() {

        SubRoutePart part = new SubRoutePartDto(this.subRoutePart).toSubRoutePart();
        assertThat(part.getOrigin().getLatitude(), comparesEqualTo(ONE));
        assertThat(part.getOrigin().getLongitude(), comparesEqualTo(ONE));
        assertThat(part.getDestination().getLatitude(), comparesEqualTo(TEN));
        assertThat(part.getDestination().getLongitude(), comparesEqualTo(TEN));
        assertThat(part.getRailDieselDistance(), comparesEqualTo(railDiesel));
        assertThat(part.getElectricDistance(), comparesEqualTo(railElectric));
        assertThat(part.getBargeDieselDistance(), comparesEqualTo(barge));
        assertThat(part.getRouteType(), is(RouteType.BARGE));
        assertThat(part.getCo2(), comparesEqualTo(co2));
        assertThat(part.getDuration(), comparesEqualTo(duration));
        assertThat(part.getDistance(), comparesEqualTo(distance));
    }
}
