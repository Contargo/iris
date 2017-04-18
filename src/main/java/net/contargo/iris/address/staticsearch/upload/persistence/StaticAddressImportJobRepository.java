package net.contargo.iris.address.staticsearch.upload.persistence;

import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface StaticAddressImportJobRepository extends JpaRepository<StaticAddressImportJob, Long> {

    Optional<StaticAddressImportJob> findFirstByOrderByIdAsc();
}
