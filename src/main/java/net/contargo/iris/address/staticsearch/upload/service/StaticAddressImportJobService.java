package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;

import java.util.Optional;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface StaticAddressImportJobService {

    void addJob(StaticAddressImportJob job);


    Optional<StaticAddressImportJob> getTopmostJob();


    void deleteJob(StaticAddressImportJob job);
}
