package net.contargo.iris.address.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.AddressList;

import org.springframework.cache.Cache;

import java.math.BigDecimal;

import java.util.List;


/**
 * Wraps a Cache to save resolved Address instances.
 *
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 */
class AddressCache {

    private final Cache cache;

    public AddressCache(Cache cache) {

        this.cache = cache;
    }

    /**
     * Caches all the Addresses.
     *
     * @param  addressListList  A {@link java.util.List} of {@link net.contargo.iris.address.AddressList}
     */
    public void cache(List<AddressList> addressListList) {

        for (AddressList list : addressListList) {
            for (Address address : list.getAddresses()) {
                cache.put(getAddressLocationBasedHash(address), address);
            }
        }
    }


    /**
     * Returns the Address-Instance for the given {@link GeoLocation} if it exists in the cache - null otherwise.
     *
     * @param  loc  Eventually cached {@link GeoLocation}
     *
     * @return  cached {@link net.contargo.iris.address.Address} from given {@link net.contargo.iris.GeoLocation}
     */
    public Address getForLocation(GeoLocation loc) {

        Cache.ValueWrapper valWrapper = cache.get(getAddressLocationBasedHash(loc));

        if (valWrapper == null) {
            return null;
        } else {
            return (Address) valWrapper.get();
        }
    }


    private String getAddressLocationBasedHash(GeoLocation address) {

        BigDecimal latitude = new BigDecimal(-1);

        if (address.getLatitude() != null) {
            latitude = address.getLatitude();
        }

        BigDecimal longitude = new BigDecimal(-1);

        if (address.getLongitude() != null) {
            longitude = address.getLongitude();
        }

        return latitude + ":" + longitude;
    }
}
