package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.BoundingBox;
import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.persistence.StaticAddressRepository;
import net.contargo.iris.normalizer.NormalizerServiceImpl;
import net.contargo.iris.sequence.service.UniqueIdSequenceServiceImpl;

import org.hamcrest.CoreMatchers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import static org.junit.Assert.assertThat;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.math.BigInteger.ONE;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


/**
 * Unit test for {@link StaticAddressServiceImpl}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticAddressServiceImplUnitTest {

    private static final long STATIC_ADDRESS_ID = 66L;
    private static final String POSTAL_CODE_76137 = "76137";
    private static final String POSTALCODE_76189 = "76189";
    private static final String COUNTRY_DE = "DE";
    private static final String CITY_KARLSRUHE = "Karlsruhe";
    private static final String CITY_BERLIN = "Berlin";
    private static final String CITY_OBERREUT = "Oberreut";
    private static final String CITY_SCHOENEFELD = "Schoenefeld";
    private static final String CITY_NEUSTADT_AN_DER_WEINSTRASSE = "Neustadt an der Weinstrasse";
    private static final String CITY_KARLSRUHE_NORMALIZED = "KARLSRUHE";
    private static final String CITY_BERLIN_NORMALIZED = "BERLIN";
    private static final String CITY_SCHOENEFELD_NORMALIZED = "SCHOENEFELD";
    private static final String CITY_OBERREUT_NORMALIZED = "CITY_OBERREUT";
    private static final String CITY_NEUSTADT_NORMALIZED = "NEUSTADTANDERWEINSTRASSE";
    private static final BigInteger UNIQUE_ID = new BigInteger("130100000000008");

    private StaticAddressServiceImpl sut;

    @Mock
    private StaticAddressRepository staticAddressRepositoryMock;
    @Mock
    private UniqueIdSequenceServiceImpl uniqueIdSequenceServiceMock;
    @Mock
    private NormalizerServiceImpl normalizerServiceMock;

    private StaticAddress kaSuedstadt;
    private StaticAddress kaWeststadt;
    private StaticAddress kaNordstadt;
    private StaticAddress berlinSchoenefeld;
    private StaticAddress berlinSchoenefeldWithoutPostalcode;

    @Before
    public void setup() {

        sut = new StaticAddressServiceImpl(staticAddressRepositoryMock, uniqueIdSequenceServiceMock,
                normalizerServiceMock);

        when(normalizerServiceMock.normalize(CITY_KARLSRUHE)).thenReturn(CITY_KARLSRUHE_NORMALIZED);
        when(normalizerServiceMock.normalize(CITY_BERLIN)).thenReturn(CITY_BERLIN_NORMALIZED);
        when(normalizerServiceMock.normalize(CITY_OBERREUT)).thenReturn(CITY_OBERREUT_NORMALIZED);
        when(normalizerServiceMock.normalize(CITY_SCHOENEFELD)).thenReturn(CITY_SCHOENEFELD_NORMALIZED);
        when(normalizerServiceMock.normalize(CITY_NEUSTADT_AN_DER_WEINSTRASSE)).thenReturn(CITY_NEUSTADT_NORMALIZED);

        initaddresses();
    }


    @Test
    public void findByPostalCodeAndCity() {

        String postalCode = "78137";
        String city = CITY_KARLSRUHE;
        String normalizedCity = CITY_KARLSRUHE_NORMALIZED;

        when(staticAddressRepositoryMock.findByPostalCodeAndCity(postalCode, normalizedCity + "%")).thenReturn(
            singletonList(kaSuedstadt));

        AddressList addresses = sut.findAddresses(postalCode, city, "");
        assertThat(addresses.getAddresses().size(), is(1));
        verify(staticAddressRepositoryMock).findByPostalCodeAndCity(postalCode, normalizedCity + "%");
    }


    @Test
    public void findByPostalcodeOnly() {

        String postalCode = "78137";

        when(staticAddressRepositoryMock.findByPostalcode(postalCode)).thenReturn(singletonList(kaSuedstadt));

        AddressList addresses = sut.findAddresses(postalCode, "", "");
        assertThat(addresses.getAddresses().size(), is(1));
        verify(staticAddressRepositoryMock, times(2)).findByPostalcode(postalCode);
    }


    @Test
    public void findByPostalcodeAndCountry() {

        String postalCode = "78137";
        String country = "ch";

        when(staticAddressRepositoryMock.findByPostalcodeAndCountry(postalCode, country)).thenReturn(singletonList(
                kaSuedstadt));

        List<StaticAddress> result = sut.findByPostalcodeAndCountry(postalCode, country);
        assertThat(result.size(), is(1));
        assertThat(result.get(0), is(kaSuedstadt));
    }


    @Test
    public void findByPostalcodeOnlyWithNullValues() {

        String postalCode = "12345";

        when(staticAddressRepositoryMock.findByPostalcode(postalCode)).thenReturn(singletonList(kaSuedstadt));

        AddressList addresses = sut.findAddresses(postalCode, null, null);
        assertThat(addresses.getAddresses().size(), is(1));
        verify(staticAddressRepositoryMock, times(2)).findByPostalcode(postalCode);
    }


    @Test
    public void testPostalCodeOrCountry() {

        String postalCode = "78137";
        String city = CITY_KARLSRUHE;
        String normalizedCity = CITY_KARLSRUHE_NORMALIZED;
        String country = COUNTRY_DE;

        when(staticAddressRepositoryMock.findByCountryAndPostalCodeAndCity(postalCode, normalizedCity + "%", country))
            .thenReturn(new ArrayList<>());

        when(staticAddressRepositoryMock.findByCountryAndPostalCodeOrCity(postalCode, normalizedCity + "%", country))
            .thenReturn(asList(kaSuedstadt, kaWeststadt, kaNordstadt));

        AddressList addresses = sut.findAddresses(postalCode, city, country);
        assertThat(addresses.getAddresses().size(), is(3));
        verify(staticAddressRepositoryMock).findByCountryAndPostalCodeAndCity(postalCode, normalizedCity + "%",
            country);
        verify(staticAddressRepositoryMock).findByCountryAndPostalCodeOrCity(postalCode, normalizedCity + "%", country);
    }


    @Test
    public void findByCity() {

        String postalCode = "";
        String city = CITY_KARLSRUHE;
        String normalizedCity = CITY_KARLSRUHE_NORMALIZED;
        String country = "";

        when(staticAddressRepositoryMock.findByPostalCodeAndCity(postalCode, normalizedCity + "%")).thenReturn(
            new ArrayList<>());
        when(staticAddressRepositoryMock.findByPostalCodeOrCity(postalCode, normalizedCity + "%")).thenReturn(asList(
                kaSuedstadt, kaWeststadt, kaNordstadt));

        AddressList result = sut.findAddresses(postalCode, city, country);

        assertThat(result.getAddresses().size(), is(3));
        verify(staticAddressRepositoryMock).findByPostalCodeAndCity(postalCode, normalizedCity + "%");
        verify(staticAddressRepositoryMock).findByPostalCodeOrCity(postalCode, normalizedCity + "%");
    }


    @Test
    public void findById() {

        StaticAddress expectedAddress = new StaticAddress();
        expectedAddress.setCountry("Unique Attribute for distinction");
        when(staticAddressRepositoryMock.findOne(STATIC_ADDRESS_ID)).thenReturn(expectedAddress);

        Assert.assertEquals(expectedAddress, sut.findById(STATIC_ADDRESS_ID));
        verify(staticAddressRepositoryMock).findOne(STATIC_ADDRESS_ID);
    }


    @Test
    public void findByUId() {

        StaticAddress expectedAddress = new StaticAddress();
        when(staticAddressRepositoryMock.findByUniqueId(ONE)).thenReturn(expectedAddress);

        StaticAddress staticAddress = sut.findByUId(ONE);
        assertThat(staticAddress, is(expectedAddress));
    }


    @Test(expected = StaticAddressNotFoundException.class)
    public void findByUIdException() {

        when(staticAddressRepositoryMock.findByUniqueId(ONE)).thenReturn(null);
        sut.findByUId(ONE);
    }


    @Test
    public void findByPostalCodeAndCityAndCountry() {

        String postalCode = POSTAL_CODE_76137;
        String city = CITY_KARLSRUHE;
        String normalizedCity = CITY_KARLSRUHE_NORMALIZED;
        String country = COUNTRY_DE;

        when(staticAddressRepositoryMock.findByCountryAndPostalCodeAndCity(postalCode, normalizedCity + "%", country))
            .thenReturn(singletonList(kaSuedstadt));

        AddressList addresses = sut.findAddresses(postalCode, city, country);
        assertThat(addresses.getAddresses().size(), is(1));
        verify(staticAddressRepositoryMock).findByCountryAndPostalCodeAndCity(postalCode, normalizedCity + "%",
            country);
    }


    @Test
    public void getAddressesByDetails() {

        String postalCode = POSTAL_CODE_76137;
        String city = CITY_KARLSRUHE;
        String normalizedCity = CITY_KARLSRUHE_NORMALIZED;
        String country = COUNTRY_DE;

        when(staticAddressRepositoryMock.findByCountryAndPostalCodeAndCity(postalCode, normalizedCity, country))
            .thenReturn(singletonList(kaSuedstadt));

        List<StaticAddress> addressesByDetails = sut.getAddressesByDetails(postalCode, city, country);
        assertThat(addressesByDetails.size(), is(1));
    }


    @Test
    public void findLatLong() {

        BigDecimal lat = new BigDecimal("49.123");
        BigDecimal lon = new BigDecimal("8.123");
        GeoLocation loc = new GeoLocation(lat, lon);

        when(staticAddressRepositoryMock.findByLatitudeAndLongitude(loc.getLatitude(), loc.getLongitude())).thenReturn(
            kaSuedstadt);

        StaticAddress staticAddress = sut.getForLocation(loc);
        assertThat(staticAddress, CoreMatchers.notNullValue());
        assertThat(staticAddress.toAddress().getDisplayName(), equalTo(kaSuedstadt.toAddress().getDisplayName()));
        verify(staticAddressRepositoryMock).findByLatitudeAndLongitude(loc.getLatitude(), loc.getLongitude());
    }


    @Test
    public void fallbackFindByPostalCodeAndCity() {

        String postalCode = "76131";
        String city = CITY_KARLSRUHE;
        String normalizedCity = CITY_KARLSRUHE_NORMALIZED;
        String country = "";

        when(staticAddressRepositoryMock.findByPostalCodeAndCity(postalCode, normalizedCity + "%")).thenReturn(
            new ArrayList<>());
        when(staticAddressRepositoryMock.findByPostalCodeOrCity(postalCode, normalizedCity + "%")).thenReturn(asList(
                kaSuedstadt, kaWeststadt, kaNordstadt));

        AddressList addressList = sut.findAddresses(postalCode, city, country);
        assertThat(addressList.getAddresses().size(), is(3));
        verify(staticAddressRepositoryMock).findByPostalCodeAndCity(postalCode, normalizedCity + "%");
        verify(staticAddressRepositoryMock).findByPostalCodeOrCity(postalCode, normalizedCity + "%");
    }


    @Test
    public void fallbackSplittingSearchParameter() {

        String postalCode = "";
        String country = "";

        when(staticAddressRepositoryMock.findByPostalCodeAndCity(postalCode, CITY_NEUSTADT_NORMALIZED + "%"))
            .thenReturn(new ArrayList<>());
        when(staticAddressRepositoryMock.findByPostalCodeOrCity(postalCode, CITY_NEUSTADT_NORMALIZED + "%")).thenReturn(
            new ArrayList<>());

        sut.findAddresses(postalCode, CITY_NEUSTADT_AN_DER_WEINSTRASSE, country);
        verify(staticAddressRepositoryMock).findByPostalCodeAndCity("", CITY_NEUSTADT_NORMALIZED + "%");
        verify(staticAddressRepositoryMock).findByPostalCodeOrCity("", CITY_NEUSTADT_NORMALIZED + "%");
    }


    @Test
    public void addressTransformation() {

        String postalCode = POSTAL_CODE_76137;
        String city = CITY_KARLSRUHE;
        String normalizedCity = CITY_KARLSRUHE_NORMALIZED;
        String country = COUNTRY_DE;

        when(staticAddressRepositoryMock.findByCountryAndPostalCodeAndCity(postalCode, normalizedCity + "%", country))
            .thenReturn(singletonList(kaSuedstadt));

        AddressList addresses = sut.findAddresses(postalCode, city, country);
        assertThat(addresses.getAddresses().size(), is(1));

        Address address = addresses.getAddresses().get(0);
        assertThat(address.getDisplayName(), is("76137 Karlsruhe (Suedstadt)"));
        assertThat(address.getAddress().get(Address.COUNTRY_CODE), is(COUNTRY_DE));
        assertThat(address.getLatitude(), notNullValue());
        assertThat(address.getLongitude(), notNullValue());
        verify(staticAddressRepositoryMock).findByCountryAndPostalCodeAndCity(postalCode, normalizedCity + "%",
            country);
    }


    @Test
    public void saveStaticAddressWithValidAddress() {

        berlinSchoenefeld.setUniqueId(UNIQUE_ID);
        when(staticAddressRepositoryMock.save(berlinSchoenefeld)).thenReturn(berlinSchoenefeld);

        StaticAddress staticAddress = sut.saveStaticAddress(berlinSchoenefeld);
        assertThat(staticAddress, is(berlinSchoenefeld));
        verify(staticAddressRepositoryMock).save(berlinSchoenefeld);
    }


    @Test
    public void saveNewStaticAddressWithoutUniqueId() {

        berlinSchoenefeld.setId(null);
        berlinSchoenefeld.setUniqueId(null);

        BigInteger id = new BigInteger("1301000000000005");

        when(staticAddressRepositoryMock.save(berlinSchoenefeld)).thenReturn(berlinSchoenefeld);
        when(uniqueIdSequenceServiceMock.getNextId("StaticAddress")).thenReturn(id);
        when(staticAddressRepositoryMock.findByUniqueId(id)).thenReturn(null);

        ArgumentCaptor<StaticAddress> captor = ArgumentCaptor.forClass(StaticAddress.class);

        StaticAddress savedBerlinSchoenefeld = sut.saveStaticAddress(berlinSchoenefeld);
        assertThat(savedBerlinSchoenefeld.getUniqueId(), notNullValue());
        verify(staticAddressRepositoryMock).save(captor.capture());
        assertThat(captor.getValue().getUniqueId(), is(savedBerlinSchoenefeld.getUniqueId()));
    }


    @Test
    public void determineUniqueId() {

        when(uniqueIdSequenceServiceMock.getNextId("StaticAddress")).thenReturn(ONE);
        when(staticAddressRepositoryMock.findByUniqueId(ONE)).thenReturn(null);

        BigInteger uniqueId = sut.determineUniqueId();
        assertThat(uniqueId, is(ONE));
        verify(uniqueIdSequenceServiceMock, times(1)).getNextId("StaticAddress");
    }


    @Test
    public void determineUniqueIdWithSecondRetry() {

        when(uniqueIdSequenceServiceMock.getNextId("StaticAddress")).thenReturn(ONE);
        when(staticAddressRepositoryMock.findByUniqueId(ONE)).thenReturn(new StaticAddress());

        assertThat(sut.determineUniqueId(), is(new BigInteger("2")));

        InOrder order = inOrder(uniqueIdSequenceServiceMock);
        order.verify(uniqueIdSequenceServiceMock, times(1)).getNextId("StaticAddress");
        order.verify(uniqueIdSequenceServiceMock, times(1)).setNextId("StaticAddress", new BigInteger("2"));
    }


    @Test
    public void saveStaticAddressWithNullValues() {

        StaticAddress berlinSchoenefeldWithNull = mock(StaticAddress.class);

        when(berlinSchoenefeldWithNull.getId()).thenReturn(null);
        when(berlinSchoenefeldWithNull.getCity()).thenReturn(null);
        when(berlinSchoenefeldWithNull.getSuburb()).thenReturn(null);
        when(berlinSchoenefeldWithNull.getPostalcode()).thenReturn(null);

        when(staticAddressRepositoryMock.save(berlinSchoenefeldWithNull)).thenReturn(berlinSchoenefeld);

        sut.saveStaticAddress(berlinSchoenefeldWithNull);

        verify(berlinSchoenefeldWithNull, times(1)).setSuburb("");
        verify(berlinSchoenefeldWithNull, times(1)).setCity("");
        verify(berlinSchoenefeldWithNull, times(1)).setPostalcode("");
        verify(staticAddressRepositoryMock).save(berlinSchoenefeldWithNull);
    }


    @Test
    public void normalizingProcess() {

        StaticAddress berlinSchoenefeldWithNull = mock(StaticAddress.class);

        when(berlinSchoenefeldWithNull.getId()).thenReturn(null);
        when(berlinSchoenefeldWithNull.getCity()).thenReturn(CITY_KARLSRUHE);
        when(berlinSchoenefeldWithNull.getSuburb()).thenReturn(CITY_OBERREUT);
        when(berlinSchoenefeldWithNull.getPostalcode()).thenReturn(POSTALCODE_76189);

        when(staticAddressRepositoryMock.save(berlinSchoenefeldWithNull)).thenReturn(berlinSchoenefeld);

        sut.saveStaticAddress(berlinSchoenefeldWithNull);

        verify(berlinSchoenefeldWithNull, times(1)).setSuburbNormalized(CITY_OBERREUT_NORMALIZED);
        verify(berlinSchoenefeldWithNull, times(1)).setCityNormalized(CITY_KARLSRUHE_NORMALIZED);
        verify(berlinSchoenefeldWithNull, never()).setPostalcode(anyString());
        verify(staticAddressRepositoryMock).save(berlinSchoenefeldWithNull);
    }


    @Test(expected = StaticAddressDuplicationException.class)
    public void saveStaticAddressWithDuplicates() {

        List<StaticAddress> staticAddresses = new ArrayList<>();
        staticAddresses.add(berlinSchoenefeld);

        berlinSchoenefeld.setCityNormalized(CITY_BERLIN);
        berlinSchoenefeld.setSuburbNormalized(CITY_SCHOENEFELD);

        when(staticAddressRepositoryMock.findByCityAndSuburbAndPostalcode(berlinSchoenefeld.getCity(),
                    berlinSchoenefeld.getSuburb(), berlinSchoenefeld.getPostalcode())).thenReturn(staticAddresses);

        sut.saveStaticAddress(berlinSchoenefeld);
    }


    @Test(expected = StaticAddressCoordinatesDuplicationException.class)
    public void saveStaticAddressWithDuplicateCoordinates() {

        berlinSchoenefeld.setCityNormalized(CITY_BERLIN_NORMALIZED);
        berlinSchoenefeld.setSuburbNormalized(CITY_SCHOENEFELD_NORMALIZED);

        when(staticAddressRepositoryMock.findByLatitudeAndLongitude(any(BigDecimal.class), any(BigDecimal.class)))
            .thenReturn(new StaticAddress());

        sut.saveStaticAddress(berlinSchoenefeld);
    }


    @Test
    public void updaaticAddress() {

        berlinSchoenefeld.setId(1L);

        when(staticAddressRepositoryMock.findOne(berlinSchoenefeld.getId())).thenReturn(berlinSchoenefeld);
        when(staticAddressRepositoryMock.save(berlinSchoenefeld)).thenReturn(berlinSchoenefeld);
        assertThat(sut.saveStaticAddress(berlinSchoenefeld), is(berlinSchoenefeld));
    }


    @Test
    public void updaaticAddressWithChangedGeocoordinates() {

        berlinSchoenefeld.setId(1L);

        StaticAddress berlinSchoenefeldParameterChanged = new StaticAddress();
        berlinSchoenefeldParameterChanged.setId(1L);
        berlinSchoenefeldParameterChanged.setCity(berlinSchoenefeld.getCity());
        berlinSchoenefeldParameterChanged.setCityNormalized(berlinSchoenefeld.getCityNormalized());
        berlinSchoenefeldParameterChanged.setSuburb(berlinSchoenefeld.getSuburb());
        berlinSchoenefeldParameterChanged.setSuburbNormalized(berlinSchoenefeld.getSuburbNormalized());
        berlinSchoenefeldParameterChanged.setPostalcode(berlinSchoenefeld.getPostalcode());
        berlinSchoenefeldParameterChanged.setLatitude(BigDecimal.ONE);
        berlinSchoenefeldParameterChanged.setLongitude(BigDecimal.TEN);

        when(staticAddressRepositoryMock.findOne(berlinSchoenefeld.getId())).thenReturn(
            berlinSchoenefeldParameterChanged);
        when(staticAddressRepositoryMock.save(berlinSchoenefeld)).thenReturn(berlinSchoenefeld);
        assertThat(sut.saveStaticAddress(berlinSchoenefeld), is(berlinSchoenefeld));
        verify(staticAddressRepositoryMock, times(1)).findByLatitudeAndLongitude(any(BigDecimal.class),
            any(BigDecimal.class));
        verify(staticAddressRepositoryMock, never()).findByCityAndSuburbAndPostalcode(anyString(), anyString(),
            anyString());
    }


    @Test(expected = StaticAddressCoordinatesDuplicationException.class)
    public void updaaticAddressWithDuplicateCoordinatesError() {

        berlinSchoenefeld.setId(1L);

        StaticAddress address = new StaticAddress();
        address.setPostalcode(berlinSchoenefeld.getPostalcode());
        address.setCity(berlinSchoenefeld.getCity());
        address.setSuburb(berlinSchoenefeld.getSuburb());
        address.setLongitude(BigDecimal.TEN);
        address.setLatitude(BigDecimal.ONE);

        when(staticAddressRepositoryMock.findOne(berlinSchoenefeld.getId())).thenReturn(address);
        when(staticAddressRepositoryMock.findByLatitudeAndLongitude(any(BigDecimal.class), any(BigDecimal.class)))
            .thenReturn(new StaticAddress());

        sut.saveStaticAddress(berlinSchoenefeld);

        verify(staticAddressRepositoryMock, never()).save(any(StaticAddress.class));

        verify(staticAddressRepositoryMock, never()).findByCityAndSuburbAndPostalcode(anyString(), anyString(),
            anyString());
    }


    @Test
    public void updaaticAddressWithKeyValueChanges() {

        berlinSchoenefeld.setId(1L);
        berlinSchoenefeld.setCityNormalized(CITY_BERLIN_NORMALIZED);
        berlinSchoenefeld.setSuburbNormalized(CITY_SCHOENEFELD_NORMALIZED);

        when(staticAddressRepositoryMock.findOne(berlinSchoenefeld.getId())).thenReturn(
            berlinSchoenefeldWithoutPostalcode);
        when(staticAddressRepositoryMock.findByCityAndSuburbAndPostalcode(berlinSchoenefeld.getCityNormalized(),
                    berlinSchoenefeld.getSuburbNormalized(), berlinSchoenefeld.getPostalcode())).thenReturn(
            new ArrayList<>());
        when(staticAddressRepositoryMock.save(berlinSchoenefeld)).thenReturn(berlinSchoenefeld);

        sut.saveStaticAddress(berlinSchoenefeld);

        assertThat(sut.saveStaticAddress(berlinSchoenefeld), is(berlinSchoenefeld));
    }


    @Test(expected = StaticAddressDuplicationException.class)
    public void updaaticAddressWithKeyValueChangesAndDuplicates() {

        berlinSchoenefeld.setId(1L);
        berlinSchoenefeld.setCity(CITY_BERLIN);
        berlinSchoenefeld.setSuburb(CITY_SCHOENEFELD);

        List<StaticAddress> staticAddresses = new ArrayList<>();
        staticAddresses.add(berlinSchoenefeld);

        when(staticAddressRepositoryMock.findOne(berlinSchoenefeld.getId())).thenReturn(
            berlinSchoenefeldWithoutPostalcode);

        when(staticAddressRepositoryMock.findByCityAndSuburbAndPostalcode(berlinSchoenefeld.getCity(),
                    berlinSchoenefeld.getSuburb(), berlinSchoenefeld.getPostalcode())).thenReturn(staticAddresses);

        sut.saveStaticAddress(berlinSchoenefeld);
    }


    @Test
    public void testGetAddressListListForStaticAddressId() {

        List<AddressList> expectedListOfLists = new ArrayList<>();
        StaticAddress expectedAddress = new StaticAddress();
        expectedAddress.setCountry("Unique Attribute for distinction");

        AddressList expectedList = new AddressList("Result ", singletonList(expectedAddress.toAddress()));
        expectedListOfLists.add(expectedList);

        when(staticAddressRepositoryMock.findOne(STATIC_ADDRESS_ID)).thenReturn(expectedAddress);

        Assert.assertEquals(expectedListOfLists, sut.getAddressListListForStaticAddressId(STATIC_ADDRESS_ID));
    }


    @Test
    public void testGetAddressListListForNotExistingStaticAddressId() {

        when(staticAddressRepositoryMock.findOne(STATIC_ADDRESS_ID)).thenReturn(null);

        Assert.assertEquals(new ArrayList<AddressList>(), sut.getAddressListListForStaticAddressId(STATIC_ADDRESS_ID));
    }


    @Test
    public void testGetAddressListListForGeolocation() {

        List<AddressList> expectedListOfLists = new ArrayList<>();
        StaticAddress expectedAddress = new StaticAddress();
        expectedAddress.setCountry("Unique Attribute for distinction");

        AddressList expectedList = new AddressList("Result ", singletonList(expectedAddress.toAddress()));
        expectedListOfLists.add(expectedList);

        when(staticAddressRepositoryMock.findByLatitudeAndLongitude(any(BigDecimal.class), any(BigDecimal.class)))
            .thenReturn(expectedAddress);

        Assert.assertEquals(expectedListOfLists,
            sut.getAddressListListForGeolocation(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN)));
    }


    @Test
    public void testGetAddressListListForNotExistingGeolocation() {

        when(staticAddressRepositoryMock.findByLatitudeAndLongitude(BigDecimal.ONE, BigDecimal.TEN)).thenReturn(null);

        Assert.assertEquals(new ArrayList<AddressList>(),
            sut.getAddressListListForGeolocation(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN)));
    }


    @Test
    public void normalizeFields() {

        StaticAddress address = new StaticAddress();
        sut.normalizeFields(address);

        assertThat(address.getCityNormalized(), nullValue());
        assertThat(address.getSuburbNormalized(), nullValue());
    }


    @Test
    public void normalizeFieldsNonNull() {

        StaticAddress address = new StaticAddress();
        address.setCity("FOO");
        address.setSuburb("BAR");

        when(normalizerServiceMock.normalize("FOO")).thenReturn("foo");
        when(normalizerServiceMock.normalize("BAR")).thenReturn("bar");

        sut.normalizeFields(address);

        assertThat(address.getCityNormalized(), is("foo"));
        assertThat(address.getSuburbNormalized(), is("bar"));
    }


    @Test
    public void fillMissingHashKeys() {

        Pageable pageable1 = new PageRequest(0, 3);
        Page<StaticAddress> page = new PageImpl<>(asList(kaSuedstadt, kaNordstadt, kaWeststadt), pageable1, 3);

        Pageable pageable2 = new PageRequest(0, 3);
        Page<StaticAddress> lastPage = new PageImpl<>(emptyList(), pageable2, 50);

        when(staticAddressRepositoryMock.findMissingHashKeys(any(Pageable.class))).thenReturn(page)
            .thenReturn(page)
            .thenReturn(lastPage);

        sut.fillMissingHashKeys();

        verify(staticAddressRepositoryMock, times(3)).findMissingHashKeys(any(Pageable.class));
        verify(staticAddressRepositoryMock, times(3)).save(any(StaticAddress.class));
    }


    @Test
    public void getAddressesInBoundingBox() {

        double distance = 20d;
        GeoLocation corner = new GeoLocation(BigDecimal.ONE, BigDecimal.TEN);
        GeoLocation location = mock(GeoLocation.class);
        BoundingBox boundingBox = mock(BoundingBox.class);

        when(location.getBoundingBox(distance)).thenReturn(boundingBox);
        when(boundingBox.getLowerLeft()).thenReturn(corner);
        when(boundingBox.getUpperRight()).thenReturn(corner);
        when(staticAddressRepositoryMock.findByBoundingBox(argThat(closeTo(BigDecimal.ONE, BigDecimal.ZERO)),
                    argThat(closeTo(BigDecimal.ONE, BigDecimal.ZERO)),
                    argThat(closeTo(BigDecimal.TEN, BigDecimal.ZERO)),
                    argThat(closeTo(BigDecimal.TEN, BigDecimal.ZERO)))).thenReturn(singletonList(berlinSchoenefeld));

        List<StaticAddress> addresses = sut.getAddressesInBoundingBox(location, distance);

        assertThat(addresses, hasSize(1));
    }


    @Test
    public void findByHashKey() {

        String hashKey = "ABCDE";
        StaticAddress expectedStaticAddress = new StaticAddress();
        when(staticAddressRepositoryMock.findByHashKey(hashKey)).thenReturn(expectedStaticAddress);

        StaticAddress staticAddress = sut.findByHashKey(hashKey);
        assertThat(staticAddress, is(expectedStaticAddress));
    }


    @Test(expected = StaticAddressNotFoundException.class)
    public void findByHashKeyWithException() {

        String hashKey = "ABCDE";
        when(staticAddressRepositoryMock.findByHashKey(hashKey)).thenReturn(null);

        sut.findByHashKey(hashKey);
    }


    private void initaddresses() {

        kaSuedstadt = new StaticAddress();
        kaSuedstadt.setId(76137L);
        kaSuedstadt.setPostalcode(POSTAL_CODE_76137);
        kaSuedstadt.setCity(CITY_KARLSRUHE);
        kaSuedstadt.setSuburb("Suedstadt");
        kaSuedstadt.setCountry(COUNTRY_DE);
        kaSuedstadt.setLatitude(new BigDecimal(1.1));
        kaSuedstadt.setLongitude(new BigDecimal(1.2));

        kaWeststadt = new StaticAddress();
        kaWeststadt.setId(76135L);
        kaWeststadt.setPostalcode("76135");
        kaWeststadt.setCity(CITY_KARLSRUHE);
        kaWeststadt.setSuburb("Weststadt");
        kaWeststadt.setCountry(COUNTRY_DE);
        kaWeststadt.setLatitude(new BigDecimal(2.1));
        kaWeststadt.setLongitude(new BigDecimal(2.2));

        kaNordstadt = new StaticAddress();
        kaNordstadt.setId(76133L);
        kaNordstadt.setPostalcode("76133");
        kaNordstadt.setCity(CITY_KARLSRUHE);
        kaNordstadt.setSuburb("Nordstadt");
        kaNordstadt.setCountry(COUNTRY_DE);
        kaNordstadt.setLatitude(new BigDecimal(3.1));
        kaNordstadt.setLongitude(new BigDecimal(3.2));

        StaticAddress berlinSteglitz = new StaticAddress();
        berlinSteglitz.setId(12157L);
        berlinSteglitz.setPostalcode("12157");
        berlinSteglitz.setCity(CITY_BERLIN);
        berlinSteglitz.setSuburb("Steglitz");
        berlinSteglitz.setCountry(COUNTRY_DE);
        berlinSteglitz.setLatitude(new BigDecimal(4.1));
        berlinSteglitz.setLongitude(new BigDecimal(4.2));

        berlinSchoenefeld = new StaticAddress();
        berlinSchoenefeld.setPostalcode("12345");
        berlinSchoenefeld.setCity(CITY_BERLIN);
        berlinSchoenefeld.setSuburb(CITY_SCHOENEFELD);
        berlinSchoenefeld.setLatitude(new BigDecimal(4.1));
        berlinSchoenefeld.setLongitude(new BigDecimal(4.2));

        berlinSchoenefeldWithoutPostalcode = new StaticAddress();
        berlinSchoenefeldWithoutPostalcode.setCity(berlinSchoenefeld.getCity());
        berlinSchoenefeldWithoutPostalcode.setSuburb(berlinSchoenefeld.getSuburb());
        berlinSchoenefeldWithoutPostalcode.setPostalcode("");
        berlinSchoenefeldWithoutPostalcode.setLatitude(berlinSchoenefeld.getLatitude());
        berlinSchoenefeldWithoutPostalcode.setLongitude(berlinSchoenefeld.getLongitude());
    }
}
