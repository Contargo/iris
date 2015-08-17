package net.contargo.iris.connection.dto;

import java.math.BigInteger;

import java.util.List;


/**
 * Services that provides handling of {@link MainRunConnectionDto}s and {@link SimpleMainRunConnectionDto}s.
 *
 * @author  Oliver Messner - messner@synyx.de
 */
public interface MainRunConnectionDtoService {

    /**
     * Finds all {@link SimpleMainRunConnectionDto}s that are connected to a {@link net.contargo.iris.terminal.Terminal}
     * with the given uid.
     *
     * @param  terminalUID  a {@link net.contargo.iris.terminal.Terminal}'s uid
     *
     * @return  a list of matching {@link SimpleMainRunConnectionDto}s
     */
    List<SimpleMainRunConnectionDto> getConnectionsForTerminal(BigInteger terminalUID);


    /**
     * Finds a {@link MainRunConnectionDto}.
     *
     * @param  id  the {@link MainRunConnectionDto}'s id
     *
     * @return  the {@link MainRunConnectionDto} with the specified {@code id}
     */
    MainRunConnectionDto get(Long id);


    /**
     * Saves a {@link MainRunConnectionDto}.
     *
     * @param  dto  the {@link MainRunConnectionDto} to be saved
     *
     * @return  the saved {@link MainRunConnectionDto} that does not have a null id property
     */
    MainRunConnectionDto save(MainRunConnectionDto dto);
}
