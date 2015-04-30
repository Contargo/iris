
package net.contargo.iris.util;

import org.hamcrest.Matchers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import static org.junit.Assert.assertThat;


/**
 * Unit test for {@link InputStreamUtil}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class InputStreamUtilUnitTest {

    private static final String SAMPLE_XML_FILE = "/openrouteservice/ors_geocoding_sample.xml";
    private InputStream in;

    @Before
    public void setup() {

        in = InputStreamUtil.getFileInputStream(SAMPLE_XML_FILE);
    }


    @After
    public void shutdown() throws IOException {

        in.close();
    }


    @Test
    public void testGetFileInputStream() {

        assertThat(in, notNullValue());
    }


    @Test(expected = RuntimeException.class)
    public void testGetFileInputStreamInvalidFileName() throws IOException {

        InputStream stream = InputStreamUtil.getFileInputStream("foo");

        assertThat(stream, Matchers.nullValue());

        stream.close();
    }


    @Test
    public void testConvertInputStreamToString() throws IOException {

        String result = InputStreamUtil.convertInputStreamToString(in);

        assertThat(result, notNullValue());
        assertThat(result, containsString("7.2931383 51.1526207"));
    }


    @Test
    public void testGetDocument() {

        Document doc = InputStreamUtil.getDocument(in);

        assertThat(doc, notNullValue());

        NodeList nodes = doc.getElementsByTagName("xls:GeocodeResponseList");
        assertThat(nodes.getLength(), equalTo(1));

        Node node = nodes.item(0);

        String numberOfAddresses = node.getAttributes().item(0).getNodeValue();

        assertThat(numberOfAddresses, notNullValue());
        assertThat(numberOfAddresses, equalTo("4"));
    }
}
