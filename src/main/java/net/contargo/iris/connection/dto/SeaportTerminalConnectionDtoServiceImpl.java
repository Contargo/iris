package net.contargo.iris.connection.dto;

import net.contargo.iris.connection.service.SeaportTerminalConnectionService;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of {@link SeaportTerminalConnectionDtoService}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SeaportTerminalConnectionDtoServiceImpl implements SeaportTerminalConnectionDtoService {

    private final SeaportTerminalConnectionService seaportTerminalConnectionService;

    public SeaportTerminalConnectionDtoServiceImpl(SeaportTerminalConnectionService seaportTerminalConnectionService) {

        this.seaportTerminalConnectionService = seaportTerminalConnectionService;
    }

    @Override
    public List<SeaportDto> findSeaportsConnectedByRouteType(RouteType type) {

        List<Seaport> seaports = seaportTerminalConnectionService.findSeaPortsConnectedByRouteType(type);

        List<SeaportDto> dtos = new ArrayList<>();

        for (Seaport seaport : seaports) {
            dtos.add(new SeaportDto(seaport));
        }

        return dtos;
    }


    @Override
    public List<TerminalDto> findTerminalsConnectedToSeaPortByRouteType(SeaportDto seaportDto, RouteType routeType) {

        Seaport seaport = seaportDto.toEntity();

        List<Terminal> terminals = seaportTerminalConnectionService.getTerminalsConnectedToSeaPortByRouteType(seaport,
                routeType);

        List<TerminalDto> dtos = new ArrayList<>();

        for (Terminal terminal : terminals) {
            dtos.add(new TerminalDto(terminal));
        }

        return dtos;
    }
}
