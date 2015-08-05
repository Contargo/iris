package net.contargo.iris.connection.service;

import net.contargo.iris.connection.AbstractSubConnection;
import net.contargo.iris.connection.MainRunConnection;
import net.contargo.iris.connection.SeaportSubConnection;
import net.contargo.iris.connection.TerminalSubConnection;
import net.contargo.iris.connection.persistence.MainRunConnectionRepository;
import net.contargo.iris.route.RouteType;
import net.contargo.iris.route.SubRoutePart;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.Assert;

import java.math.BigInteger;

import java.util.List;


/**
 * Service for operations with the {@link net.contargo.iris.connection.MainRunConnection}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@Transactional
public class MainRunConnectionServiceImpl implements MainRunConnectionService {

    private final MainRunConnectionRepository mainRunConnectionRepository;
    private final SeaportService seaportService;
    private final TerminalService terminalService;
    private final BargeRailConnectionFinderService bargeRailConnectionFinderService;

    public MainRunConnectionServiceImpl(MainRunConnectionRepository mainRunConnectionRepository,
        SeaportService seaportService, TerminalService terminalService,
        BargeRailConnectionFinderService bargeRailConnectionFinderService) {

        this.mainRunConnectionRepository = mainRunConnectionRepository;
        this.seaportService = seaportService;
        this.terminalService = terminalService;
        this.bargeRailConnectionFinderService = bargeRailConnectionFinderService;
    }

    @Override
    public List<MainRunConnection> getAll() {

        return mainRunConnectionRepository.findAll();
    }


    @Override
    public List<MainRunConnection> getAllActive() {

        return mainRunConnectionRepository.findByEnabled(true);
    }


    @Override
    public MainRunConnection getById(Long id) {

        return mainRunConnectionRepository.findOne(id);
    }


    @Override
    public MainRunConnection save(MainRunConnection mainrunConnection) {

        mainrunConnection.setSeaport(seaportService.getByUniqueId(mainrunConnection.getSeaport().getUniqueId()));
        mainrunConnection.setTerminal(terminalService.getByUniqueId(mainrunConnection.getTerminal().getUniqueId()));

        if (combinationExists(mainrunConnection) && mainrunConnection.getRouteType() != RouteType.BARGE_RAIL) {
            throw new DuplicateMainRunConnectionException();
        }

        for (AbstractSubConnection subConnection : mainrunConnection.getSubConnections()) {
            subConnection.setTerminal(terminalService.getByUniqueId(subConnection.getTerminal().getUniqueId()));

            if (subConnection instanceof SeaportSubConnection) {
                ((SeaportSubConnection) subConnection).setSeaport(seaportService.getByUniqueId(
                        ((SeaportSubConnection) subConnection).getSeaport().getUniqueId()));
            } else {
                ((TerminalSubConnection) subConnection).setTerminal2(terminalService.getByUniqueId(
                        ((TerminalSubConnection) subConnection).getTerminal2().getUniqueId()));
            }
        }

        return mainRunConnectionRepository.save(mainrunConnection);
    }


    @Override
    @Transactional(readOnly = true)
    public MainRunConnection findRoutingConnectionBetweenTerminalAndSeaportByType(Terminal terminal, Seaport seaport,
        RouteType routeType, List<SubRoutePart> subRouteParts) {

        Assert.notNull(terminal);
        Assert.notNull(seaport);
        Assert.notNull(routeType);

        List<MainRunConnection> connections =
            mainRunConnectionRepository.findByTerminalAndSeaportAndRouteTypeAndEnabled(terminal, seaport, routeType,
                true);

        if (connections.isEmpty()) {
            return null;
        }

        if (routeType == RouteType.BARGE_RAIL) {
            return bargeRailConnectionFinderService.findMatchingBargeRailConnection(connections, subRouteParts);
        } else {
            return connections.get(0);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<MainRunConnection> getConnectionsForTerminal(BigInteger terminalUID) {

        return mainRunConnectionRepository.findConnectionsByTerminalUniqueId(terminalUID);
    }


    private boolean combinationExists(MainRunConnection connection) {

        boolean exists;

        if (connection.getId() == null) {
            exists = mainRunConnectionRepository.existsBySeaportAndTerminalAndRouteType(connection.getSeaport()
                    .getId(), connection.getTerminal().getId(), connection.getRouteType());
        } else {
            exists = mainRunConnectionRepository.existsBySeaportAndTerminalAndRouteTypeAndIdNot(connection.getSeaport()
                    .getId(), connection.getTerminal().getId(), connection.getRouteType(), connection.getId());
        }

        return exists;
    }
}
