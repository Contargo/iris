package net.contargo.iris.address.staticsearch.upload.csv;

import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.lang.invoke.MethodHandles;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

import static java.util.stream.Collectors.toList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class StaticAddressCsvService {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private static final String POSTAL_CODE = "postalcode";
    private static final String CITY = "city";
    private static final String ERROR = "error";

    private final Path csvDirectory;

    public StaticAddressCsvService(String csvDirectory) {

        this.csvDirectory = Paths.get(csvDirectory);
    }

    public List<StaticAddressImportRecord> parseStaticAddressImportFile(String csvFileName) {

        Path path = csvDirectory.resolve(csvFileName);

        LOG.debug("Parsing csv file {}", path);

        try(InputStream s = Files.newInputStream(path);
                CSVParser csvReader = createCsvReader(s)) {
            return csvReader.getRecords().stream().map(r ->
                        new StaticAddressImportRecord(r.get(POSTAL_CODE), r.get(CITY))).collect(toList());
        } catch (IOException e) {
            throw new StaticAddressImportException("Error while attempting to read "
                + path.toAbsolutePath().toString(), e);
        }
    }


    public InputStream generateCsvReport(List<StaticAddressErrorRecord> records) {

        LOG.debug("Generating csv report with {} error records", records.size());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try(CSVPrinter csvPrinter = createCsvPrinter(stream)) {
            csvPrinter.printRecord(POSTAL_CODE, CITY, ERROR);

            for (StaticAddressErrorRecord r : records) {
                csvPrinter.printRecord(r.getPostalCode(), r.getCity(), r.getError());
            }
        } catch (IOException e) {
            throw new StaticAddressImportException("Error while attempting to generate csv report", e);
        }

        return new ByteArrayInputStream(stream.toByteArray());
    }


    private CSVParser createCsvReader(InputStream csvInputStream) throws IOException {

        return new CSVParser(new InputStreamReader(csvInputStream, Charset.forName("ISO-8859-1")),
                CSVFormat.DEFAULT.withEscape('\\').withDelimiter(';').withSkipHeaderRecord().withHeader());
    }


    private CSVPrinter createCsvPrinter(OutputStream outputStream) throws IOException {

        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(';').withEscape('\\').withRecordSeparator('\r');

        return new CSVPrinter(new OutputStreamWriter(outputStream, Charset.forName("ISO-8859-1")), csvFormat);
    }
}
