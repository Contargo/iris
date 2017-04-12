package net.contargo.iris.address.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import net.contargo.iris.address.service.BestMatch;
import net.contargo.iris.address.service.BestMatchService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * @author  David Schilling - schilling@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 * @author  Ben Antony - antony@synyx.de
 */
@Controller
@Api(description = "API for querying one best matched address.", value = "")
public class BestMatchApiController {

    private final BestMatchService bestMatchService;

    @Autowired
    public BestMatchApiController(BestMatchService bestMatchService) {

        this.bestMatchService = bestMatchService;
    }

    @ApiOperation(
        value = "Returns a single best matched address according to the query.",
        notes = "When no static address is found, a nominatim resolved address will be returned."
    )
    @RequestMapping(value = "/addresses/bestmatch", method = GET)
    public ResponseEntity<BestMatch> bestMatch(@RequestParam(value = "postalcode") String postalCode,
        @RequestParam(value = "city") String city,
        @RequestParam(value = "countrycode") String countryCode) {

        Optional<BestMatch> optional = bestMatchService.bestMatch(postalCode, city, countryCode);

        return optional.map(bestMatch -> new ResponseEntity<>(bestMatch, OK)).orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
