package net.contargo.iris.transport.elevation.smoothing;

import net.contargo.iris.transport.elevation.dto.Point3D;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class ElevationSmoother {

    private static final int WINDOW_SIZE = 20;
    private static final double MIN_DISTANCE = 10;

    public List<Point3D> smooth(List<Point3D> points) {

        final int numberOfPoints = points.size();

        if (numberOfPoints <= 2) {
            return points;
        }

        List<Point3D> smoothedPoints = new ArrayList<>();
        LinkedList<Double> values = new LinkedList<>();

        double x0, y0, z0;
        double x1, y1, z1;
        double eleSum = 0;

        x0 = points.get(0).getLongitude().doubleValue();
        y0 = points.get(0).getLatitude().doubleValue();
        z0 = points.get(0).getElevation();

        eleSum += z0;
        values.addLast(z0);
        smoothedPoints.add(new Point3D((int) z0, BigDecimal.valueOf(y0), BigDecimal.valueOf(x0)));

        for (int i = 1; i < numberOfPoints; i++) {
            x1 = points.get(i).getLongitude().doubleValue();
            y1 = points.get(i).getLatitude().doubleValue();
            z1 = points.get(i).getElevation();

            double dist = DistanceCalculationUtil.calculateDistance(y0, x0, y1, x1);

            if (dist > MIN_DISTANCE) {
                int n = (int) Math.ceil(dist / MIN_DISTANCE);

                for (int j = 1; j < n; j++) {
                    double ele = z0 + j * (z1 - z0) / ((double) (n - 1));

                    if (values.size() == WINDOW_SIZE) {
                        eleSum -= values.getFirst();
                        values.removeFirst();
                    }

                    eleSum += ele;
                    values.addLast(ele);
                }
            } else {
                if (values.size() == WINDOW_SIZE) {
                    eleSum -= values.getFirst();
                    values.removeFirst();
                }

                eleSum += z1;
                values.addLast(z1);
            }

            int ele = (int) Math.round(eleSum / values.size());
            smoothedPoints.add(new Point3D(ele, BigDecimal.valueOf(y1), BigDecimal.valueOf(x1)));

            x0 = x1;
            y0 = y1;
            z0 = z1;
        }

        return smoothedPoints;
    }
}
