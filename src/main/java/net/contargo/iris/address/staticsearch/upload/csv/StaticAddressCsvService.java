package net.contargo.iris.address.staticsearch.upload.csv;

import net.contargo.iris.address.staticsearch.upload.file.StaticAddressFileService;

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

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

import static java.util.stream.Collectors.toList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class StaticAddressCsvService {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private static final String POSTAL_CODE = "postalcode";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String ERROR = "error";

    private final StaticAddressFileService fileService;

    public StaticAddressCsvService(StaticAddressFileService fileService) {

        this.fileService = fileService;
    }

    public List<StaticAddressImportRecord> parseStaticAddressImportFile(String csvFileName) {

        LOG.debug("Parsing import CSV");

        try(CSVParser csvReader = createCsvReader(fileService.read(csvFileName))) {
            return csvReader.getRecords()
                .stream()
                .map(r -> new StaticAddressImportRecord(r.get(POSTAL_CODE), r.get(CITY), r.get(COUNTRY)))
                .collect(toList());
        } catch (IOException | IllegalArgumentException e) {
            throw new StaticAddressCsvException("Error while attempting to read from import CSV", e);
        }
    }


    public InputStream generateCsvReport(List<StaticAddressErrorRecord> records) {

        LOG.debug("Generating csv report with {} error records", records.size());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try(CSVPrinter csvPrinter = createCsvPrinter(stream)) {
            csvPrinter.printRecord(POSTAL_CODE, CITY, COUNTRY, ERROR);

            for (StaticAddressErrorRecord r : records) {
                csvPrinter.printRecord(r.getPostalCode(), r.getCity(), r.getCountry(), r.getError());
            }
        } catch (IOException e) {
            throw new StaticAddressCsvException("Error while attempting to generate csv report", e);
        }

        return new ByteArrayInputStream(stream.toByteArray());
    }


    private static CSVParser createCsvReader(InputStream csvInputStream) throws IOException {

        return new CSVParser(new InputStreamReader(csvInputStream, Charset.forName("ISO-8859-1")),
                CSVFormat.DEFAULT.withEscape('\\').withDelimiter(';').withSkipHeaderRecord().withHeader());
    }


    private static CSVPrinter createCsvPrinter(OutputStream outputStream) throws IOException {

        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(';').withEscape('\\').withRecordSeparator('\r');

        return new CSVPrinter(new OutputStreamWriter(outputStream, Charset.forName("ISO-8859-1")), csvFormat);
    }
}
