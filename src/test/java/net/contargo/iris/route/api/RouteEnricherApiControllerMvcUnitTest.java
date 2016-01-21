package net.contargo.iris.route.api;

import net.contargo.iris.connection.dto.RouteDataDto;
import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.connection.dto.RoutePartDto;
import net.contargo.iris.route.dto.EnricherDtoService;
import net.contargo.iris.security.UserAuthenticationService;
import net.contargo.iris.security.UserClassification;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.when;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:public-api-context.xml" })
@WebAppConfiguration
public class RouteEnricherApiControllerMvcUnitTest {

    @Autowired
    private EnricherDtoService enricherDtoServiceMock;
    @Autowired
    private UserAuthenticationService userAuthenticationService;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {

        mockMvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void testGetEnrichedRoute() throws Exception {

        RouteDto routeDto = new RouteDto();
        routeDto.setName("RuthResponse");

        RouteDataDto routeData = new RouteDataDto();
        List<RoutePartDto> routePartList = singletonList(new RoutePartDto());
        routeData.setParts(routePartList);
        routeDto.setData(routeData);

        UserDetails userDetails = new User("foo", "passsword", emptyList());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                "password");
        when(userAuthenticationService.getCurrentUser()).thenReturn(authentication);
        when(userAuthenticationService.getUserClassification("foo")).thenReturn(UserClassification.EXTERN);

        when(enricherDtoServiceMock.enrich(any(RouteDto.class))).thenReturn(routeDto);

        ResultActions resultActions = mockMvc.perform(get("/routedetails").param("name", "Ruth")
                .accept(APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().contentType(APPLICATION_JSON_VALUE));
        resultActions.andExpect((jsonPath("$.response.route.name", is("RuthResponse"))));
        resultActions.andExpect(model().attributeDoesNotExist("routeDto"));
    }
}
