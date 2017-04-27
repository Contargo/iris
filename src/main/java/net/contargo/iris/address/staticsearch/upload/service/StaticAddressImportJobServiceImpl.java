package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;
import net.contargo.iris.address.staticsearch.upload.persistence.StaticAddressImportJobRepository;

import org.slf4j.Logger;

import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@Transactional
public class StaticAddressImportJobServiceImpl implements StaticAddressImportJobService {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final StaticAddressImportJobRepository repository;

    public StaticAddressImportJobServiceImpl(StaticAddressImportJobRepository repository) {

        this.repository = repository;
    }

    @Override
    public void addJob(StaticAddressImportJob job) {

        LOG.debug("Adding new static address import job for {} ({})", job.getEmail(), job.getCsvPath());

        repository.save(job);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<StaticAddressImportJob> getTopmostJob() {

        return repository.findFirstByOrderByIdAsc();
    }


    @Override
    public void deleteJob(StaticAddressImportJob job) {

        LOG.debug("Deleting static address import job for {} ({})", job.getEmail(), job.getCsvPath());

        repository.delete(job);
    }
}
