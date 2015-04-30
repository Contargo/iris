package net.contargo.iris.seaport.persistence;

import net.contargo.iris.seaport.Seaport;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;


/**
 * Repository interface for {@link Seaport}.
 *
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Tobias Schneider - schneider@synyx.de
 */
public interface SeaportRepository extends JpaRepository<Seaport, Long> {

    List<Seaport> findByEnabled(boolean enabled);


    Seaport findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);


    Seaport findByUniqueId(BigInteger uniqueId);


    Seaport findByName(String name);


    Seaport findByLatitudeAndLongitudeAndIdNot(BigDecimal latitude, BigDecimal longitude, Long terminalId);


    Seaport findByNameAndIdNot(String name, Long terminalId);
}
