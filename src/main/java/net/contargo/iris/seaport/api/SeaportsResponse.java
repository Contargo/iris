package net.contargo.iris.seaport.api;

import net.contargo.iris.seaport.dto.SeaportDto;

import org.springframework.hateoas.ResourceSupport;

import java.util.Set;


/**
 * HATEOAS supporting response object for a list of {@link net.contargo.iris.seaport.dto.SeaportDto}s.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
class SeaportsResponse extends ResourceSupport {

    private Set<SeaportDto> seaports;

    public Set<SeaportDto> getSeaports() {

        return seaports;
    }


    public void setSeaports(Set<SeaportDto> seaports) {

        this.seaports = seaports;
    }
}
