package net.contargo.iris.countries.api;

import net.contargo.iris.countries.dto.CountryDto;
import net.contargo.iris.countries.dto.CountryDtoService;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Collections.singletonList;


/**
 * MVC Unit test of {@link CountriesApiController}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
public class CountriesApiControllerMvcUnitTest {

    private static final String GERMANY = "Germany";
    private static final String GER = "GER";
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CountryDtoService countryDtoServiceMock;

    @Test
    public void geoCodeByOsmId() throws Exception {

        when(countryDtoServiceMock.getCountries()).thenReturn(singletonList(new CountryDto(GERMANY, GER)));

        MockHttpServletRequestBuilder builder = get("/countries");
        builder.accept(APPLICATION_JSON);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(view().name("countries"));
        resultActions.andExpect(content().contentType(APPLICATION_JSON));
        resultActions.andExpect(jsonPath("$.countriesResponse.countries", hasSize(1)));
        resultActions.andExpect(jsonPath("$.countriesResponse.countries[0].name", is(GERMANY)));
        resultActions.andExpect(jsonPath("$.countriesResponse.countries[0].value", is(GER)));
        resultActions.andExpect(jsonPath("$.countriesResponse.links[0].href", endsWith("/countries")));

        verify(countryDtoServiceMock).getCountries();
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
