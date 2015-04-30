package net.contargo.iris.countries.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.api.AbstractController;
import net.contargo.iris.countries.dto.CountryDtoService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


/**
 * API Controller for Information about countries.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@Controller
@RequestMapping(value = CountriesApiController.COUNTRIES)
@Api(value = CountriesApiController.COUNTRIES, description = "API for Information about countries.")
public class CountriesApiController extends AbstractController {

    private final CountryDtoService countryDtoService;

    @Autowired
    public CountriesApiController(CountryDtoService countryDtoService) {

        this.countryDtoService = countryDtoService;
    }

    @ApiOperation(value = "Get all countries.", notes = "Get all countries.")
    @ModelAttribute("countriesResponse")
    @RequestMapping(method = RequestMethod.GET)
    public CountriesResponse countries() {

        CountriesResponse response = new CountriesResponse();
        response.setCountries(countryDtoService.getCountries());

        response.add(linkTo(getClass()).withSelfRel());

        return response;
    }
}
