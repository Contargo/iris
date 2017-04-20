package net.contargo.iris.address.staticsearch.upload;

import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportException;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportJobService;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportService;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Executes an import of static addresses when scheduled by cron.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class StaticAddressImportTask {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final StaticAddressImportJobService jobService;
    private final StaticAddressImportService importService;

    public StaticAddressImportTask(StaticAddressImportJobService jobService,
        StaticAddressImportService importService) {

        this.jobService = jobService;
        this.importService = importService;
    }

    public void processNextJob() {

        LOG.debug("Checking for static address import jobs");

        Optional<StaticAddressImportJob> job = jobService.getTopmostJob();

        job.ifPresent(j -> {
            LOG.info("Submitting new import job for file '{}'", j.getCsvPath());

            try {
                importService.importAddresses(j);
                LOG.info("Deleting job for file '{}'", j.getCsvPath());
                jobService.deleteJob(j);
                LOG.info("Finished processing job for file '{}'", j.getCsvPath());
            } catch (StaticAddressImportException e) {
                LOG.error("Something went wrong during static address import, not deleting job with id {}", j.getId(),
                    e);
            }
        });
    }
}
