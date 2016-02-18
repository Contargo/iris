package net.contargo.iris.address.staticsearch.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.normalizer.NormalizerService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


@RunWith(MockitoJUnitRunner.class)
public class PostcodeCityStaticAddressMappingProcessorUnitTest {

    public static final String CITY_NORMALIZED = "STADTGENAU";
    public static final String CITY = "Stadtgenau";
    private PostcodeCityStaticAddressMappingProcessor sut;

    @Mock
    private StaticAddressService staticAddressService;
    @Mock
    private NormalizerService normalizerService;

    @Before
    public void setUp() {

        sut = new PostcodeCityStaticAddressMappingProcessor(null, staticAddressService, normalizerService);
    }


    @Test
    public void map() {

        Map<String, String> addressMap = new HashMap<>();
        addressMap.put("country_code", "ch");
        addressMap.put("postcode", "12345");
        addressMap.put("city", CITY);

        Address address = new Address();
        address.setAddress(addressMap);

        StaticAddress matchingStaticAddress = new StaticAddress();
        matchingStaticAddress.setCityNormalized(CITY_NORMALIZED);

        StaticAddress noneMatchingStaticAddressCity = new StaticAddress();
        noneMatchingStaticAddressCity.setCityNormalized("foo");

        StaticAddress noneMatchingStaticAddressSuburb = new StaticAddress();
        noneMatchingStaticAddressSuburb.setCityNormalized(CITY_NORMALIZED);
        noneMatchingStaticAddressSuburb.setSuburb("bar");

        when(normalizerService.normalize(CITY)).thenReturn(CITY_NORMALIZED);
        when(staticAddressService.findByPostalcodeAndCountry("12345", "ch")).thenReturn(asList(matchingStaticAddress,
                noneMatchingStaticAddressCity, noneMatchingStaticAddressSuburb));

        List<StaticAddress> map = sut.map(address);
        assertThat(map, hasSize(1));
        assertThat(map.get(0), is(matchingStaticAddress));
    }
}
