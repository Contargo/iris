package net.contargo.iris.gis.api;

import net.contargo.iris.gis.dto.AirlineDistanceDto;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


/**
 * {@link ResourceAssemblerSupport} that maps an {@link net.contargo.iris.gis.dto.AirlineDistanceDto} to an object of
 * type {@link AirlineDistanceResponse}.
 *
 * @author  Oliver Messner - messner@synyx.de
 */
class AirlineDistanceResponseAssembler extends ResourceAssemblerSupport<AirlineDistanceDto, AirlineDistanceResponse> {

    public AirlineDistanceResponseAssembler() {

        super(AirlineDistanceApiController.class, AirlineDistanceResponse.class);
    }

    @Override
    public AirlineDistanceResponse toResource(AirlineDistanceDto airlineDistanceDto) {

        AirlineDistanceResponse airlineDistanceResponse = new AirlineDistanceResponse(airlineDistanceDto);

        Link linkWithoutParams = linkTo(AirlineDistanceApiController.class).withSelfRel();

        // Did not find a way to attach ?-params properly with ControllerLinkBuilder
        Link linkWithParams = new Link(linkWithoutParams.getHref().concat(airlineDistanceDto.getScope()),
                linkWithoutParams.getRel());
        airlineDistanceResponse.add(linkWithParams);

        return airlineDistanceResponse;
    }
}
