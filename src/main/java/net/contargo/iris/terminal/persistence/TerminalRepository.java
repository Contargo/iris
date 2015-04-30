package net.contargo.iris.terminal.persistence;

import net.contargo.iris.terminal.Terminal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;


/**
 * Repository interface for {@link Terminal}s.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface TerminalRepository extends JpaRepository<Terminal, Long> {

    List<Terminal> findByEnabled(boolean enabled);


    Terminal findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);


    Terminal findByUniqueId(BigInteger uniqueId);


    Terminal findByName(String name);


    Terminal findByLatitudeAndLongitudeAndIdNot(BigDecimal latitude, BigDecimal longitude, Long terminalId);


    Terminal findByNameAndIdNot(String name, Long terminalId);
}
