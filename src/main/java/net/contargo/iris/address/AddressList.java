package net.contargo.iris.address;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;
import java.util.Optional;


/**
 * Represents a list of addresses. Contains a list of {@link Address}es and a parent Address.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
public class AddressList {

    private final List<Address> addresses;

    private Address parentAddress;

    public AddressList(Address root, List<Address> addresses) {

        this.parentAddress = root;
        this.addresses = addresses;
    }


    public AddressList(String name, List<Address> addresses) {

        Address address = new Address();
        address.setDisplayName(name);

        this.parentAddress = address;
        this.addresses = addresses;
    }

    public List<Address> getAddresses() {

        return addresses;
    }


    public Optional<Address> firstAddress() {

        return addresses.stream().findFirst();
    }


    public Address getParentAddress() {

        return parentAddress;
    }


    public void setParentAddress(Address parentAddress) {

        this.parentAddress = parentAddress;
    }


    @Override
    public int hashCode() {

        return new HashCodeBuilder().append(addresses).append(parentAddress).toHashCode();
    }


    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof AddressList)) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        final AddressList other = (AddressList) obj;

        return new EqualsBuilder().append(this.hashCode(), other.hashCode())
            .append(this.addresses, other.addresses)
            .append(this.parentAddress, other.parentAddress)
            .isEquals();
    }
}
