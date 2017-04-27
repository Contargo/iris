package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public interface StaticAddressImportService {

    StaticAddressImportReport importAddresses(StaticAddressImportJob job);
}
