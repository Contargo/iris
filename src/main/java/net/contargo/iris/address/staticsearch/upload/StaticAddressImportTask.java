package net.contargo.iris.address.staticsearch.upload;

import net.contargo.iris.address.staticsearch.upload.file.StaticAddressFileService;
import net.contargo.iris.address.staticsearch.upload.service.AddressMailService;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportException;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportJobService;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportReport;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportService;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * Executes an import of static addresses when scheduled by cron.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class StaticAddressImportTask {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final StaticAddressImportJobService jobService;
    private final StaticAddressImportService importService;
    private final StaticAddressFileService fileService;
    private final AddressMailService addressMailService;

    public StaticAddressImportTask(StaticAddressImportJobService jobService, StaticAddressImportService importService,
        StaticAddressFileService fileService, AddressMailService addressMailService) {

        this.jobService = jobService;
        this.importService = importService;
        this.fileService = fileService;
        this.addressMailService = addressMailService;
    }

    public void processNextJob() {

        LOG.debug("Checking for static address import jobs");

        Optional<StaticAddressImportJob> job = jobService.getTopmostJob();

        job.ifPresent(j -> {
            String file = j.getCsvPath();
            LOG.info("Submitting new import job for file '{}'", file);

            try {
                StaticAddressImportReport report = importService.importAddresses(j);
                LOG.info("Finished processing job for file '{}'", file);
                addressMailService.sendSuccessMail(j.getEmail(), j.getCsvPath(), report.getData());
            } catch (StaticAddressImportException e) {
                LOG.error("Something went wrong during static address import", file, e);
                addressMailService.sendErrorMail(j.getEmail(), file);
            } finally {
                LOG.info("Deleting job for file '{}'", file);
                jobService.deleteJob(j);
                fileService.delete(file);
            }
        });
    }
}
