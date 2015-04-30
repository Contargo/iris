package net.contargo.iris.location;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.service.AddressServiceWrapper;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Mockito.when;


/**
 * Unit test of {@link GeoLocationServiceImpl}.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class GeoLocationServiceImplUnitTest {

    private GeoLocationServiceImpl sut;

    @Mock
    private SeaportService seaportServiceMock;
    @Mock
    private TerminalService terminalServiceMock;
    @Mock
    private AddressServiceWrapper addressServiceWrapperMock;

    private GeoLocation geoLocation;

    @Before
    public void setUp() {

        geoLocation = new GeoLocation();
        geoLocation.setLatitude(BigDecimal.TEN);
        geoLocation.setLongitude(BigDecimal.TEN);

        sut = new GeoLocationServiceImpl(terminalServiceMock, seaportServiceMock, addressServiceWrapperMock);
    }


    @Test
    public void getDetailedGeoLocationSeaport() {

        Seaport seaport = new Seaport(geoLocation);
        when(seaportServiceMock.getByGeoLocation(geoLocation)).thenReturn(seaport);

        GeoLocation detailedGeoLocation = sut.getDetailedGeoLocation(geoLocation);

        assertThat(detailedGeoLocation, is((GeoLocation) seaport));
        assertThat("Expecting Seaport instance.", detailedGeoLocation instanceof Seaport, is(true));
    }


    @Test
    public void getDetailedGeoLocationTerminal() {

        Terminal terminal = new Terminal(geoLocation);
        when(terminalServiceMock.getByGeoLocation(geoLocation)).thenReturn(terminal);

        GeoLocation detailedGeoLocation = sut.getDetailedGeoLocation(geoLocation);

        assertThat(detailedGeoLocation, is((GeoLocation) terminal));
        assertThat("Expecting Terminal instance.", detailedGeoLocation instanceof Terminal, is(true));
    }


    @Test
    public void getDetailedGeoLocationNothingFound() {

        GeoLocation detailedGeoLocation = sut.getDetailedGeoLocation(geoLocation);

        assertThat(detailedGeoLocation, is(geoLocation));
    }


    @Test
    public void getDetailedGeoLocationAddress() {

        Address address = new Address();
        address.setShortName("Karlsruhe");

        when(addressServiceWrapperMock.getAddressForGeoLocation(geoLocation)).thenReturn(address);

        GeoLocation detailedGeoLocation = sut.getDetailedGeoLocation(geoLocation);

        assertThat("Expecting Address instance.", detailedGeoLocation instanceof Address, is(true));

        if (detailedGeoLocation instanceof Address) {
            assertThat("Expecting Address instance.", "Karlsruhe", is(((Address) detailedGeoLocation).getShortName()));
        }
    }
}
