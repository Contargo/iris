package net.contargo.iris.seaport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.contargo.iris.address.dto.GeoLocationDto;
import net.contargo.iris.seaport.Seaport;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.math.BigInteger;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
public class SeaportDto extends GeoLocationDto {

    private static final int MAX_NAME_SIZE = 254;
    private static final String SEAPORT = "SEAPORT";

    @NotEmpty
    @Size(max = MAX_NAME_SIZE)
    private String name;

    private boolean enabled;
    private String type;

    private String uniqueId;

    public SeaportDto() {

        // Used to create JSON Objects
    }


    public SeaportDto(Seaport seaport) {

        super(seaport);

        if (seaport != null) {
            this.name = seaport.getName();
            this.enabled = seaport.isEnabled();
            this.uniqueId = seaport.getUniqueId() == null ? null : seaport.getUniqueId().toString();
            this.type = SEAPORT;
        }
    }

    public boolean isEnabled() {

        return enabled;
    }


    public String getName() {

        return name;
    }


    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }


    public void setName(String name) {

        this.name = name;
    }


    @Override
    public Seaport toEntity() {

        Seaport seaport = new Seaport(super.toEntity());
        seaport.setEnabled(this.enabled);
        seaport.setName(this.name);
        seaport.setUniqueId(this.uniqueId == null ? null : new BigInteger(this.uniqueId));

        return seaport;
    }


    @Override
    public String getType() {

        return type;
    }


    @JsonProperty("uniqueId")
    public String getUniqueId() {

        return uniqueId;
    }


    public void setUniqueId(String uniqueId) {

        this.uniqueId = uniqueId;
    }
}
