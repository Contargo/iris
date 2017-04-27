package net.contargo.iris.address.staticsearch.upload.service;

import java.io.InputStream;


/**
 * @author  Oliver Messner - messner@synyx.de
 */
public class StaticAddressImportReport {

    private final InputStream data;

    public StaticAddressImportReport(InputStream data) {

        this.data = data;
    }

    public InputStream getData() {

        return data;
    }
}
