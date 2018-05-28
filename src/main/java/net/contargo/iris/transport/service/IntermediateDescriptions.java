package net.contargo.iris.transport.service;

import net.contargo.iris.transport.api.TransportDescriptionDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author  Ben Antony - antony@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
class IntermediateDescriptions {

    private List<TransportDescriptionDto> descriptions = new ArrayList<>();

    IntermediateDescriptions(TransportDescriptionDto description) {

        this.descriptions.add(description);
    }

    void updateWith(List<TransportDescriptionDto> update) {

        this.descriptions = new ArrayList<>(update);
    }


    List<TransportDescriptionDto> get() {

        return Collections.unmodifiableList(this.descriptions);
    }
}
