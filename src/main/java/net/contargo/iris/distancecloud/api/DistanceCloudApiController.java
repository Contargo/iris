package net.contargo.iris.distancecloud.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.distancecloud.dto.DistanceCloudAddressDto;
import net.contargo.iris.distancecloud.dto.DistanceCloudAddressDtoService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@Api(value = "", description = "API for calculating cloud-distances.")
@Controller
public class DistanceCloudApiController {

    private final DistanceCloudAddressDtoService distanceCloudAddressDtoService;

    @Autowired
    public DistanceCloudApiController(DistanceCloudAddressDtoService distanceCloudAddressDtoService) {

        this.distanceCloudAddressDtoService = distanceCloudAddressDtoService;
    }

    @ApiOperation(
        notes = "Calculates the cloud-distance between the given terminal and static address.",
        value = "Calculates the cloud-distance between the given terminal and static address."
    )
    @RequestMapping(value = "distancecloudaddress", method = RequestMethod.GET)
    @ResponseBody
    public DistanceCloudAddressResponse cloudAddress(@RequestParam("terminal") BigInteger terminalUid,
        @RequestParam("address") BigInteger staticAddressUid) {

        DistanceCloudAddressDto address = distanceCloudAddressDtoService.getAddressInCloud(terminalUid,
                staticAddressUid);

        DistanceCloudAddressResponse distanceCloudAddressResponse = new DistanceCloudAddressResponse(address);

        distanceCloudAddressResponse.add(linkTo(methodOn(getClass()).cloudAddress(terminalUid, staticAddressUid))
            .withSelfRel());

        return distanceCloudAddressResponse;
    }
}
