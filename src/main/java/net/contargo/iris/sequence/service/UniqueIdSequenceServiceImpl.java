package net.contargo.iris.sequence.service;

import net.contargo.iris.sequence.UniqueIdSequence;
import net.contargo.iris.sequence.persistence.UniqueIdSequenceRepository;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;


/**
 * Manages sequences of Unique IDs in the UniqueIdSequence table.
 *
 * @author  Arnold Franke - franke@synyx.de
 */
@Transactional
public class UniqueIdSequenceServiceImpl implements SequenceService {

    private final UniqueIdSequenceRepository uniqueIdSequenceRepository;

    public UniqueIdSequenceServiceImpl(UniqueIdSequenceRepository uniqueIdSequenceRepository) {

        this.uniqueIdSequenceRepository = uniqueIdSequenceRepository;
    }

    @Override
    public synchronized BigInteger getNextId(String entityName) {

        UniqueIdSequence uniqueIdSequence = uniqueIdSequenceRepository.findByEntityName(entityName);

        if (uniqueIdSequence == null) {
            throw new UniqueIdSequenceServiceException("No uniqueId sequence found in the database.");
        }

        BigInteger nextUniqueId = uniqueIdSequence.getNextId();

        incrementNextId(uniqueIdSequence);

        return nextUniqueId;
    }


    private void incrementNextId(UniqueIdSequence uniqueIdSequence) {

        uniqueIdSequence.incrementNextId();

        checkValidity(uniqueIdSequence.getNextId());

        uniqueIdSequenceRepository.save(uniqueIdSequence);
    }


    void checkValidity(BigInteger nextId) {

        if (!nextId.toString().startsWith(UniqueIdSequence.SYSTEM_UNIQUEID_PREFIX)
                || nextId.toString().length() != UniqueIdSequence.SYSTEM_UNIQUEID_LENGTH) {
            throw new UniqueIdSequenceServiceException(
                "The automatically generated uniqueId is not valid for this system. It has to be 16 digits long and "
                + "has to start with the system identifier " + UniqueIdSequence.SYSTEM_UNIQUEID_PREFIX);
        }
    }


    @Override
    public void setNextId(String entityName, BigInteger id) {

        UniqueIdSequence uniqueIdSequence = uniqueIdSequenceRepository.findByEntityName(entityName);
        uniqueIdSequence.setNextId(id);
        checkValidity(uniqueIdSequence.getNextId());
        uniqueIdSequenceRepository.save(uniqueIdSequence);
    }
}
