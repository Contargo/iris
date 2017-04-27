package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressCsvException;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressCsvService;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressErrorRecord;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressImportRecord;

import java.io.InputStream;

import java.util.List;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class StaticAddressImportServiceImpl implements StaticAddressImportService {

    private final StaticAddressCsvService csvService;
    private final StaticAddressResolverService resolverService;

    public StaticAddressImportServiceImpl(StaticAddressCsvService csvService,
        StaticAddressResolverService resolverService) {

        this.csvService = csvService;
        this.resolverService = resolverService;
    }

    @Override
    public StaticAddressImportReport importAddresses(StaticAddressImportJob job) {

        try {
            List<StaticAddressImportRecord> importRecords = csvService.parseStaticAddressImportFile(job.getCsvPath());
            List<StaticAddressErrorRecord> errors = resolverService.resolveAddresses(importRecords);
            InputStream data = csvService.generateCsvReport(errors);

            return new StaticAddressImportReport(data);
        } catch (StaticAddressCsvException e) {
            throw new StaticAddressImportException(e.getMessage(), e);
        }
    }
}
