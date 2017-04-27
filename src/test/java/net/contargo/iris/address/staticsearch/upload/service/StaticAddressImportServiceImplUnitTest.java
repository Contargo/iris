package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressCsvService;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressErrorRecord;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressImportRecord;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;

import java.util.List;

import static org.mockito.Mockito.when;

import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticAddressImportServiceImplUnitTest {

    private StaticAddressImportServiceImpl sut;

    @Mock
    private StaticAddressCsvService csvServiceMock;
    @Mock
    private StaticAddressResolverService matchServiceMock;
    @Mock
    private AddressMailService mailServiceMock;

    @Before
    public void setUp() {

        sut = new StaticAddressImportServiceImpl(csvServiceMock, matchServiceMock);
    }


    @Test
    public void importAddresses() {

        StaticAddressImportJob job = new StaticAddressImportJob("user@example.com", "addresses.csv");

        List<StaticAddressImportRecord> importRecords = singletonList(new StaticAddressImportRecord("76135",
                    "Karlsruhe", "de"));
        List<StaticAddressErrorRecord> errors = singletonList(new StaticAddressErrorRecord("76135", "Karlsruhe", "de",
                    "error"));
        ByteArrayInputStream csvReport = new ByteArrayInputStream("example".getBytes());

        when(csvServiceMock.parseStaticAddressImportFile("addresses.csv")).thenReturn(importRecords);
        when(matchServiceMock.resolveAddresses(importRecords)).thenReturn(errors);
        when(csvServiceMock.generateCsvReport(errors)).thenReturn(csvReport);

        sut.importAddresses(job);
    }
}
