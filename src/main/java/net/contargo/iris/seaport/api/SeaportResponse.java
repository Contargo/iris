package net.contargo.iris.seaport.api;

import net.contargo.iris.seaport.dto.SeaportDto;

import org.springframework.hateoas.ResourceSupport;


/**
 * HATEOAS supporting response object for a single {@link net.contargo.iris.seaport.dto.SeaportDto}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
class SeaportResponse extends ResourceSupport {

    private SeaportDto seaport;

    public SeaportDto getSeaport() {

        return seaport;
    }


    public void setSeaport(SeaportDto seaport) {

        this.seaport = seaport;
    }
}
