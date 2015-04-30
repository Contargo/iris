package net.contargo.iris.api.discover;

import org.springframework.hateoas.ResourceSupport;


/**
 * Response Object which is used in the {@link DiscoverPublicApiController} to return the current version of the
 * application.
 *
 * @author  Marc Kannegiesser - kannegiesser@synyx.de
 * @author  David Schilling - schilling@synyx.de
 */
class DiscoverResponse extends ResourceSupport {

    private String version;

    public DiscoverResponse(String applicationVersion) {

        this.version = applicationVersion;
    }


    public DiscoverResponse() {

        // Needed for Jackson Mapping
    }

    public String getVersion() {

        return version;
    }


    public void setVersion(String version) {

        this.version = version;
    }
}
