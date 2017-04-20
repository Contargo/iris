package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.Address;
import net.contargo.iris.address.nominatim.service.NominatimAddressService;
import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressCoordinatesDuplicationException;
import net.contargo.iris.address.staticsearch.service.StaticAddressDuplicationException;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressErrorRecord;
import net.contargo.iris.address.staticsearch.upload.csv.StaticAddressImportRecord;

import org.slf4j.Logger;

import java.lang.invoke.MethodHandles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class StaticAddressResolverServiceImpl implements StaticAddressResolverService {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());

    private final StaticAddressService staticAddressService;
    private final NominatimAddressService nominatimAddressService;

    public StaticAddressResolverServiceImpl(StaticAddressService staticAddressService,
        NominatimAddressService nominatimAddressService) {

        this.staticAddressService = staticAddressService;
        this.nominatimAddressService = nominatimAddressService;
    }

    @Override
    public List<StaticAddressErrorRecord> resolveAddresses(List<StaticAddressImportRecord> importRecords) {

        LOG.debug("Trying to resolve {} addresses", importRecords.size());

        List<StaticAddressErrorRecord> errors = new ArrayList<>();

        importRecords.forEach(r -> {
            List<Address> resolvedAddresses = nominatimAddressService.getAddressesByDetails(r.toAddressDetails());

            if (resolvedAddresses.isEmpty()) {
                errors.add(new StaticAddressErrorRecord(r.getPostalCode(), r.getCity(), "unresolvable address"));
            } else {
                StaticAddress staticAddress = toStaticAddress(resolvedAddresses.get(0));
                LOG.debug("Resolved {} to {}", r.toAddressDetails(), staticAddress);
                persistAddress(staticAddress, r).ifPresent(errors::add);
            }
        });

        return errors;
    }


    private Optional<StaticAddressErrorRecord> persistAddress(StaticAddress staticAddress,
        StaticAddressImportRecord importRecord) {

        if (staticAddress.getPostalcode() == null) {
            staticAddress.setPostalcode(importRecord.getPostalCode());
        }

        if (staticAddress.getCity() == null) {
            staticAddress.setCity(importRecord.getCity());
        }

        try {
            staticAddressService.saveStaticAddress(staticAddress);
        } catch (StaticAddressDuplicationException e) {
            String cityAndPostalcode = "(" + staticAddress.getPostalcode() + " " + staticAddress.getCity() + ")";

            return Optional.of(new StaticAddressErrorRecord(importRecord.getPostalCode(), importRecord.getCity(),
                        "address with same city and postalcode already exists " + cityAndPostalcode));
        } catch (StaticAddressCoordinatesDuplicationException e) {
            String coordinates = "(" + staticAddress.getLatitude() + ", " + staticAddress.getLongitude() + " ["
                + staticAddress.getPostalcode() + " " + staticAddress.getCity() + "])";

            return Optional.of(new StaticAddressErrorRecord(importRecord.getPostalCode(), importRecord.getCity(),
                        "address with same coordinates already exists " + coordinates));
        }

        return Optional.empty();
    }


    private static StaticAddress toStaticAddress(Address address) {

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setCity(address.getCity());
        staticAddress.setCountry(address.getCountryCode());
        staticAddress.setPostalcode(address.getPostcode());
        staticAddress.setSuburb(address.getSuburb());
        staticAddress.setLatitude(address.getLatitude());
        staticAddress.setLongitude(address.getLongitude());

        return staticAddress;
    }
}
