package net.contargo.iris.terminal.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.sequence.service.SequenceService;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.persistence.TerminalRepository;

import org.slf4j.Logger;

import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;

import java.math.BigInteger;

import java.util.List;

import javax.persistence.Entity;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Provides implementations related to {@link Terminal} entities.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Transactional
public class TerminalServiceImpl implements TerminalService {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final TerminalRepository terminalRepository;
    private final SequenceService uniqueIdSequenceService;

    public TerminalServiceImpl(TerminalRepository terminalRepository, SequenceService uniqueIdSequenceService) {

        this.terminalRepository = terminalRepository;
        this.uniqueIdSequenceService = uniqueIdSequenceService;
    }

    @Override
    public List<Terminal> getAll() {

        return terminalRepository.findAll();
    }


    @Override
    public List<Terminal> getAllActive() {

        return terminalRepository.findByEnabled(true);
    }


    @Override
    public synchronized Terminal save(Terminal terminal) {

        if (terminal.getUniqueId() == null) {
            terminal.setUniqueId(determineUniqueId());
        }

        checkUniqueConstraints(terminal);

        return terminalRepository.save(terminal);
    }


    BigInteger determineUniqueId() {

        String entityName = Terminal.class.getAnnotation(Entity.class).name();
        BigInteger nextUniqueId = uniqueIdSequenceService.getNextId(entityName);

        boolean isUniqueIdAlreadyAssigned = terminalRepository.findByUniqueId(nextUniqueId) != null;

        while (isUniqueIdAlreadyAssigned) {
            // In this loop we increment the ID by ourselves to avoid write-accesses to the DB for performance.
            LOG.warn("Terminal uniqueId {} already assigned - trying next uniqueId", nextUniqueId);
            nextUniqueId = nextUniqueId.add(BigInteger.ONE);

            if (terminalRepository.findByUniqueId(nextUniqueId) == null) {
                isUniqueIdAlreadyAssigned = false;
                uniqueIdSequenceService.setNextId(entityName, nextUniqueId);
            }
        }

        return nextUniqueId;
    }


    @Override
    public Terminal getById(Long id) {

        return terminalRepository.findOne(id);
    }


    @Override
    public Terminal getByUniqueId(BigInteger uniqueId) {

        return terminalRepository.findByUniqueId(uniqueId);
    }


    @Override
    public Terminal getByGeoLocation(GeoLocation location) {

        return terminalRepository.findByLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
    }


    @Override
    public boolean existsByUniqueId(BigInteger uniqueId) {

        return getByUniqueId(uniqueId) != null;
    }


    @Override
    public Terminal updateTerminal(BigInteger terminalUid, Terminal terminal) {

        Terminal savedTerminal = getByUniqueId(terminalUid);
        savedTerminal.setEnabled(terminal.isEnabled());
        savedTerminal.setName(terminal.getName());
        savedTerminal.setLatitude(terminal.getLatitude());
        savedTerminal.setLongitude(terminal.getLongitude());
        savedTerminal.setRegion(terminal.getRegion());

        return save(savedTerminal);
    }


    @Transactional(readOnly = true)
    void checkUniqueConstraints(Terminal terminal) {

        boolean uniqueCoordinates;
        boolean uniqueName;

        if (terminal.getId() == null) {
            // create
            uniqueCoordinates = terminalRepository.findByLatitudeAndLongitude(terminal.getLatitude(),
                    terminal.getLongitude()) == null;

            uniqueName = terminalRepository.findByName(terminal.getName()) == null;
        } else {
            // update
            Long terminalId = terminal.getId();

            uniqueCoordinates = terminalRepository.findByLatitudeAndLongitudeAndIdNot(terminal.getLatitude(),
                    terminal.getLongitude(), terminalId) == null;

            uniqueName = terminalRepository.findByNameAndIdNot(terminal.getName(), terminalId) == null;
        }

        if (!uniqueCoordinates) {
            throw new NonUniqueTerminalException("latitude", "longitude");
        }

        if (!uniqueName) {
            throw new NonUniqueTerminalException("name");
        }
    }
}
