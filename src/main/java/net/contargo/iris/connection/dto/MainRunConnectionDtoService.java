package net.contargo.iris.connection.dto;

import java.math.BigInteger;

import java.util.List;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public interface MainRunConnectionDtoService {

    List<MainRunConnectionDto> getConnectionsForTerminal(BigInteger terminalUID);
}
