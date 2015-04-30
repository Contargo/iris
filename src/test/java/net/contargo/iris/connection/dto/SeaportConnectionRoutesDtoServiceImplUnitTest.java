package net.contargo.iris.connection.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.connection.service.SeaportConnectionRoutesService;
import net.contargo.iris.container.ContainerType;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteCombo;
import net.contargo.iris.route.RouteDirection;
import net.contargo.iris.route.RouteInformation;
import net.contargo.iris.route.RouteProduct;
import net.contargo.iris.seaport.dto.SeaportDto;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.when;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import static java.util.Arrays.asList;


/**
 * Unit test for {@link net.contargo.iris.connection.dto.SeaportConnectionRoutesDtoServiceImpl}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class SeaportConnectionRoutesDtoServiceImplUnitTest {

    private SeaportConnectionRoutesDtoServiceImpl sut;

    @Mock
    private SeaportConnectionRoutesService seaportConnectionRoutesServiceMock;

    @Before
    public void setUp() {

        sut = new SeaportConnectionRoutesDtoServiceImpl(seaportConnectionRoutesServiceMock);
    }


    @Test
    public void getAvailableSeaportConnectionRoutes() {

        SeaportDto port = new SeaportDto();
        GeoLocation location = new GeoLocation();

        Route route1 = new Route();
        Route route2 = new Route();

        RouteInformation information = new RouteInformation(location, RouteProduct.ONEWAY, ContainerType.FORTY,
                RouteDirection.EXPORT, RouteCombo.ALL);

        when(seaportConnectionRoutesServiceMock.getAvailableSeaportConnectionRoutes(port.toEntity(), information))
            .thenReturn(asList(route1, route2));

        List<RouteDto> routes = sut.getAvailableSeaportConnectionRoutes(port, information);

        assertThat(routes, hasSize(2));
        assertReflectionEquals(new RouteDto(route1), routes.get(0));
        assertReflectionEquals(new RouteDto(route2), routes.get(1));
    }


    @Test
    public void getAvailableSeaportConnectionRoutesForNullSeaport() {

        RouteInformation information = new RouteInformation(new GeoLocation(), RouteProduct.ONEWAY, ContainerType.FORTY,
                RouteDirection.EXPORT, RouteCombo.ALL);

        List<RouteDto> routes = sut.getAvailableSeaportConnectionRoutes(null, information);

        assertThat(routes, empty());
    }
}
