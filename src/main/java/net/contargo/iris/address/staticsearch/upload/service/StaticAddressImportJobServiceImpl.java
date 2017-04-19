package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;
import net.contargo.iris.address.staticsearch.upload.persistence.StaticAddressImportJobRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Transactional
public class StaticAddressImportJobServiceImpl implements StaticAddressImportJobService {

    private final StaticAddressImportJobRepository repository;
    private final StaticAddressFileService fileService;

    public StaticAddressImportJobServiceImpl(StaticAddressImportJobRepository repository,
        StaticAddressFileService fileService) {

        this.repository = repository;
        this.fileService = fileService;
    }

    @Override
    public void addJob(StaticAddressImportJob job) {

        repository.save(job);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<StaticAddressImportJob> getTopmostJob() {

        return repository.findFirstByOrderByIdAsc();
    }


    @Override
    public void deleteJob(StaticAddressImportJob job) {

        repository.delete(job);
        fileService.delete(job.getCsvPath());
    }
}
