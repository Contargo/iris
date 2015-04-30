package net.contargo.iris.terminal.dto;

import java.math.BigInteger;

import java.util.List;


/**
 * View Bean Service for mapping of {@link net.contargo.iris.terminal.Terminal}s to {@link TerminalDto}s, Maps all
 * return values of the methods of TerminalService to View Beans. For detailed Javadoc see interface
 * {@link net.contargo.iris.terminal.service.TerminalService}
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface TerminalDtoService {

    /**
     * @return  a List of all Terminals
     */
    List<TerminalDto> getAll();


    /**
     * @return  a List of alle enabled Terminals.
     */
    List<TerminalDto> getAllActive();


    /**
     * Save the Terminal.
     *
     * @param  terminalDto
     *
     * @return  The terminalDto constructed from the saved Entity.
     */
    TerminalDto save(TerminalDto terminalDto);


    /**
     * @param  uid  the {@link net.contargo.iris.terminal.Terminal}'s uniqueId
     *
     * @return  The Terminal found to the given id. Null if no terminal is found.
     */
    TerminalDto getByUid(BigInteger uid);


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
     * @param  terminalDto  the dto providing values that should be updated
     */
    TerminalDto updateTerminal(BigInteger terminalUid, TerminalDto terminalDto);
}
