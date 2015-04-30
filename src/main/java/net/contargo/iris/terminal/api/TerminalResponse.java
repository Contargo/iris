package net.contargo.iris.terminal.api;

import net.contargo.iris.terminal.dto.TerminalDto;

import org.springframework.hateoas.ResourceSupport;


/**
 * HATEOAS supporting response object for a single {@link net.contargo.iris.terminal.dto.TerminalDto}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
class TerminalResponse extends ResourceSupport {

    private TerminalDto terminal;

    public TerminalDto getTerminal() {

        return terminal;
    }


    public void setTerminal(TerminalDto terminal) {

        this.terminal = terminal;
    }
}
