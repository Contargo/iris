package net.contargo.iris.address.staticsearch.upload.csv;

import org.apache.commons.io.IOUtils;

import org.hamcrest.Matchers;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class StaticAddressCsvServiceIntegrationTest {

    private StaticAddressCsvService sut;

    private String directory = System.getProperty("java.io.tmpdir");

    @Before
    public void setUp() {

        sut = new StaticAddressCsvService(directory);
    }


    @Test
    public void parseStaticAddressImportFile() throws IOException {

        String csvFile = createCsv();

        List<StaticAddressImportRecord> importRecords = sut.parseStaticAddressImportFile(csvFile);

        assertThat(importRecords, Matchers.hasSize(1));
        assertThat(importRecords.get(0).getPostalCode(), is("76135"));
        assertThat(importRecords.get(0).getCity(), is("Karlsruhe"));
    }


    private String createCsv() throws IOException {

        String csv = "postalcode;city;\n76135;Karlsruhe;";
        Path csvFilePath = Files.createTempFile("addresses", ".csv");
        IOUtils.write(csv, Files.newOutputStream(csvFilePath), Charset.forName("ISO-8859-1"));

        return csvFilePath.getFileName().toString();
    }


    @Test
    public void generateCsvReport() throws IOException {

        List<StaticAddressErrorRecord> errorRecords = singletonList(new StaticAddressErrorRecord("76135", "Karlsruhe",
                    "error"));
        InputStream inputStream = sut.generateCsvReport(errorRecords);
        String csv = IOUtils.toString(inputStream, Charset.forName("ISO-8859-1"));
        assertThat(csv, containsString("postalcode;city;error"));
        assertThat(csv, containsString("76135;Karlsruhe;error"));
    }
}
