package net.contargo.iris.gis.dto;

/**
 * Delegates service calls to the injected {@link GisService}.
 *
 * @author  Oliver Messner - messner@synyx.de
 */
public interface AirlineDistanceDtoService {

    /**
     * Calculates the airline distance between two geographical points (each represented by latitude and longitude) in
     * meters.
     *
     * @param  alat  latitude of point A
     * @param  alon  longitude of point A
     * @param  blat  latitude of point B
     * @param  blon  longitude of point B
     *
     * @return  an object of type {@link AirlineDistanceDto} representing the airline distance
     */
    AirlineDistanceDto calcAirLineDistInMeters(String alat, String alon, String blat, String blon);
}
