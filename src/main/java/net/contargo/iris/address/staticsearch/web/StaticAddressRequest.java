package net.contargo.iris.address.staticsearch.web;

import org.springframework.util.StringUtils;


/**
 * View bean to encapsulate search parameter for a static address.
 *
 * @author  Michael Herbold - herbold@synyx.de
 */
class StaticAddressRequest {

    private String postalcode = null;
    private String city = null;

    public String getPostalcode() {

        if (null != postalcode && postalcode.isEmpty()) {
            postalcode = null;
        }

        return postalcode;
    }


    public void setPostalcode(String postalcode) {

        this.postalcode = postalcode;
    }


    public String getCity() {

        if (null != city && city.isEmpty()) {
            city = null;
        }

        return city;
    }


    public void setCity(String city) {

        this.city = city;
    }


    public boolean isEmpty() {

        return StringUtils.isEmpty(postalcode) && StringUtils.isEmpty(city);
    }
}
