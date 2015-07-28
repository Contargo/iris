package net.contargo.iris.terminal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.contargo.iris.address.dto.GeoLocationDto;
import net.contargo.iris.terminal.Region;
import net.contargo.iris.terminal.Terminal;

import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigInteger;

import javax.validation.constraints.Size;


/**
 * View Bean Class for Terminal.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public final class TerminalDto extends GeoLocationDto {

    private static final int MAX_NAME_SIZE = 254;
    private static final String TERMINAL = "TERMINAL";

    @NotEmpty
    @Size(max = MAX_NAME_SIZE)
    private String name;

    private boolean enabled;

    private String uniqueId;
    private String type;
    private Region region;

    public TerminalDto() {

        // Used to create JSON Objects
    }


    public TerminalDto(Terminal terminal) {

        super(terminal);

        if (terminal != null) {
            this.name = terminal.getName();
            this.enabled = terminal.isEnabled();
            this.type = TERMINAL;
            this.uniqueId = terminal.getUniqueId() == null ? null : terminal.getUniqueId().toString();

            if (terminal.getRegion() != null) {
                this.region = terminal.getRegion();
            }
        }
    }

    @JsonProperty("uniqueId")
    public String getUniqueId() {

        return uniqueId;
    }


    public void setUniqueId(String uniqueId) {

        this.uniqueId = uniqueId;
    }


    // Setters are needed so this DTO can be used as @ModelAttribute in Spring MVC
    public void setName(String name) {

        this.name = name;
    }


    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }


    public boolean isEnabled() {

        return enabled;
    }


    public String getName() {

        return name;
    }


    public Region getRegion() {

        return region;
    }


    public void setRegion(Region region) {

        this.region = region;
    }


    @Override
    public Terminal toEntity() {

        Terminal terminal = new Terminal(super.toEntity());
        terminal.setEnabled(this.enabled);
        terminal.setName(this.name);
        terminal.setUniqueId(this.uniqueId == null ? null : new BigInteger(this.uniqueId));
        terminal.setRegion(this.region);

        return terminal;
    }


    @Override
    public String getType() {

        return type;
    }
}
