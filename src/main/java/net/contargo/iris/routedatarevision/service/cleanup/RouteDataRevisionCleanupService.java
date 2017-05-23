package net.contargo.iris.routedatarevision.service.cleanup;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.mail.service.EmailService;
import net.contargo.iris.rounding.RoundingService;
import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.persistence.RouteDataRevisionRepository;
import net.contargo.iris.routedatarevision.web.RouteDataRevisionCleanupRequest;
import net.contargo.iris.truck.TruckRoute;
import net.contargo.iris.truck.service.TruckRouteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

import java.lang.invoke.MethodHandles;

import java.math.BigDecimal;
import java.math.MathContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import static java.util.stream.Collectors.toList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@Transactional
public class RouteDataRevisionCleanupService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final MathContext MATH_CONTEXT = new MathContext(0);

    private final RouteDataRevisionRepository routeDataRevisionRepository;
    private final TruckRouteService truckRouteService;
    private final RoundingService roundingService;
    private final EmailService emailService;
    private final RouteDataRevisionCsvService csvService;

    public RouteDataRevisionCleanupService(RouteDataRevisionRepository routeDataRevisionRepository,
        TruckRouteService truckRouteService, RoundingService roundingService, EmailService emailService,
        RouteDataRevisionCsvService csvService) {

        this.routeDataRevisionRepository = routeDataRevisionRepository;
        this.truckRouteService = truckRouteService;
        this.roundingService = roundingService;
        this.emailService = emailService;
        this.csvService = csvService;
    }

    public void cleanup(RouteDataRevisionCleanupRequest cleanupRequest) {

        List<RouteDataRevisionCleanupRecord> obsoleteRevisions = findObsoleteRevisions();

        Map<String, String> data = new HashMap<>();
        data.put("username", cleanupRequest.getEmail());
        data.put("numberOfRevisions", String.valueOf(obsoleteRevisions.size()));

        try {
            InputStream csvReport = csvService.generateCsvReport(obsoleteRevisions);
            emailService.sendWithAttachment(cleanupRequest.getEmail(), "Route revision - Cleanup report",
                "routerevision-cleanup.ftl", data, csvReport, "routerevision-cleanup.csv");
        } catch (RouteDataRevisionCsvException e) {
            emailService.send(cleanupRequest.getEmail(), "Route revision - Cleanup report - Error",
                "routerevision-cleanup-error.ftl", data);
        }
    }


    private List<RouteDataRevisionCleanupRecord> findObsoleteRevisions() {

        int i = 0;

        Date today = new Date();

        List<RouteDataRevisionCleanupRecord> obsoleteRevisions = new ArrayList<>();

        Page<RouteDataRevision> page;

        while ((page = routeDataRevisionRepository.findByValidNow(today, new PageRequest(i, 100))).hasContent()) {
            obsoleteRevisions.addAll(page.getContent()
                .stream()
                .map(this::identifyObsoleteRevision)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList()));

            i++;
        }

        return obsoleteRevisions;
    }


    Optional<RouteDataRevisionCleanupRecord> identifyObsoleteRevision(RouteDataRevision revision) {

        if ("ch".equalsIgnoreCase(revision.getCountry())) {
            return Optional.empty();
        }

        GeoLocation terminalLocation = new GeoLocation(revision.getTerminal().getLatitude(),
                revision.getTerminal().getLongitude());
        GeoLocation revisionLocation = new GeoLocation(revision.getLatitude(), revision.getLongitude());
        TruckRoute route = truckRouteService.route(terminalLocation, revisionLocation);

        if (!isStaffelsprung(revision, route)) {
            return Optional.of(new RouteDataRevisionCleanupRecord(revision, route));
        }

        return Optional.empty();
    }


    boolean isStaffelsprung(RouteDataRevision revision, TruckRoute route) {

        BigDecimal revisionDistance = revision.getTruckDistanceOneWayInKilometer().compareTo(ZERO) != 0
            ? revision.getTruckDistanceOneWayInKilometer().subtract(ONE)
            : revision.getTruckDistanceOneWayInKilometer();

        BigDecimal routeDistance = roundingService.roundDistance(route.getDistance());

        BigDecimal currentDistance = routeDistance.compareTo(ZERO) != 0 ? routeDistance.subtract(ONE) : routeDistance;

        BigDecimal revisionDivider = revisionDistance.divideToIntegralValue(BigDecimal.valueOf(10L), MATH_CONTEXT);
        BigDecimal currentDivider = currentDistance.divideToIntegralValue(BigDecimal.valueOf(10L), MATH_CONTEXT);

        return revisionDivider.compareTo(currentDivider) != 0;
    }
}
