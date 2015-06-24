package net.contargo.iris.connection.service;

import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.persistence.MainRunConnectionRepository;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import java.util.List;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SeaportTerminalConnectionServiceImpl implements SeaportTerminalConnectionService {

    private final MainRunConnectionRepository mainRunConnectionRepository;
    private final SeaportService seaportService;
    private final TerminalService terminalService;

    public SeaportTerminalConnectionServiceImpl(MainRunConnectionRepository mainRunConnectionRepository,
        SeaportService seaportService, TerminalService terminalService) {

        this.mainRunConnectionRepository = mainRunConnectionRepository;
        this.seaportService = seaportService;
        this.terminalService = terminalService;
    }

    /**
     * @see  SeaportTerminalConnectionService#findSeaPortsConnectedByRouteType(net.contargo.iris.route.RouteType)
     */
    @Override
    public List<Seaport> findSeaPortsConnectedByRouteType(RouteType type) {

        if (type.equals(RouteType.TRUCK)) {
            return seaportService.getAllActive();
        }

        return mainRunConnectionRepository.findSeaportsConnectedByRouteType(type);
    }


    /**
     * @see  SeaportTerminalConnectionService#getTerminalsConnectedToSeaPortByRouteType(Seaport, RouteType)
     */
    @Override
    public List<Terminal> getTerminalsConnectedToSeaPortByRouteType(Seaport seaPort, RouteType routeType) {

        if (routeType.equals(RouteType.TRUCK)) {
            return terminalService.getAllActive();
        }

        return mainRunConnectionRepository.getTerminalsConnectedToSeaPortByRouteType(seaPort.getUniqueId(), routeType);
    }


    @Override
    public List<MainRunConnection> getConnectionsToSeaPortByRouteType(Seaport seaport, RouteType routeType) {

        return mainRunConnectionRepository.findBySeaportAndRouteType(seaport.getUniqueId(), routeType);
    }
}
