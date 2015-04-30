package net.contargo.iris.seaport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.persistence.SeaportRepository;
import net.contargo.iris.sequence.service.SequenceService;

import org.slf4j.Logger;

import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;

import java.math.BigInteger;

import java.util.List;

import javax.persistence.Entity;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Provides implementations related to {@link Seaport} entities.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
@Transactional
public class SeaportServiceImpl implements SeaportService {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final SeaportRepository seaportRepository;
    private final SequenceService uniqueIdSequenceService;

    public SeaportServiceImpl(SeaportRepository seaportRepository, SequenceService uniqueIdSequenceService) {

        this.seaportRepository = seaportRepository;
        this.uniqueIdSequenceService = uniqueIdSequenceService;
    }

    @Override
    public List<Seaport> getAll() {

        return seaportRepository.findAll();
    }


    @Override
    public List<Seaport> getAllActive() {

        return seaportRepository.findByEnabled(true);
    }


    @Override
    public Seaport getById(Long id) {

        return seaportRepository.findOne(id);
    }


    @Override
    public Seaport getByUniqueId(BigInteger uniqueId) {

        return seaportRepository.findByUniqueId(uniqueId);
    }


    @Override
    public synchronized Seaport save(Seaport seaport) {

        if (seaport.getUniqueId() == null) {
            seaport.setUniqueId(determineUniqueId());
        }

        checkUniqueConstraints(seaport);

        return seaportRepository.save(seaport);
    }


    @Override
    public Seaport getByGeoLocation(GeoLocation location) {

        return seaportRepository.findByLatitudeAndLongitude(location.getLatitude(), location.getLongitude());
    }


    @Override
    public boolean existsByUniqueId(BigInteger seaportUid) {

        return getByUniqueId(seaportUid) != null;
    }


    @Override
    public Seaport updateSeaport(BigInteger seaportUid, Seaport seaport) {

        Seaport savedSeaport = getByUniqueId(seaportUid);
        savedSeaport.setEnabled(seaport.isEnabled());
        savedSeaport.setName(seaport.getName());
        savedSeaport.setLatitude(seaport.getLatitude());
        savedSeaport.setLongitude(seaport.getLongitude());

        return save(savedSeaport);
    }


    @Transactional(readOnly = true)
    void checkUniqueConstraints(Seaport seaport) {

        boolean uniqueCoordinates;
        boolean uniqueName;

        if (seaport.getId() == null) {
            // create
            uniqueCoordinates =
                seaportRepository.findByLatitudeAndLongitude(seaport.getLatitude(), seaport.getLongitude()) == null;

            uniqueName = seaportRepository.findByName(seaport.getName()) == null;
        } else {
            // update
            Long seaportId = seaport.getId();

            uniqueCoordinates = seaportRepository.findByLatitudeAndLongitudeAndIdNot(seaport.getLatitude(),
                    seaport.getLongitude(), seaportId) == null;

            uniqueName = seaportRepository.findByNameAndIdNot(seaport.getName(), seaportId) == null;
        }

        if (!uniqueCoordinates) {
            throw new NonUniqueSeaportException("latitude", "longitude");
        }

        if (!uniqueName) {
            throw new NonUniqueSeaportException("name");
        }
    }


    BigInteger determineUniqueId() {

        String entityName = Seaport.class.getAnnotation(Entity.class).name();
        BigInteger nextUniqueId = uniqueIdSequenceService.getNextId(entityName);

        boolean isUniqueIdAlreadyAssigned = seaportRepository.findByUniqueId(nextUniqueId) != null;

        while (isUniqueIdAlreadyAssigned) {
            // In this loop we increment the ID by ourselves to avoid write-accesses to the DB for performance.
            LOG.warn("Terminal uniqueId {} already assigned - trying next uniqueId", nextUniqueId);
            nextUniqueId = nextUniqueId.add(BigInteger.ONE);

            if (!(seaportRepository.findByUniqueId(nextUniqueId) != null)) {
                isUniqueIdAlreadyAssigned = false;
                uniqueIdSequenceService.setNextId(entityName, nextUniqueId);
            }
        }

        return nextUniqueId;
    }
}
