package net.contargo.iris.api.discover;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_CONNECTIONS;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_COUNTRIES;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_GEOCODE;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_OSM_ADDRESSES;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_REVERSE_GEOCODE;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_ROUTES;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_ROUTE_DETAILS_EXAMPLE;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_SEAPORTS_OF_CONNECTIONS;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_SEAPORTS_OF_CONNECTIONS_FILTERED;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_SEAPORT_EXAMPLE;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_SIMPLE_GEOCODES_EXAMPLE;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_TERMINALS;
import static net.contargo.iris.api.discover.DiscoverPublicApiController.REL_TERMINAL_EXAMPLE;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * MVC Unit test of {@link net.contargo.iris.api.discover.DiscoverPublicApiController}.
 *
 * @author  David Schilling - schilling@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class DiscoverPublicApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void discover() throws Exception {

        ResultActions resultActions = perform(get("/").accept(APPLICATION_JSON));

        resultActions.andExpect(jsonPath("$.response.links", hasSize(13)));

        String responseContent = new ObjectMapper().readTree(resultActions.andReturn()
                    .getResponse()
                    .getContentAsString())
                .get("response")
                .toString();

        DiscoverResponse response =
            new ObjectMapper().readValue(responseContent, new TypeReference<DiscoverResponse>() {
                });

        assertThat(response.getLink(REL_COUNTRIES).getHref(), is("http://localhost/countries"));
        assertThat(response.getLink(REL_OSM_ADDRESSES).getHref(),
            is("http://localhost/osmaddresses/134631686?_=1381911583029"));
        assertThat(response.getLink(REL_REVERSE_GEOCODE).getHref(),
            is("http://localhost/reversegeocode/49.123:8.12/"));
        assertThat(response.getLink(REL_GEOCODE).getHref(),
            is("http://localhost/geocodes?city=Karlsruhe&postalcode=76137"));
        assertThat(response.getLink(REL_SEAPORTS_OF_CONNECTIONS).getHref(),
            is("http://localhost/connections/seaports?combo=ALL"));
        assertThat(response.getLink(REL_SEAPORTS_OF_CONNECTIONS_FILTERED).getHref(),
            is("http://localhost/connections/seaports?combo=RAILWAY"));
        assertThat(response.getLink(REL_SEAPORT_EXAMPLE).getHref(), is("http://localhost/seaports/1301000000000001"));
        assertThat(response.getLink(REL_TERMINALS).getHref(), is("http://localhost/terminals?activeOnly=true"));
        assertThat(response.getLink(REL_TERMINAL_EXAMPLE).getHref(),
            is("http://localhost/terminals/1301000000000001"));
        assertThat(response.getLink(REL_CONNECTIONS).getHref(),
            is("http://localhost/connections/1301000000000001/49.0:8.41/true?containerType=TWENTY_LIGHT&isImport=false&combo=WATERWAY"));
        assertThat(response.getLink(REL_ROUTES).getHref(),
            is("http://localhost/routes/1301000000000001/49.0:8.41/true?containerType=TWENTY_LIGHT&isImport=false&combo=WATERWAY"));
        assertThat(response.getLink(REL_SIMPLE_GEOCODES_EXAMPLE).getHref(),
            is("http://localhost/simplegeocodes?city=Karlsruhe&postalcode=76137"));
        assertThat(response.getLink(REL_ROUTE_DETAILS_EXAMPLE).getHref(),
            is("http://localhost/routedetails?data.parts[0].origin.longitude=4.3"
                + "&data.parts[0].origin.latitude=51.36833"
                + "&data.parts[0].destination.longitude=8.2852700000"
                + "&data.parts[0].destination.latitude=49.0690300000"
                + "&data.parts[0].routeType=BARGE&data.parts[0].containerType=TWENTY_LIGHT"
                + "&data.parts[0].containerState=FULL&data.parts[1].origin.longitude=8.2852700000"
                + "&data.parts[1].origin.latitude=49.0690300000&data.parts[1].destination.longitude=8.41"
                + "&data.parts[1].destination.latitude=49.0&data.parts[1].routeType=TRUCK"
                + "&data.parts[1].containerType=TWENTY_LIGHT&data.parts[1].containerState=FULL"
                + "&data.parts[2].origin.longitude=8.41&data.parts[2].origin.latitude=49.0"
                + "&data.parts[2].destination.longitude=8.2852700000"
                + "&data.parts[2].destination.latitude=49.0690300000&data.parts[2].routeType=TRUCK"
                + "&data.parts[2].containerType=TWENTY_LIGHT&data.parts[2].containerState=EMPTY"
                + "&data.parts[3].origin.longitude=8.2852700000"
                + "&data.parts[3].origin.latitude=49.0690300000&data.parts[3].destination.longitude=4.3"
                + "&data.parts[3].destination.latitude=51.36833&data.parts[3].routeType=BARGE"
                + "&data.parts[3].containerType=TWENTY_LIGHT&data.parts[3].containerState=EMPTY"));
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
