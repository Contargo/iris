package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressErrorRecord;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressImportRecord;

import java.util.List;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface StaticAddressResolverService {

    /**
     * Matches each {@code importRecord} to a {@link net.contargo.iris.address.staticsearch.StaticAddress} and persists
     * it. If an address is unresolvable or it's already known an {@link StaticAddressErrorRecord} is created.
     *
     * @param  importRecords  list containing a {@link StaticAddressImportRecord} for each address that should be
     *                        imported
     *
     * @return  a list of all {@code StaticAddressErrorRecords} that have been created, never {@code null}
     */
    List<StaticAddressErrorRecord> resolveAddresses(List<StaticAddressImportRecord> importRecords);
}
