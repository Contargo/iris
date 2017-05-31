package net.contargo.iris.routedatarevision.web;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteDataRevisionCleanupRequest {

    @Email
    @NotBlank
    private String email;

    public RouteDataRevisionCleanupRequest() {
    }


    public RouteDataRevisionCleanupRequest(String email) {

        this.email = email;
    }

    public String getEmail() {

        return email;
    }


    public void setEmail(String email) {

        this.email = email;
    }
}
