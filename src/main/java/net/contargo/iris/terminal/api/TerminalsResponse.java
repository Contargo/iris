package net.contargo.iris.terminal.api;

import net.contargo.iris.terminal.dto.TerminalDto;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;


/**
 * HATEOAS supporting response object for a list of {@link net.contargo.iris.terminal.dto.TerminalDto}s.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
class TerminalsResponse extends ResourceSupport {

    private List<TerminalDto> terminals;

    public List<TerminalDto> getTerminals() {

        return terminals;
    }


    public void setTerminals(List<TerminalDto> terminals) {

        this.terminals = terminals;
    }
}
