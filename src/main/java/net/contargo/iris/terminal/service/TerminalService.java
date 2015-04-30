package net.contargo.iris.terminal.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.terminal.Terminal;

import java.math.BigInteger;

import java.util.List;


/**
 * Provides services related to {@link Terminal} entities.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface TerminalService {

    /**
     * Returns all (active/inactive) {@link Terminal}s of the System.
     *
     * @return  a List of all {@link Terminal}s
     */
    List<Terminal> getAll();


    /**
     * Returns all active {@link Terminal}s of the System.
     *
     * @return  a List of active {@link Terminal}s
     */
    List<Terminal> getAllActive();


    /**
     * Saves a {@link Terminal}.
     *
     * @param  terminal  to save
     *
     * @return  Terminal the saved {@link Terminal}
     */
    Terminal save(Terminal terminal);


    /**
     * Returns a {@link Terminal} by its id or null if not found.
     *
     * @param  id  the id to search for
     *
     * @return  the {@link Terminal} with the corresponding id or null
     */
    Terminal getById(Long id);


    /**
     * Returns a {@link Terminal} by its uniqueId.
     *
     * @param  uniqueId  the uniqueId to search for
     *
     * @return  the {@link Terminal} with the corresponding id or null
     */
    Terminal getByUniqueId(BigInteger uniqueId);


    /**
     * Returns a {@link Terminal} by its latitude and longitude.
     *
     * @param  location  credentials to find terminal with
     *
     * @return  {@link Terminal} with given {@link GeoLocation}
     */
    Terminal getByGeoLocation(GeoLocation location);


    /**
     * Checks whether a {@link net.contargo.iris.terminal.Terminal} exists,
     *
     * @param  uniqueId  the terminal's uniqueId
     *
     * @return  true if the terminal exists, else false
     */
    boolean existsByUniqueId(BigInteger uniqueId);


    /**
     * Updates the {@link net.contargo.iris.terminal.Terminal} specified by its uniqueId with the values given in
     * {@code terminalDto}.
     *
     * @param  terminalUid  the uniqueId of the terminal that should be updated
     * @param  terminal  the dto providing values that should be updated
     */
    Terminal updateTerminal(BigInteger terminalUid, Terminal terminal);
}
