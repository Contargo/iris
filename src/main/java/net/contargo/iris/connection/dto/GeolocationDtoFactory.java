package net.contargo.iris.connection.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.address.Address;
import net.contargo.iris.address.dto.AddressDto;
import net.contargo.iris.address.dto.GeoLocationDto;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.dto.SeaportDto;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.dto.TerminalDto;


/**
 * Factory that creates {@link GeoLocationDto}s from {@link GeoLocation}s, {@link Terminal}s or {@link Seaport}s.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
final class GeolocationDtoFactory {

    private GeolocationDtoFactory() {

        // utility classes should not have a public or default constructor
    }

    /**
     * Transforms a {@link GeoLocation} instance into its corresponing dto of type {@link GeoLocationDto},
     * {@link TerminalDto} or {@link SeaportDto}.
     *
     * @param  geolocation
     *
     * @return
     */
    static GeoLocationDto createGeolocationDto(GeoLocation geolocation) {

        GeoLocationDto dto;

        if (geolocation instanceof Terminal) {
            dto = new TerminalDto((Terminal) geolocation);
        } else if (geolocation instanceof Seaport) {
            dto = new SeaportDto((Seaport) geolocation);
        } else if (geolocation instanceof Address) {
            dto = new AddressDto((Address) geolocation);
        } else {
            dto = new GeoLocationDto(geolocation);
        }

        return dto;
    }
}
