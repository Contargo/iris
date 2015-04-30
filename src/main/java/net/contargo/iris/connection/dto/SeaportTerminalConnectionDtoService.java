package net.contargo.iris.connection.dto;

import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.terminal.dto.TerminalDto;

import java.util.List;


/**
 * Dto service layer for {@link net.contargo.iris.connection.service.SeaportTerminalConnectionService}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface SeaportTerminalConnectionDtoService {

    /**
     * Finds all {@link net.contargo.iris.seaport.Seaport}s that have a
     * {@link net.contargo.iris.connection.MainRunConnection} of type {@code type}.
     *
     * @param  type  the {@link net.contargo.iris.connection.MainRunConnection}'s
     *               {@link net.contargo.iris.route.RouteType}
     *
     * @return  all matching {@link net.contargo.iris.seaport.Seaport}s as {@link SeaportDto}s
     */
    List<SeaportDto> findSeaportsConnectedByRouteType(RouteType type);


    /**
     * Finds all {@link net.contargo.iris.terminal.Terminal}s that are part of a
     * {@link net.contargo.iris.connection.MainRunConnection} with the given {@link RouteType} and the specified
     * {@link net.contargo.iris.seaport.Seaport} property.
     *
     * @param  seaport  the {@link net.contargo.iris.connection.MainRunConnection}'s
     *                  {@link net.contargo.iris.seaport.Seaport}
     * @param  routeType  the {@link net.contargo.iris.connection.MainRunConnection}'s {@link RouteType}
     *
     * @return  all matching {@link net.contargo.iris.terminal.Terminal}s as {@link TerminalDto}s
     */
    List<TerminalDto> findTerminalsConnectedToSeaPortByRouteType(SeaportDto seaport, RouteType routeType);
}
