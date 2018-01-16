
package net.contargo.iris.address.nominatim.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.countries.service.CountryService;

import org.hamcrest.MatcherAssert;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.anyOf;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.startsWith;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;


/**
 * Unit test for {@link NominatimUrlBuilder}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class NominatimUrlBuilderUnitTest {

    private static final int OSM_ID = 77;
    private static final int PLACE_ID = 42;

    private NominatimUrlBuilder sut;

    @Mock
    private CountryService countryServiceMock;

    @Before
    public void setup() {

        String baseUrl = "http://maps.contargo.net/nominatim/";
        String language = "de";

        Map<String, String> countryMap = new HashMap<>();
        countryMap.put("Netherlands", "NL");
        countryMap.put("Germany", "DE");

        when(countryServiceMock.getCountries()).thenReturn(countryMap);

        sut = new NominatimUrlBuilder(baseUrl, language, countryServiceMock);
    }


    @Test
    public void testConstructorAndBuildUrl() {

        String url = sut.buildUrl("karlstrasse 68", "76137", "karlsruhe", "de", null);

        assertThat(url, startsWith("http://maps.contargo.net/nominatim/search.php?"));
        assertThat(url, containsString("accept-language=de"));
        assertThat(url, containsString("format=json"));

        // URL encoding results in: whitespaces are replaced by plus and commas by %2C
        assertThat(url, containsString("karlstrasse 68,76137 karlsruhe"));
        assertThat(url, containsString("countrycodes=de"));
    }


    @Test
    public void testBuildUrlWithNoCountryGiven() {

        String url = sut.buildUrl(null, null, "karlsruhe", null, null);

        assertThat(url, startsWith("http://maps.contargo.net/nominatim/search.php?"));
        assertThat(url, containsString("accept-language=de"));
        assertThat(url, containsString("format=json"));
        assertThat(url, containsString("karlsruhe"));

        assertThat(url, anyOf(containsString("countrycodes=DE,NL"), containsString("countrycodes=NL,DE")));
    }


    @Test
    public void testBuildQuery() {

        // URL encoding results in: whitespaces are replaced by plus and commas by %2C

        String query = sut.buildQuery("karlstrasse 68", "76137", "karlsruhe", null);
        assertThat(query, containsString("karlstrasse 68,76137 karlsruhe"));

        query = sut.buildQuery("karlstrasse 68", "76137", "karlsruhe", null);
        assertThat(query, containsString("karlstrasse 68,76137 karlsruhe"));

        query = sut.buildQuery("", "", "karlsruhe", null);
        assertThat(query, containsString("karlsruhe"));

        query = sut.buildQuery(null, "76137", "karlsruhe", "Fotostudio Becker");
        assertThat(query, containsString("76137 karlsruhe Fotostudio Becker"));
    }


    @Test
    public void testAppendStringWithSeparatorIfHasText() {

        StringBuilder builder = new StringBuilder();
        sut.appendStringWithSeparatorIfHasText(builder, null, " ");
        assertThat(builder.toString(), isEmptyString());

        builder = new StringBuilder();
        sut.appendStringWithSeparatorIfHasText(builder, "", " ");
        assertThat(builder.toString(), isEmptyString());

        builder = new StringBuilder();
        sut.appendStringWithSeparatorIfHasText(builder, " ", ",");
        assertThat(builder.toString(), isEmptyString());

        builder = new StringBuilder();
        sut.appendStringWithSeparatorIfHasText(builder, "Hello World", ",");
        assertThat(builder.toString(), containsString("Hello World,"));
    }


    @Test
    public void testBuildSuburbUrlForTypeSuburb() {

        String url = sut.buildSuburbUrl(PLACE_ID, SuburbType.SUBURB.getType());
        assertThat(url,
            containsString(
                "http://maps.contargo.net/nominatim/suburb.php?place_id=42&format=json&addressdetails=1&suburb_type="
                    .concat(SuburbType.SUBURB.getType())));
    }


    @Test
    public void testBuildSuburbUrlForTypeAdministrative() {

        String url = sut.buildSuburbUrl(PLACE_ID, SuburbType.ADMINISTRATIVE.getType());
        assertThat(url,
            containsString(
                "http://maps.contargo.net/nominatim/suburb.php?place_id=42&format=json&addressdetails=1&suburb_type="
                    .concat(SuburbType.ADMINISTRATIVE.getType())));
    }


    @Test
    public void testBuildSuburbUrlForTypeVillage() {

        String url = sut.buildSuburbUrl(PLACE_ID, SuburbType.VILLAGE.getType());
        assertThat(url,
            containsString(
                "http://maps.contargo.net/nominatim/suburb.php?place_id=42&format=json&addressdetails=1&suburb_type="));
    }


    @Test
    public void buildOsmUrl() {

        // using OsmType.WAY
        assertThat(sut.buildOsmUrl(OSM_ID, OsmType.WAY),
            containsString("http://maps.contargo.net/nominatim/reverse?osm_id=" + OSM_ID
                + "&osm_type=W&format=json"));

        // using OsmType.NODE
        assertThat(sut.buildOsmUrl(OSM_ID, OsmType.NODE),
            containsString("http://maps.contargo.net/nominatim/reverse?osm_id=" + OSM_ID
                + "&osm_type=N&format=json"));
    }


    @Test
    public void buildUrl() {

        GeoLocation geoLocation = new GeoLocation(new BigDecimal(48.07506), new BigDecimal(8.6362987));
        MatcherAssert.assertThat(sut.buildUrl(geoLocation),
            is("http://maps.contargo.net/nominatim/reverse/?format=json&lat=48.0750600000&lon=8.6362987000"));
    }


    @Test(expected = IllegalArgumentException.class)
    public void buildUrlWithoutLatitude() {

        GeoLocation locationWithoutLatitude = new GeoLocation();
        locationWithoutLatitude.setLongitude(BigDecimal.ONE);
        sut.buildUrl(locationWithoutLatitude);
    }


    @Test(expected = IllegalArgumentException.class)
    public void buildUrlWithoutLongitude() {

        GeoLocation locationWithoutLongitude = new GeoLocation();
        locationWithoutLongitude.setLatitude(BigDecimal.ONE);
        sut.buildUrl(locationWithoutLongitude);
    }


    @Test
    public void buildSearchUrl() {

        assertThat(sut.buildSearchUrl("streetName"),
            is("http://maps.contargo.net/nominatim/search/streetName?format=json&addressdetails=1"));
        assertThat(sut.buildSearchUrl("streetName cityname"),
            is("http://maps.contargo.net/nominatim/search/streetName cityname?format=json&addressdetails=1"));
    }
}
