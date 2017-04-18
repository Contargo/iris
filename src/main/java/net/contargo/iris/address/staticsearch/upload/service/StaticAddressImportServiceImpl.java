package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressCsvService;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressErrorRecord;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressImportRecord;

import java.io.InputStream;

import java.util.List;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class StaticAddressImportServiceImpl implements StaticAddressImportService {

    private final StaticAddressCsvService csvService;
    private final StaticAddressResolverService resolverService;
    private final AddressMailService addressMailService;

    public StaticAddressImportServiceImpl(StaticAddressCsvService csvService,
        StaticAddressResolverService resolverService, AddressMailService addressMailService) {

        this.csvService = csvService;
        this.resolverService = resolverService;
        this.addressMailService = addressMailService;
    }

    @Override
    public void importAddresses(StaticAddressImportJob job) {

        List<StaticAddressImportRecord> importRecords = csvService.parseStaticAddressImportFile(job.getCsvPath());

        List<StaticAddressErrorRecord> errors = resolverService.resolveAddresses(importRecords);

        InputStream csv = csvService.generateCsvReport(errors);

        addressMailService.send(job.getEmail(), job.getCsvPath(), csv);
    }
}
