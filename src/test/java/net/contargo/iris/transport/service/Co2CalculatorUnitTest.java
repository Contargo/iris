package net.contargo.iris.transport.service;

import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportResponseDto;
import net.contargo.iris.transport.api.TransportSite;

import org.junit.Test;

import java.math.BigDecimal;

import static net.contargo.iris.container.ContainerState.EMPTY;
import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.terminal.Region.NIEDERRHEIN;
import static net.contargo.iris.terminal.Region.NOT_SET;
import static net.contargo.iris.terminal.Region.OBERRHEIN;
import static net.contargo.iris.terminal.Region.SCHELDE;
import static net.contargo.iris.transport.api.SiteType.ADDRESS;
import static net.contargo.iris.transport.api.SiteType.SEAPORT;
import static net.contargo.iris.transport.api.SiteType.TERMINAL;
import static net.contargo.iris.transport.service.Co2Calculator.handling;
import static net.contargo.iris.transport.service.Co2Calculator.rail;
import static net.contargo.iris.transport.service.Co2Calculator.truck;
import static net.contargo.iris.transport.service.Co2Calculator.water;
import static net.contargo.iris.transport.service.FlowDirection.DOWNSTREAM;
import static net.contargo.iris.transport.service.FlowDirection.UPSTREAM;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;


/**
 * @author  Ben Antony - antony@synyx.de
 */
public class Co2CalculatorUnitTest {

    private static final int DISTANCE = 100;

    @Test
    public void testTruck() {

        assertThat(truck(DISTANCE, FULL), comparesEqualTo(new BigDecimal("88.00")));
        assertThat(truck(DISTANCE, EMPTY), comparesEqualTo(new BigDecimal("73")));
    }


    @Test
    public void testRail() {

        assertThat(rail(DISTANCE, DISTANCE, FULL), comparesEqualTo(new BigDecimal("84.00")));
        assertThat(rail(DISTANCE, DISTANCE, EMPTY), comparesEqualTo(new BigDecimal("67.00")));
    }


    @Test
    public void testWater() {

        assertThat(water(DISTANCE, NIEDERRHEIN, FULL, UPSTREAM), comparesEqualTo(new BigDecimal("31.00")));
        assertThat(water(DISTANCE, NIEDERRHEIN, EMPTY, UPSTREAM), comparesEqualTo(new BigDecimal("27.00")));
        assertThat(water(DISTANCE, NIEDERRHEIN, FULL, DOWNSTREAM), comparesEqualTo(new BigDecimal("17.00")));
        assertThat(water(DISTANCE, NIEDERRHEIN, EMPTY, DOWNSTREAM), comparesEqualTo(new BigDecimal("14.00")));

        assertThat(water(DISTANCE, OBERRHEIN, FULL, UPSTREAM), comparesEqualTo(new BigDecimal("43.00")));
        assertThat(water(DISTANCE, OBERRHEIN, EMPTY, UPSTREAM), comparesEqualTo(new BigDecimal("40.00")));
        assertThat(water(DISTANCE, OBERRHEIN, FULL, DOWNSTREAM), comparesEqualTo(new BigDecimal("23.00")));
        assertThat(water(DISTANCE, OBERRHEIN, EMPTY, DOWNSTREAM), comparesEqualTo(new BigDecimal("21.00")));

        assertThat(water(DISTANCE, SCHELDE, FULL, UPSTREAM), comparesEqualTo(new BigDecimal("42.70")));
        assertThat(water(DISTANCE, SCHELDE, EMPTY, UPSTREAM), comparesEqualTo(new BigDecimal("37.50")));
        assertThat(water(DISTANCE, SCHELDE, FULL, DOWNSTREAM), comparesEqualTo(new BigDecimal("42.70")));
        assertThat(water(DISTANCE, SCHELDE, EMPTY, DOWNSTREAM), comparesEqualTo(new BigDecimal("37.50")));

        assertThat(water(DISTANCE, NOT_SET, FULL, UPSTREAM), comparesEqualTo(new BigDecimal("31.00")));
        assertThat(water(DISTANCE, NOT_SET, EMPTY, UPSTREAM), comparesEqualTo(new BigDecimal("27.00")));
        assertThat(water(DISTANCE, NOT_SET, FULL, DOWNSTREAM), comparesEqualTo(new BigDecimal("17.00")));
        assertThat(water(DISTANCE, NOT_SET, EMPTY, DOWNSTREAM), comparesEqualTo(new BigDecimal("14.00")));
    }


    @Test
    public void testHandling() {

        TransportSite terminal = new TransportSite(TERMINAL, "123456789", null, null);
        TransportSite seaport = new TransportSite(SEAPORT, "1234", null, null);
        TransportSite address = new TransportSite(ADDRESS, null, new BigDecimal("42.32432"),
                new BigDecimal("8.123412"));

        assertHandling(terminal, seaport, "4.00");
        assertHandling(seaport, terminal, "4.00");
        assertHandling(terminal, terminal, "8.00");
        assertHandling(terminal, address, "4.00");
        assertHandling(address, terminal, "4.00");
        assertHandling(seaport, address, "0.00");
        assertHandling(address, seaport, "0.00");
        assertHandling(address, address, "0.00");
        assertHandling(seaport, seaport, "0.00");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testRailNullLoadingState() {

        rail(DISTANCE, DISTANCE, null);
    }


    private void assertHandling(TransportSite fromSite, TransportSite toSite, String value) {

        TransportDescriptionDto.TransportDescriptionSegment descriptionSegment =
            new TransportDescriptionDto.TransportDescriptionSegment(fromSite, toSite, null, null, null);
        TransportResponseDto.TransportResponseSegment segment = new TransportResponseDto.TransportResponseSegment(
                descriptionSegment);
        assertThat(handling(segment), comparesEqualTo(new BigDecimal(value)));
    }
}
