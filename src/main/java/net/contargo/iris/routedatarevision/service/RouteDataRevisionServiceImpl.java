package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.service.AddressServiceWrapper;
import net.contargo.iris.api.NotFoundException;
import net.contargo.iris.normalizer.NormalizerService;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.RouteRevisionRequest;
import net.contargo.iris.routedatarevision.ValidityRange;
import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDto;
import net.contargo.iris.routedatarevision.persistence.RouteDataRevisionRepository;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import org.slf4j.Logger;

import org.springframework.data.jpa.domain.Specifications;

import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static net.contargo.iris.routedatarevision.service.RouteRevisionSpecifications.hasCity;
import static net.contargo.iris.routedatarevision.service.RouteRevisionSpecifications.hasPostalCode;
import static net.contargo.iris.routedatarevision.service.RouteRevisionSpecifications.hasTerminal;

import static org.slf4j.LoggerFactory.getLogger;

import static org.springframework.data.jpa.domain.Specifications.where;


@Transactional
public class RouteDataRevisionServiceImpl implements RouteDataRevisionService {

    private static final Logger LOG = getLogger(MethodHandles.lookup().lookupClass());
    private static final int LOG_INTERVAL = 100;

    private final RouteDataRevisionRepository routeDataRevisionRepository;
    private final TerminalService terminalService;
    private final AddressServiceWrapper addressServiceWrapper;
    private final NormalizerService normalizerService;

    public RouteDataRevisionServiceImpl(RouteDataRevisionRepository routeDataRevisionRepository,
        TerminalService terminalService, AddressServiceWrapper addressServiceWrapper,
        NormalizerService normalizerService) {

        this.routeDataRevisionRepository = routeDataRevisionRepository;
        this.terminalService = terminalService;
        this.addressServiceWrapper = addressServiceWrapper;
        this.normalizerService = normalizerService;
    }

    @Override
    @Transactional(readOnly = true)
    public RouteDataRevision getRouteDataRevision(Terminal terminal, GeoLocation destination) {

        return routeDataRevisionRepository.findNearest(terminal, destination.getLatitude(), destination.getLongitude(),
                new Date());
    }


    @Override
    @Transactional(readOnly = true)
    public RouteDataRevision getRouteDataRevision(BigInteger terminalUid, GeoLocation destination, Date date) {

        Terminal terminal = terminalService.getByUniqueId(terminalUid);

        if (terminal == null) {
            throw new RevisionDoesNotExistException("Terminal with uid " + terminalUid + "  does not exist",
                "terminal.error.notfound");
        }

        Date actualDate = date == null ? new Date() : date;

        RouteDataRevision nearest = routeDataRevisionRepository.findNearest(terminal, destination.getLatitude(),
                destination.getLongitude(), actualDate);

        if (nearest == null) {
            throw new RevisionDoesNotExistException("Route revision for terminal with uid " + terminalUid
                + ", coordinates " + destination.getLatitude() + "," + destination.getLongitude() + " and date: "
                + new SimpleDateFormat(RouteDataRevisionDto.DATE_FORMAT, Locale.getDefault()).format(actualDate)
                + " does not exist", "routerevision.notfound");
        }

        return nearest;
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<RouteDataRevision> getRouteDataRevision(BigInteger terminalUid, GeoLocation destination) {

        Terminal terminal = terminalService.getByUniqueId(terminalUid);

        if (terminal == null) {
            return Optional.empty();
        }

        RouteDataRevision nearest = routeDataRevisionRepository.findNearest(terminal, destination.getLatitude(),
                destination.getLongitude(), new Date());

        return Optional.ofNullable(nearest);
    }


    @Override
    public List<RouteDataRevision> getRouteDataRevisions() {

        return routeDataRevisionRepository.findAll();
    }


    @Override
    public List<RouteDataRevision> getRouteDataRevisions(Long terminalId) {

        return routeDataRevisionRepository.findByTerminalId(terminalId);
    }


    @Override
    public RouteDataRevision getRouteDataRevision(Long id) {

        RouteDataRevision routeDataRevision = routeDataRevisionRepository.findOne(id);

        if (routeDataRevision == null) {
            throw new NotFoundException("Cannot find route data revision for id " + id);
        }

        return routeDataRevision;
    }


    @Override
    public RouteDataRevision save(RouteDataRevision routeDataRevision) {

        GeoLocation geoLocation = new GeoLocation(routeDataRevision.getLatitude(), routeDataRevision.getLongitude());
        Address address = addressServiceWrapper.getAddressForGeoLocation(geoLocation);

        String countryCode = address.getCountryCode();
        String combinedCity = address.getCombinedCity();
        String postcode = address.getPostcode();

        try {
            routeDataRevision.setCountry(countryCode);
            routeDataRevision.setCity(combinedCity);
            routeDataRevision.setCityNormalized(normalizerService.normalize(combinedCity));
            routeDataRevision.setPostalCode(postcode);
        } catch (IllegalArgumentException e) {
            LOG.info("Invalid Address Information: city: {}, postalcode: {}, country: {}", combinedCity, postcode,
                countryCode);
        }

        RouteDataRevision save = routeDataRevisionRepository.save(routeDataRevision);

        return getRouteDataRevision(save.getId());
    }


    @Override
    public boolean overlapsWithExisting(BigInteger terminalUniqueId, BigDecimal latitude, BigDecimal longitude,
        ValidityRange validityRange, Long routeRevisionId) {

        List<RouteDataRevision> revisions = routeDataRevisionRepository.findByTerminalAndLatitudeAndLongitude(
                terminalUniqueId, latitude, longitude);

        return revisions.stream().filter(revision -> !revision.getId().equals(routeRevisionId)).anyMatch(revision ->
                    validityRange.overlapWith(revision.getValidityRange()));
    }


    @Override
    public List<RouteDataRevision> search(RouteRevisionRequest routeRevisionRequest) {

        String normalizedCity = normalizerService.normalize(routeRevisionRequest.getCity());
        String postalcode = routeRevisionRequest.getPostalcode();
        Long terminalId = routeRevisionRequest.getTerminalId();

        Specifications<RouteDataRevision> spec = where(hasCity(normalizedCity)).and(hasPostalCode(postalcode))
                .and(hasTerminal(terminalId));

        return routeDataRevisionRepository.findAll(spec);
    }


    @Override
    public void enrichWithAddressInformation() {

        List<RouteDataRevision> revisions = routeDataRevisionRepository.findByCityIsNullAndPostalCodeIsNull();

        long start = System.currentTimeMillis();

        LOG.info("Enriching {} route revisions with address information", revisions.size());

        for (int i = 0; i < revisions.size(); i++) {
            if (i % LOG_INTERVAL == 0) {
                LOG.info("Enriching route revisions with address information progress: {}/{}", i, revisions.size());
            }

            save(revisions.get(i));
        }

        LOG.info("Done enriching route revisions with address information (took {} ms)",
            System.currentTimeMillis() - start);
    }
}
