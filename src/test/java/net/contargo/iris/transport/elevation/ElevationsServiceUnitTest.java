package net.contargo.iris.transport.elevation;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.transport.api.TransportDescriptionDto;
import net.contargo.iris.transport.api.TransportStop;
import net.contargo.iris.transport.elevation.client.ElevationProviderClient;
import net.contargo.iris.transport.elevation.client.RoutingClient;
import net.contargo.iris.transport.elevation.dto.ElevationPoint;
import net.contargo.iris.transport.elevation.dto.Point2D;
import net.contargo.iris.transport.elevation.dto.Point3D;
import net.contargo.iris.transport.elevation.smoothing.ElevationSmoother;

import org.junit.jupiter.api.Test;

import org.springframework.core.convert.ConversionService;

import java.math.BigDecimal;

import java.util.List;

import static net.contargo.iris.container.ContainerState.FULL;
import static net.contargo.iris.transport.api.ModeOfTransport.ROAD;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;


/**
 * @author  Petra Scherer - scherer@synyx.de
 */
class ElevationsServiceUnitTest {

    private ElevationProviderClient elevationProviderClientMock = mock(ElevationProviderClient.class);
    private RoutingClient routingClientMock = mock(RoutingClient.class);
    private ElevationSmoother elevationSmootherMock = mock(ElevationSmoother.class);
    private ConversionService conversionServiceMock = mock(ConversionService.class);

    private ElevationsService sut = new ElevationsService(elevationProviderClientMock, routingClientMock,
            elevationSmootherMock, conversionServiceMock);

    @Test
    void gettingElevationListFromPoints() {

        TransportStop stopFrom = new TransportStop(null, null, null, null);
        TransportStop stopTo = new TransportStop(null, null, null, null);

        TransportDescriptionDto.TransportDescriptionSegment segment =
            new TransportDescriptionDto.TransportDescriptionSegment(stopFrom, stopTo, FULL, true, ROAD);

        TransportDescriptionDto description = new TransportDescriptionDto(singletonList(segment));

        GeoLocation locationFrom = new GeoLocation();
        GeoLocation locationTo = new GeoLocation();

        when(conversionServiceMock.convert(stopFrom, GeoLocation.class)).thenReturn(locationFrom);
        when(conversionServiceMock.convert(stopTo, GeoLocation.class)).thenReturn(locationTo);

        List<Point2D> points2D = emptyList();
        List<Point3D> points3D = emptyList();

        when(routingClientMock.getPoints(locationFrom, locationTo)).thenReturn(points2D);
        when(elevationProviderClientMock.getElevations(points2D)).thenReturn(points3D);

        List<Point3D> smoothedPoints = asList(new Point3D(100, new BigDecimal(49.53294), new BigDecimal(8.102254)),
                new Point3D(125, new BigDecimal(49.552326), new BigDecimal(8.181607)),
                new Point3D(105, new BigDecimal(49.548811), new BigDecimal(8.237675)),
                new Point3D(115, new BigDecimal(49.553698), new BigDecimal(8.306469)));

        when(elevationSmootherMock.smooth(points3D)).thenReturn(smoothedPoints);

        Elevations elevations = sut.getElevationsFor(description);

        Elevations expected = new Elevations(asList(new ElevationPoint(100, 0), new ElevationPoint(125, 6118),
                    new ElevationPoint(105, 10182), new ElevationPoint(115, 15174)));

        assertReflectionEquals(elevations, expected);
    }
}
