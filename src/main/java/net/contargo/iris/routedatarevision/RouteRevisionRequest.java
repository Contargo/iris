package net.contargo.iris.routedatarevision;

import org.springframework.util.StringUtils;

import java.util.stream.Stream;

import static org.springframework.util.StringUtils.trimWhitespace;


/**
 * Dto object that contains all relevant parameters for searching
 * {@link net.contargo.iris.routedatarevision.RouteDataRevision}s.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteRevisionRequest {

    private static final int LEAST_VALID_PARAMETER_COUNT = 1;
    private static final int PARAMETER_COUNT = 3;

    private String postalcode;
    private String city;
    private Long terminalId;

    public boolean isValid() {

        return Stream.of(postalcode, city, terminalId).filter(value -> value != null).count()
            >= LEAST_VALID_PARAMETER_COUNT;
    }


    public boolean isEmpty() {

        return Stream.of(postalcode, city, terminalId).filter(value -> value == null).count() == PARAMETER_COUNT;
    }


    public void setPostalcode(String postalcode) {

        if (!StringUtils.isEmpty(trimWhitespace(postalcode))) {
            this.postalcode = postalcode;
        } else {
            this.postalcode = null;
        }
    }


    public void setCity(String city) {

        if (!StringUtils.isEmpty(trimWhitespace(city))) {
            this.city = city;
        } else {
            this.city = null;
        }
    }


    public void setTerminalId(Long terminalId) {

        this.terminalId = terminalId;
    }


    public String getPostalcode() {

        return postalcode;
    }


    public String getCity() {

        return city;
    }


    public Long getTerminalId() {

        return terminalId;
    }
}
