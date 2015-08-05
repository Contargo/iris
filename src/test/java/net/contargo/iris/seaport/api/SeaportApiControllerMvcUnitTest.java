package net.contargo.iris.seaport.api;

import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.seaport.dto.SeaportDtoService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.argThat;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Collections.singletonList;


/**
 * Mvc Unit test for {@link SeaportApiController}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class SeaportApiControllerMvcUnitTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private SeaportDtoService seaportDtoService;

    private SeaportDto seaportDto;

    @Before
    public void setUp() {

        reset(seaportDtoService);
        createSeaportDto();
    }


    @Test
    public void getSeaports() throws Exception {

        when(seaportDtoService.getAllActive()).thenReturn(singletonList(seaportDto));

        MockHttpServletRequestBuilder builder = get("/seaports");
        builder.accept(APPLICATION_JSON);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json;charset=UTF-8"));
        resultActions.andExpect(jsonPath("$.seaports[0].latitude", is(seaportDto.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$.seaports[0].longitude", is(seaportDto.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$.seaports[0].name", is(seaportDto.getName())));
        resultActions.andExpect(jsonPath("$.seaports[0].enabled", is(seaportDto.isEnabled())));
        resultActions.andExpect(jsonPath("$.links", hasSize(1)));
        resultActions.andExpect(jsonPath("$.links[0].rel", is("self")));
        resultActions.andExpect(jsonPath("$.links[0].href", endsWith("/seaports?activeOnly=true")));

        verify(seaportDtoService, never()).getAll();
    }


    @Test
    public void getAllSeaports() throws Exception {

        when(seaportDtoService.getAll()).thenReturn(singletonList(seaportDto));

        MockHttpServletRequestBuilder builder = get("/seaports?activeOnly=false");
        builder.accept(APPLICATION_JSON);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType("application/json;charset=UTF-8"));
        resultActions.andExpect(jsonPath("$.seaports[0].latitude", is(seaportDto.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$.seaports[0].longitude", is(seaportDto.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$.seaports[0].name", is(seaportDto.getName())));
        resultActions.andExpect(jsonPath("$.seaports[0].enabled", is(seaportDto.isEnabled())));
        resultActions.andExpect(jsonPath("$.links", hasSize(1)));
        resultActions.andExpect(jsonPath("$.links[0].rel", is("self")));
        resultActions.andExpect(jsonPath("$.links[0].href", endsWith("/seaports?activeOnly=false")));

        verify(seaportDtoService, never()).getAllActive();
    }


    @Test
    public void getSeaportByUid() throws Exception {

        when(seaportDtoService.getByUid(new BigInteger("42"))).thenReturn(seaportDto);

        MockHttpServletRequestBuilder builder = get("/seaports/42");
        builder.accept(APPLICATION_JSON);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(APPLICATION_JSON));

        resultActions.andExpect(jsonPath("$response.seaport.latitude", is(seaportDto.getLatitude().intValue())));
        resultActions.andExpect(jsonPath("$response.seaport.longitude", is(seaportDto.getLongitude().intValue())));
        resultActions.andExpect(jsonPath("$response.seaport.name", is(seaportDto.getName())));
        resultActions.andExpect(jsonPath("$response.seaport.enabled", is(seaportDto.isEnabled())));

        resultActions.andExpect(jsonPath("$response.links", hasSize(2)));
        resultActions.andExpect(jsonPath("$response.links[0].rel", is("self")));
        resultActions.andExpect(jsonPath("$response.links[0].href", endsWith("/seaports/42")));
        resultActions.andExpect(jsonPath("$response.links[1].rel", is("seaports")));
    }


    @Test
    public void getSeaportWithInvalidUid() throws Exception {

        MockHttpServletRequestBuilder builder = get("/seaports/314");
        builder.accept(APPLICATION_JSON);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isNotFound());
    }


    @Test
    public void syncSeaportCreate() throws Exception {

        BigInteger uniqueId = new BigInteger("8491748714921");

        when(seaportDtoService.existsByUniqueId(uniqueId)).thenReturn(false);

        MockHttpServletRequestBuilder builder = put("/seaports/8491748714921");
        builder.accept(APPLICATION_JSON);
        builder.contentType(APPLICATION_JSON);
        builder.content("{\"name\": \"foo\", \"enabled\": true, \"latitude\": 0, \"longitude\": 0}");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isCreated());

        verify(seaportDtoService).save(argThat(
                org.hamcrest.Matchers.<SeaportDto>allOf(hasProperty("uniqueId", is(uniqueId.toString())),
                    hasProperty("name", is("foo")), hasProperty("enabled", is(true)),
                    hasProperty("latitude", is(BigDecimal.ZERO)), hasProperty("longitude", is(BigDecimal.ZERO)))));
    }


    @Test
    public void syncSeaportUpdate() throws Exception {

        BigInteger uniqueId = new BigInteger("8491748714921");

        when(seaportDtoService.existsByUniqueId(uniqueId)).thenReturn(true);

        MockHttpServletRequestBuilder builder = put("/seaports/8491748714921");
        builder.accept(APPLICATION_JSON);
        builder.contentType(APPLICATION_JSON);
        builder.content("{\"name\": \"foo\", \"enabled\": true, \"latitude\": 0, \"longitude\": 0}");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isNoContent());

        verify(seaportDtoService).updateSeaport(Mockito.eq(uniqueId),
            argThat(
                org.hamcrest.Matchers.<SeaportDto>allOf(hasProperty("name", is("foo")),
                    hasProperty("enabled", is(true)), hasProperty("latitude", is(BigDecimal.ZERO)),
                    hasProperty("longitude", is(BigDecimal.ZERO)))));
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }


    private void createSeaportDto() {

        seaportDto = new SeaportDto();
        seaportDto.setEnabled(true);
        seaportDto.setName("terminalDto");
        seaportDto.setLatitude(BigDecimal.ONE);
        seaportDto.setLongitude(BigDecimal.ZERO);
    }
}
