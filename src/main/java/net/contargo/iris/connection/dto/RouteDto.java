package net.contargo.iris.connection.dto;

import net.contargo.iris.route.Route;
import net.contargo.iris.route.RouteDirection;
import net.contargo.iris.route.RouteProduct;
import net.contargo.iris.terminal.dto.TerminalDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Dto layer for {@link Route}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Arnold Franke - franke@synyx.de
 */
public class RouteDto {

    private TerminalDto responsibleTerminal;
    private boolean roundTrip;
    private RouteProduct product;
    private String shortName;
    private String name;
    private RouteDataDto data;
    private Map<String, String> errors;
    private RouteDirection direction;
    private String url;

    public RouteDto(Route route) {

        if (route != null) {
            this.name = route.getName();
            this.data = route.getData() == null ? null : new RouteDataDto(route.getData());
            this.shortName = route.getShortName();
            this.roundTrip = route.isRoundTrip();
            this.direction = route.getDirection();
            this.product = route.getProduct();
            this.errors = route.getErrors();
            this.responsibleTerminal = route.getResponsibleTerminal() == null
                ? null : new TerminalDto(route.getResponsibleTerminal());
        }
    }


    public RouteDto() {

        // needed for Spring MVC instantiation of Controller parameter
        this.errors = new HashMap<>();
    }

    public Route toRoute() {

        Route route = new Route();

        if (this.getData() != null) {
            route.setData(this.getData().toRouteData());
        }

        if (responsibleTerminal != null) {
            route.setResponsibleTerminal(this.responsibleTerminal.toEntity());
        }

        return route;
    }


    public TerminalDto getResponsibleTerminal() {

        return responsibleTerminal;
    }


    public void setResponsibleTerminal(TerminalDto responsibleTerminal) {

        this.responsibleTerminal = responsibleTerminal;
    }


    public int size() {

        return data.getParts().size();
    }


    public void setName(String name) {

        this.name = name;
    }


    public void setRoundTrip(boolean roundTrip) {

        this.roundTrip = roundTrip;
    }


    public void setProduct(RouteProduct product) {

        this.product = product;
    }


    public void setShortName(String shortName) {

        this.shortName = shortName;
    }


    public void setData(RouteDataDto data) {

        this.data = data;
    }


    public void setErrors(Map<String, String> errors) {

        this.errors = errors;
    }


    public void setDirection(RouteDirection direction) {

        this.direction = direction;
    }


    public String getName() {

        return name;
    }


    public String getShortName() {

        return shortName;
    }


    public RouteDataDto getData() {

        return data;
    }


    public boolean isRoundTrip() {

        return roundTrip;
    }


    public String getUrl() {

        return url;
    }


    public void setUrl(String url) {

        this.url = url;
    }


    public Map<String, String> getErrors() {

        return Collections.unmodifiableMap(errors);
    }


    public RouteDirection getDirection() {

        return direction;
    }


    public RouteProduct getProduct() {

        return product;
    }
}
