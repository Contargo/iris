package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.service.MainRunConnectionService;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class MainRunConnectionDtoServiceImpl implements MainRunConnectionDtoService {

    private final MainRunConnectionService mainRunConnectionService;

    public MainRunConnectionDtoServiceImpl(MainRunConnectionService mainRunConnectionService) {

        this.mainRunConnectionService = mainRunConnectionService;
    }

    @Override
    public List<SimpleMainRunConnectionDto> getConnectionsForTerminal(BigInteger terminalUID) {

        List<SimpleMainRunConnectionDto> connectionDtos = new ArrayList<>();

        for (MainRunConnection connection : mainRunConnectionService.getConnectionsForTerminal(terminalUID)) {
            connectionDtos.add(newDto(connection));
        }

        return connectionDtos;
    }


    @Override
    public MainRunConnectionDto get(Long id) {

        return new MainRunConnectionDto(mainRunConnectionService.getById(id));
    }


    @Override
    public MainRunConnectionDto save(MainRunConnectionDto dto) {

        return new MainRunConnectionDto(mainRunConnectionService.save(dto.toEntity()));
    }


    private SimpleMainRunConnectionDto newDto(MainRunConnection connection) {

        String seaportUid = connection.getSeaport().getUniqueId().toString();
        String terminalUid = connection.getTerminal().getUniqueId().toString();

        return new SimpleMainRunConnectionDto(seaportUid, terminalUid, connection.getRouteType());
    }
}
