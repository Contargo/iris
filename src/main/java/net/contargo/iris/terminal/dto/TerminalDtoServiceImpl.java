package net.contargo.iris.terminal.dto;

import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class TerminalDtoServiceImpl implements TerminalDtoService {

    private final TerminalService terminalService;

    public TerminalDtoServiceImpl(TerminalService terminalService) {

        this.terminalService = terminalService;
    }

    @Override
    public List<TerminalDto> getAll() {

        List<TerminalDto> termialDtos = new ArrayList<>();
        List<Terminal> terminals = terminalService.getAll();

        for (Terminal terminal : terminals) {
            termialDtos.add(new TerminalDto(terminal));
        }

        return termialDtos;
    }


    @Override
    public List<TerminalDto> getAllActive() {

        List<TerminalDto> termialDtos = new ArrayList<>();
        List<Terminal> terminals = terminalService.getAllActive();

        for (Terminal terminal : terminals) {
            termialDtos.add(new TerminalDto(terminal));
        }

        return termialDtos;
    }


    @Override
    public TerminalDto save(TerminalDto terminalDto) {

        Terminal terminalToSave = terminalDto.toEntity();
        Terminal savedTerminal = terminalService.save(terminalToSave);

        return new TerminalDto(savedTerminal);
    }


    @Override
    public TerminalDto getByUid(BigInteger uid) {

        Terminal terminal = terminalService.getByUniqueId(uid);

        return terminal == null ? null : new TerminalDto(terminal);
    }


    @Override
    public boolean existsByUniqueId(BigInteger uniqueId) {

        return terminalService.existsByUniqueId(uniqueId);
    }


    @Override
    public TerminalDto updateTerminal(BigInteger terminalUid, TerminalDto terminalDto) {

        Terminal terminal = terminalService.updateTerminal(terminalUid, terminalDto.toEntity());

        return new TerminalDto(terminal);
    }
}
