package net.contargo.iris.route.dto;

import net.contargo.iris.connection.dto.RouteDto;
import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteData;
import net.contargo.iris.route.RouteDirection;
import net.contargo.iris.route.RouteProduct;
import net.contargo.iris.route.service.EnricherService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class EnricherDtoServiceImplUnitTest {

    public static final String ROOT_NAME = "Ruth";
    public static final String ROUTE_SHORTNAME = "kurzRuth";
    private EnricherDtoServiceImpl sut;
    @Mock
    private EnricherService enricherServiceMock;
    @Mock
    private Route routeMock;
    @Mock
    private RouteData routeDataMock;

    // This isn't a mock because it is final
    private RouteDto routeDto;

    @Before
    public void setUp() {

        sut = new EnricherDtoServiceImpl(enricherServiceMock);

        when(routeMock.getData()).thenReturn(routeDataMock);
        when(routeMock.getDirection()).thenReturn(RouteDirection.EXPORT);
        when(routeMock.getName()).thenReturn(ROOT_NAME);
        when(routeMock.getProduct()).thenReturn(RouteProduct.ONEWAY);
        when(routeMock.getShortName()).thenReturn(ROUTE_SHORTNAME);
        when(routeMock.isRoundTrip()).thenReturn(true);

        when(routeDataMock.getCo2()).thenReturn(BigDecimal.TEN);
        when(routeDataMock.getCo2DirectTruck()).thenReturn(BigDecimal.ZERO);

        routeDto = new RouteDto(routeMock);

        when(enricherServiceMock.enrich(any(Route.class))).thenReturn(routeMock);
    }


    @Test
    public void testEnrich() {

        RouteDto returnedRouteDto = sut.enrich(routeDto);

        verify(enricherServiceMock).enrich(any(Route.class));

        assertReflectionEquals(routeDto, returnedRouteDto);
    }
}
