package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.nominatim.service.NominatimAddressService;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressCoordinatesDuplicationException;
import net.contargo.iris.address.staticsearch.service.StaticAddressDuplicationException;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressErrorRecord;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressImportRecord;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticAddressResolverServiceImplUnitTest {

    private StaticAddressResolverServiceImpl sut;

    @Mock
    private NominatimAddressService nominatimAddressServiceMock;
    @Mock
    private StaticAddressService staticAddressServiceMock;
    @Captor
    private ArgumentCaptor<StaticAddress> staticAddressArgumentCaptor;

    private StaticAddressImportRecord karlsruhe;
    private Address nominatimAddress;

    @Before
    public void setUp() {

        sut = new StaticAddressResolverServiceImpl(staticAddressServiceMock, nominatimAddressServiceMock);

        karlsruhe = new StaticAddressImportRecord("76135", "Karlsruhe", "de");

        nominatimAddress = new Address(BigDecimal.ONE, BigDecimal.TEN);
        nominatimAddress.getAddress().put("city", "Karlsruhe-Resolved");
        nominatimAddress.getAddress().put("postcode", "76135-Resolved");
        nominatimAddress.getAddress().put("country_code", "de-Resolved");
    }


    @Test
    public void resolveAddressesWithoutErrors() {

        when(nominatimAddressServiceMock.getAddressesByDetails(karlsruhe.toAddressDetails())).thenReturn(singletonList(
                nominatimAddress));

        List<StaticAddressErrorRecord> errors = sut.resolveAddresses(singletonList(karlsruhe));

        assertThat(errors, empty());

        verify(staticAddressServiceMock).saveStaticAddress(staticAddressArgumentCaptor.capture());

        StaticAddress persistedStaticAddress = staticAddressArgumentCaptor.getValue();

        assertThat(persistedStaticAddress.getLatitude(), comparesEqualTo(BigDecimal.ONE));
        assertThat(persistedStaticAddress.getLongitude(), comparesEqualTo(BigDecimal.TEN));
        assertThat(persistedStaticAddress.getCity(), is("Karlsruhe"));
        assertThat(persistedStaticAddress.getPostalcode(), is("76135"));
        assertThat(persistedStaticAddress.getCountry(), is("DE"));
    }


    @Test
    public void resolveUnresolvableAddress() {

        when(nominatimAddressServiceMock.getAddressesByDetails(karlsruhe.toAddressDetails())).thenReturn(emptyList());

        List<StaticAddressErrorRecord> errors = sut.resolveAddresses(singletonList(karlsruhe));

        assertThat(errors, hasSize(1));

        assertThat(errors.get(0).getCity(), is("Karlsruhe"));
        assertThat(errors.get(0).getPostalCode(), is("76135"));
        assertThat(errors.get(0).getCountry(), is("DE"));
        assertThat(errors.get(0).getError(), is("unresolvable address"));

        verifyZeroInteractions(staticAddressServiceMock);
    }


    @Test
    public void resolveAddressWithKnownCityAndPostalcode() {

        when(nominatimAddressServiceMock.getAddressesByDetails(karlsruhe.toAddressDetails())).thenReturn(singletonList(
                nominatimAddress));
        doThrow(StaticAddressDuplicationException.class).when(staticAddressServiceMock)
            .saveStaticAddress(staticAddressArgumentCaptor.capture());

        List<StaticAddressErrorRecord> errors = sut.resolveAddresses(singletonList(karlsruhe));

        assertThat(errors, hasSize(1));

        assertThat(errors.get(0).getCity(), is("Karlsruhe"));
        assertThat(errors.get(0).getPostalCode(), is("76135"));
        assertThat(errors.get(0).getCountry(), is("DE"));
        assertThat(errors.get(0).getError(),
            is("address with same city and postalcode already exists (DE-76135 Karlsruhe)"));

        StaticAddress staticAddress = staticAddressArgumentCaptor.getValue();
        assertThat(staticAddress.getLatitude(), comparesEqualTo(BigDecimal.ONE));
        assertThat(staticAddress.getLongitude(), comparesEqualTo(BigDecimal.TEN));
        assertThat(staticAddress.getCity(), is("Karlsruhe"));
        assertThat(staticAddress.getPostalcode(), is("76135"));
        assertThat(staticAddress.getCountry(), is("DE"));
    }


    @Test
    public void resolveAddressWithKnownLocation() {

        when(nominatimAddressServiceMock.getAddressesByDetails(karlsruhe.toAddressDetails())).thenReturn(singletonList(
                nominatimAddress));
        doThrow(StaticAddressCoordinatesDuplicationException.class).when(staticAddressServiceMock)
            .saveStaticAddress(staticAddressArgumentCaptor.capture());

        List<StaticAddressErrorRecord> errors = sut.resolveAddresses(singletonList(karlsruhe));

        assertThat(errors, hasSize(1));

        assertThat(errors.get(0).getCity(), is("Karlsruhe"));
        assertThat(errors.get(0).getPostalCode(), is("76135"));
        assertThat(errors.get(0).getCountry(), is("DE"));
        assertThat(errors.get(0).getError(),
            is("address with same coordinates already exists (1.0000000000, 10.0000000000 [76135 Karlsruhe])"));

        StaticAddress staticAddress = staticAddressArgumentCaptor.getValue();
        assertThat(staticAddress.getLatitude(), comparesEqualTo(BigDecimal.ONE));
        assertThat(staticAddress.getLongitude(), comparesEqualTo(BigDecimal.TEN));
        assertThat(staticAddress.getCity(), is("Karlsruhe"));
        assertThat(staticAddress.getPostalcode(), is("76135"));
        assertThat(staticAddress.getCountry(), is("DE"));
    }


    @Test
    public void resolveAddressWithoutCityAndPostalcode() {

        nominatimAddress.getAddress().remove("city");
        nominatimAddress.getAddress().remove("postcode");
        nominatimAddress.getAddress().remove("country_code");

        when(nominatimAddressServiceMock.getAddressesByDetails(karlsruhe.toAddressDetails())).thenReturn(singletonList(
                nominatimAddress));

        List<StaticAddressErrorRecord> errors = sut.resolveAddresses(singletonList(karlsruhe));

        assertThat(errors, empty());

        verify(staticAddressServiceMock).saveStaticAddress(staticAddressArgumentCaptor.capture());

        StaticAddress persistedStaticAddress = staticAddressArgumentCaptor.getValue();

        assertThat(persistedStaticAddress.getLatitude(), comparesEqualTo(BigDecimal.ONE));
        assertThat(persistedStaticAddress.getLongitude(), comparesEqualTo(BigDecimal.TEN));
        assertThat(persistedStaticAddress.getCity(), is("Karlsruhe"));
        assertThat(persistedStaticAddress.getPostalcode(), is("76135"));
        assertThat(persistedStaticAddress.getCountry(), is("DE"));
    }
}
