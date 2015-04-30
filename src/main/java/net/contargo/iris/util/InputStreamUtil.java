
package net.contargo.iris.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ClassPathResource;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * This is a helper class for actions concerning {@link java.io.InputStream}s.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public final class InputStreamUtil {

    private static final Logger LOG = LoggerFactory.getLogger(InputStreamUtil.class);

    private InputStreamUtil() {
    }

    /**
     * Gets the file with the given path plus file name of the classpath and converts it to an InputStream.
     *
     * @param  fileName
     *
     * @return  InputStream
     */
    public static InputStream getFileInputStream(String fileName) {

        InputStream in = null;

        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            File file = resource.getFile();
            in = new FileInputStream(file);
        } catch (IOException ex) {
            LOG.warn("File '" + fileName + "' could not be converted to InputStream.", ex);
        }

        return in;
    }


    /**
     * Converts the given InputStream to String.
     *
     * @param  in  InputStream
     *
     * @return  String content of the InputStream
     */
    public static String convertInputStreamToString(InputStream in) throws IOException {

        StringBuilder builder = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String line = reader.readLine();

        while (line != null) {
            builder.append(line);
            line = reader.readLine();
        }

        return builder.toString();
    }


    /**
     * Converts {@link java.io.InputStream} to {@link org.w3c.dom.Document}.
     *
     * @param  in  InputStream
     *
     * @return  Document
     */
    public static Document getDocument(InputStream in) {

        Document doc = null;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder = null;

        try {
            docBuilder = factory.newDocumentBuilder();

            doc = docBuilder.parse(in);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOG.warn("Error fetching xml document.", ex);
        }

        return doc;
    }
}
