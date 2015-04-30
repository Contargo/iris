package net.contargo.iris.seaport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.seaport.Seaport;

import java.math.BigInteger;

import java.util.List;


/**
 * Provides services related to {@link Seaport} entities.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface SeaportService {

    /**
     * Returns all (active/inactive) {@link Seaport}s of the System.
     *
     * @return  a List of all {@link Seaport}s
     */
    List<Seaport> getAll();


    /**
     * Returns all active {@link Seaport}s of the System.
     *
     * @return  a List of active {@link Seaport}s
     */
    List<Seaport> getAllActive();


    /**
     * Returns the {@link Seaport} based on the given ID or null if not found.
     *
     * @param  id  the id to return a {@link Seaport} for
     *
     * @return  the {@link Seaport} found or null
     */
    Seaport getById(Long id);


    Seaport getByUniqueId(BigInteger uniqueId);


    /**
     * Saves a {@link Seaport} to database..
     *
     * @param  seaport  to save
     *
     * @return  the persisted {@link Seaport}
     */
    Seaport save(Seaport seaport);


    /**
     * Returns a {@link Seaport} by its latitude and longitude.
     *
     * @param  location  credentials to find {@link Seaport} with
     *
     * @return  {@link Seaport} with given {@link GeoLocation}
     */
    Seaport getByGeoLocation(GeoLocation location);


    boolean existsByUniqueId(BigInteger seaportUid);


    Seaport updateSeaport(BigInteger seaportUid, Seaport seaport);
}
