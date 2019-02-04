package net.contargo.iris.transport.inclinations.smoothing;

import net.contargo.iris.transport.inclinations.dto.Point3D;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static java.math.BigDecimal.valueOf;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
class ElevationSmootherTest {

    private ElevationSmoother sut = new ElevationSmoother();

    @Test
    void returnTheSameListForLessThanThreePoints() {

        List<Point3D> points = new ArrayList<>();
        points.add(new Point3D(100, valueOf(49.00000), valueOf(8.00000)));
        points.add(new Point3D(100, valueOf(49.00000), valueOf(8.00100)));

        assertThat(sut.smooth(points), is(points));
    }


    @Test
    void smoothPointsNotExceedingWindowCapacity() {

        List<Point3D> points = new ArrayList<>();
        points.add(new Point3D(100, valueOf(49.00000), valueOf(8.00000))); // the first point -> +1 to window
        points.add(new Point3D(120, valueOf(49.00000), valueOf(8.00030))); // distance: 21.89 -> +2 to window
        points.add(new Point3D(130, valueOf(49.00000), valueOf(8.00050))); // distance: 14.59 -> +1 to window
        points.add(new Point3D(125, valueOf(49.00000), valueOf(8.00060))); // distance: 7.30 -> +1 to window
        points.add(new Point3D(100, valueOf(49.00000), valueOf(8.00110))); // distance: 36.48 -> +3 to window

        List<Point3D> smoothedPoints = sut.smooth(points);
        assertThat(smoothedPoints, hasSize(5));

        assertThat(smoothedPoints.get(0).getElevation(), is(100));
        assertThat(smoothedPoints.get(1).getElevation(), is(110));
        assertThat(smoothedPoints.get(2).getElevation(), is(115));
        assertThat(smoothedPoints.get(3).getElevation(), is(117));
        assertThat(smoothedPoints.get(4).getElevation(), is(114));
    }


    @Test
    void smoothPointsExceedingWindowCapacity() {

        List<Point3D> points = new ArrayList<>();
        points.add(new Point3D(100, valueOf(49.00000), valueOf(8.00000))); // the first point -> +1 to window
        points.add(new Point3D(900, valueOf(49.00000), valueOf(8.00020))); // distance: 14.59 -> +1 to window
        points.add(new Point3D(900, valueOf(49.00000), valueOf(8.00275))); // distance: 186.02 -> +18 to window

        // adding another point exceeds the maximum sliding window size
        points.add(new Point3D(900, valueOf(49.00000), valueOf(8.00295))); // distance: 14.59 -> +1 to window

        List<Point3D> smoothedPoints = sut.smooth(points);
        assertThat(smoothedPoints, hasSize(4));

        // first points elevation moved out of window, thus not having any influence on 4th points elevation
        assertThat(smoothedPoints.get(3).getElevation(), is(900));
    }
}
