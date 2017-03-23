package net.contargo.iris.address.api;

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
 */
@Controller
public class BestMatchApiController {

    private final BestMatchService bestMatchService;

    @Autowired
    public BestMatchApiController(BestMatchService bestMatchService) {

        this.bestMatchService = bestMatchService;
    }

    @RequestMapping(value = "/addresses/bestmatch", method = GET)
    public ResponseEntity<BestMatch> bestMatch(@RequestParam(value = "postalcode") String postalCode,
        @RequestParam(value = "city") String city,
        @RequestParam(value = "countrycode") String countryCode) {

        Optional<BestMatch> bestMatch = bestMatchService.bestMatch(postalCode, city, countryCode);

        if (bestMatch.isPresent()) {
            return new ResponseEntity<>(bestMatch.get(), OK);
        }

        return new ResponseEntity<>(NOT_FOUND);
    }
}
