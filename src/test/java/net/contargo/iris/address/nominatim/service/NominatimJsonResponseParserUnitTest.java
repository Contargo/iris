package net.contargo.iris.address.nominatim.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.contargo.iris.address.Address;
import net.contargo.iris.util.HttpUtil;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.when;


/**
 * Unit test for {@link NominatimJsonResponseParser} using constant file with address entries in Json format.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class NominatimJsonResponseParserUnitTest {

    private static final String NOMINATIM_SAMPLE = "src/test/resources/nominatim/json_sample.json";
    private static final String NOMINATIM_SINGLE_SAMPLE = "src/test/resources/nominatim/json_sample_single_result.json";
    private static final int DELTA = 1;
    private static final String REQUEST =
        "http://maps.contargo.net/nominatim/reverse/?format=json&lat=48.0750600000&lon=8.6362987000";
    private static final String ADDRESS =
        "Fotostudio Becker, 68, Karlstrasse, Suedweststadt, Karlsruhe, Regierungsbezirk Karlsruhe, Baden-Wuerttemberg, 76137, Federal Republic of Germany (land mass)";
    private static final String ADDRESS_EBERHARDSTR =
        "Eberhardstraße, Schura, Trossingen, Landkreis Tuttlingen, Regierungsbezirk Freiburg, Baden-Württemberg, 78647, Germany";

    private NominatimJsonResponseParser sut;

    @Mock
    private HttpUtil httpUtilMock;

    @Before
    public void setup() {

        sut = new NominatimJsonResponseParser(httpUtilMock, new ObjectMapper());
    }


    @Test
    public void getAddressesForUrl() throws IOException {

        when(httpUtilMock.getResponseContent(anyString())).thenReturn(getStringFromJson(NOMINATIM_SAMPLE));

        List<Address> addresses = sut.getAddressesForUrl("foo");
        assertThat(addresses.size(), is(5));

        Address address = addresses.get(0);
        assertThat(address.getDisplayName(), is(ADDRESS));
        assertThat(address.getOsmId(), is(90085697L));
        assertThat(address.getLatitude().doubleValue(), closeTo(49.002095196515d, DELTA));
        assertThat(address.getLongitude().doubleValue(), closeTo(8.39391718305592d, DELTA));
    }


    @Test
    public void getAddressesForUrlForOsmId() throws IOException {

        when(httpUtilMock.getResponseContent(anyString())).thenReturn(getStringFromJson(NOMINATIM_SINGLE_SAMPLE));

        List<Address> addresses = sut.getAddressesForUrlForOsmId("foo");
        assertThat(addresses.size(), is(1));

        Address address = addresses.get(0);
        assertThat(address.getDisplayName(), is(ADDRESS));
        assertThat(address.getOsmId(), is(90085697L));
        assertThat(address.getLatitude().doubleValue(), closeTo(49.002095196515d, DELTA));
        assertThat(address.getLongitude().doubleValue(), closeTo(8.39391718305592d, DELTA));
    }


    @Test
    public void noResponse() {

        when(httpUtilMock.getResponseContent(anyString())).thenReturn(null);

        List<Address> addresses = sut.getAddressesForUrl("foo");
        assertThat(addresses, nullValue());
    }


    @Test
    public void getCountryCode() throws IOException {

        when(httpUtilMock.getResponseContent(anyString())).thenReturn(getStringFromJson(NOMINATIM_SAMPLE));

        Address address = sut.getAddressesForUrl("foo").get(0);
        assertThat(address.getCountryCode(), is("de"));
    }


    @Test
    public void getAddressForUrl() {

        when(httpUtilMock.getResponseContent(REQUEST)).thenReturn(getResponse());

        String displayName = sut.getAddressForUrl(REQUEST).getDisplayName();
        assertThat(displayName, is(ADDRESS_EBERHARDSTR));
    }


    @Test
    public void getAddressForUrlRequestNull() {

        when(httpUtilMock.getResponseContent(REQUEST)).thenReturn(getResponse());

        Address address = sut.getAddressForUrl(null);
        assertThat(address, nullValue());
    }


    private String getStringFromJson(String path) throws IOException {

        return new ObjectMapper().readTree(new File(path)).toString();
    }


    private String getResponse() {

        return "{" + "\"place_id\":\"36907539\"," + "\"licence\":\"Data \\u00a9 OpenStreetMap contributors"
            + ", ODbL 1.0. http:\\/\\/www.openstreetmap.org\\/copyright\"," + "\"osm_type\":\"way\","
            + "\"osm_id\":\"23515787\"," + "\"lat\":\"48.0743824\"," + "\"lon\":\"8.6358638\","
            + "\"display_name\":\"Eberhardstra\\u00dfe, Schura, Trossingen, Landkreis Tuttlingen"
            + ", Regierungsbezirk Freiburg, Baden-W\\u00fcrttemberg, 78647, Germany\"," + "\"address\":{"
            + "\"road\":\"Eberhardstra\\u00dfe\"," + "\"suburb\":\"Schura\"," + "\"city\":\"Trossingen\","
            + "\"county\":\"Landkreis Tuttlingen\"," + "\"state_district\":\"Regierungsbezirk Freiburg\","
            + "\"state\":\"Baden-W\\u00fcrttemberg\"," + "\"postcode\":\"78647\"," + "\"country\":\"Germany\","
            + "\"country_code\":\"de\"" + "}}";
    }
}
