package net.contargo.iris.routedatarevision.service.cleanup;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.lang.invoke.MethodHandles;

import java.nio.charset.Charset;

import java.util.List;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
class RouteDataRevisionCsvService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String ID = "id";
    private static final String TERMINAL = "terminal";
    private static final String POSTAL_CODE = "postalcode";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String COMMENT = "comment";
    private static final String REVISION_TRUCK = "revisionTruckDistance";
    private static final String REVISION_TOLL = "revisionTollDistance";
    private static final String CURRENT_TRUCK = "currentTruckDistance";
    private static final String CURRENT_TOLL = "currentTollDistance";

    InputStream generateCsvReport(List<RouteDataRevisionCleanupRecord> records) {

        LOG.debug("Generating csv report with {} obsolete records", records.size());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try(CSVPrinter csvPrinter = createCsvPrinter(stream)) {
            csvPrinter.printRecord(ID, TERMINAL, POSTAL_CODE, CITY, COUNTRY, REVISION_TRUCK, REVISION_TOLL,
                CURRENT_TRUCK, CURRENT_TOLL, COMMENT);

            for (RouteDataRevisionCleanupRecord r : records) {
                csvPrinter.printRecord(r.getId(), r.getTerminal(), r.getPostalCode(), r.getCity(), r.getCountry(),
                    r.getRevisionTruckDistance(), r.getRevisionTollDistance(), r.getCurrentTruckDistance(),
                    r.getCurrentTollDistance(), r.getComment());
            }
        } catch (IOException e) {
            throw new RouteDataRevisionCsvException("Csv could not be generated", e);
        }

        return new ByteArrayInputStream(stream.toByteArray());
    }


    private static CSVPrinter createCsvPrinter(OutputStream outputStream) throws IOException {

        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(';').withEscape('\\').withRecordSeparator('\r');

        return new CSVPrinter(new OutputStreamWriter(outputStream, Charset.forName("ISO-8859-1")), csvFormat);
    }
}
