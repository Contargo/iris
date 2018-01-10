package net.contargo.iris.address.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.AddressDtoService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Controller
@Api(description = "API for querying addresses.", value = "")
public class SimpleAddressApiController {

    private final AddressDtoService nominatimAddressService;

    @Autowired
    public SimpleAddressApiController(AddressDtoService nominatimAddressService) {

        this.nominatimAddressService = nominatimAddressService;
    }

    @ApiOperation(
        value = "Returns a list of matching addresses.",
        notes = "Can be static addresses or nomniatim resolved addresses."
    )
    @RequestMapping(value = "/addresses/simpleaddress", method = GET)
    public List<AddressDto> getAddresses(@RequestParam("query") String query) {

        return nominatimAddressService.getAddressesByQuery(query);
    }
}
