package net.contargo.iris.routedatarevision.web;

import org.hibernate.validator.constraints.Email;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class RouteDataRevisionCleanupRequest {

    @Email
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
