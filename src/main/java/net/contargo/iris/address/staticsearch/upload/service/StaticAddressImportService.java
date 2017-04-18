package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface StaticAddressImportService {

    void importAddresses(StaticAddressImportJob job);
}
