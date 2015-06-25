package net.contargo.iris.connection.dto;

import java.math.BigInteger;

import java.util.List;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public interface MainRunConnectionDtoService {

    List<SimpleMainRunConnectionDto> getConnectionsForTerminal(BigInteger terminalUID);


    MainRunConnectionDto get(Long id);


    void save(MainRunConnectionDto dto);
}
