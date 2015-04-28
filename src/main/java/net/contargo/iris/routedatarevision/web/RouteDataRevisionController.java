package net.contargo.iris.routedatarevision.web;

import net.contargo.iris.routedatarevision.dto.RouteDataRevisionDtoService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author  David Schilling - schilling@synyx.de
 */
@Controller
@RequestMapping("/routerevisions")
public class RouteDataRevisionController {

    private final RouteDataRevisionDtoService routeDataRevisionDtoService;

    @Autowired
    public RouteDataRevisionController(RouteDataRevisionDtoService routeDataRevisionDtoService) {

        this.routeDataRevisionDtoService = routeDataRevisionDtoService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getAll(Model model) {

        model.addAttribute("routeRevisions", routeDataRevisionDtoService.getRouteDataRevisions());

        return "routeRevisionManagement/routeRevisions";
    }
}
