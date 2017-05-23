package net.contargo.iris.routedatarevision.service.cleanup;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.truck.TruckRoute;

import org.apache.commons.io.IOUtils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.containsString;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

import static java.util.Collections.singletonList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteDataRevisionCsvServiceUnitTest {

    private RouteDataRevisionCsvService sut;

    @Before
    public void setUp() {

        sut = new RouteDataRevisionCsvService();
    }


    @Test
    public void generateCsvReport() throws IOException {

        Terminal terminal = new Terminal();
        terminal.setName("Terminal");

        RouteDataRevision revision = new RouteDataRevision();
        revision.setId(42L);
        revision.setTerminal(terminal);
        revision.setPostalCode("76135");
        revision.setCity("Karlsruhe");
        revision.setCountry("DE");
        revision.setComment("Automatically imported");
        revision.setTruckDistanceOneWayInKilometer(ONE);
        revision.setTollDistanceOneWayInKilometer(ZERO);

        TruckRoute truckRoute = new TruckRoute(TEN, ZERO, null);

        InputStream inputStream = sut.generateCsvReport(singletonList(
                    new RouteDataRevisionCleanupRecord(revision, truckRoute)));

        String csv = IOUtils.toString(inputStream, Charset.forName("ISO-8859-1"));
        assertThat(csv,
            containsString(
                "id;terminal;postalcode;city;country;revisionTruckDistance;"
                + "revisionTollDistance;currentTruckDistance;currentTollDistance;comment"));
        assertThat(csv, containsString("42;Terminal;76135;Karlsruhe;DE;1;0;10;0;Automatically imported"));
    }
}
