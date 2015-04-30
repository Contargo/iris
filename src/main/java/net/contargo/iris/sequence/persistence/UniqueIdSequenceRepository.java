package net.contargo.iris.sequence.persistence;

import net.contargo.iris.sequence.UniqueIdSequence;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
public interface UniqueIdSequenceRepository extends JpaRepository<UniqueIdSequence, Long> {

    UniqueIdSequence findByEntityName(String entityName);
}
