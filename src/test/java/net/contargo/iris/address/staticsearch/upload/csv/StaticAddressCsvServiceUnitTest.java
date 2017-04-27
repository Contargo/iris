package net.contargo.iris.address.staticsearch.upload.csv;

import net.contargo.iris.address.staticsearch.upload.file.StaticAddressFileService;

import org.apache.commons.io.IOUtils;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticAddressCsvServiceUnitTest {

    @Mock
    private StaticAddressFileService fileServiceMock;

    private StaticAddressCsvService sut;

    @Before
    public void setUp() {

        sut = new StaticAddressCsvService(fileServiceMock);
    }


    @Test
    public void parseStaticAddressImportFile() throws IOException {

        String csvData = "postalcode;city;country\n76135;Karlsruhe;de";
        InputStream is = new ByteArrayInputStream(csvData.getBytes(Charset.forName("ISO-8859-1")));
        when(fileServiceMock.read("addresses.csv")).thenReturn(is);

        List<StaticAddressImportRecord> importRecords = sut.parseStaticAddressImportFile("addresses.csv");

        assertThat(importRecords, hasSize(1));
        assertThat(importRecords.get(0).getPostalCode(), is("76135"));
        assertThat(importRecords.get(0).getCity(), is("Karlsruhe"));
    }


    @Test
    public void generateCsvReport() throws IOException {

        StaticAddressErrorRecord errorRecord = new StaticAddressErrorRecord("76135", "Karlsruhe", "de", "error");
        List<StaticAddressErrorRecord> errorRecords = singletonList(errorRecord);

        InputStream inputStream = sut.generateCsvReport(errorRecords);
        String csv = IOUtils.toString(inputStream, Charset.forName("ISO-8859-1"));
        assertThat(csv, containsString("postalcode;city;country;error"));
        assertThat(csv, containsString("76135;Karlsruhe;de;error"));
    }
}
