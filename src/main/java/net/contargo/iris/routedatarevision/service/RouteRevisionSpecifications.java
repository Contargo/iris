package net.contargo.iris.routedatarevision.service;

import net.contargo.iris.routedatarevision.RouteDataRevision;
import net.contargo.iris.routedatarevision.RouteDataRevision_;
import net.contargo.iris.terminal.Terminal_;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;


/**
 * Utility class providing {@link Specification}s for the entity {@link RouteDataRevision}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public final class RouteRevisionSpecifications {

    private RouteRevisionSpecifications() {
    }

    public static Specification<RouteDataRevision> hasTerminal(Long terminalId) {

        return
            (root, query, builder) ->
                terminalId == null
                ? alwaysTrue(builder)
                : builder.equal(root.get(RouteDataRevision_.terminal).get(Terminal_.id), terminalId);
    }


    public static Specification<RouteDataRevision> hasPostalCode(String postalCode) {

        return
            (root, query, builder) ->
                postalCode == null ? alwaysTrue(builder)
                                   : builder.equal(root.get(RouteDataRevision_.postalCode), postalCode);
    }


    public static Specification<RouteDataRevision> hasCity(String city) {

        return
            (root, query, builder) ->
                city == null ? alwaysTrue(builder)
                             : builder.like(root.get(RouteDataRevision_.cityNormalized), "%" + city + "%");
    }


    static Specification<RouteDataRevision> isNotExpired() {

        return
            (root, query, builder) -> {
            Path<Date> validTo = root.get(RouteDataRevision_.validTo);
            Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

            return builder.or(builder.isNull(validTo), builder.greaterThanOrEqualTo(validTo, today));
        };
    }


    static Specification<RouteDataRevision> isExpired() {

        return
            (root, query, builder) -> {
            Path<Date> validTo = root.get(RouteDataRevision_.validTo);
            Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

            return builder.and(builder.isNotNull(validTo), builder.lessThan(validTo, today));
        };
    }


    private static Predicate alwaysTrue(CriteriaBuilder criteriaBuilder) {

        return criteriaBuilder.and();
    }
}
