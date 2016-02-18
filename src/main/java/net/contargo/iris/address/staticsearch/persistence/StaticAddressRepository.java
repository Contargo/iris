package net.contargo.iris.address.staticsearch.persistence;

import net.contargo.iris.address.staticsearch.StaticAddress;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;


/**
 * Repository that provides access to data of {@link StaticAddress}.
 *
 * @author  Aljona Murygina - murygina@synyx.de
 */
public interface StaticAddressRepository extends JpaRepository<StaticAddress, Long> {

    String SELECT_FROM_STATICADDRESS_A = "SELECT a FROM StaticAddress a ";
    String ORDER_BY_CITY_SUBURB_POSTALCODE = "ORDER BY a.cityNormalized, a.suburbNormalized, a.postalcode";

    @Query(
        SELECT_FROM_STATICADDRESS_A
        + "WHERE a.postalcode = ?1 "
        + "AND (a.cityNormalized LIKE ?2 OR a.suburbNormalized LIKE ?2) " + ORDER_BY_CITY_SUBURB_POSTALCODE
    )
    List<StaticAddress> findByPostalCodeAndCity(String postalCode, String city);


    @Query(
        SELECT_FROM_STATICADDRESS_A
        + "WHERE a.postalcode = ?1 "
        + "OR a.cityNormalized LIKE ?2 "
        + "OR a.suburbNormalized LIKE ?2 " + ORDER_BY_CITY_SUBURB_POSTALCODE
    )
    List<StaticAddress> findByPostalCodeOrCity(String postalCode, String city);


    @Query(
        SELECT_FROM_STATICADDRESS_A
        + "WHERE a.country = ?3 "
        + "AND a.postalcode = ?1 "
        + "AND (a.cityNormalized LIKE CONCAT('%', ?2, '%') OR a.suburbNormalized LIKE CONCAT('%', ?2, '%')) "
        + ORDER_BY_CITY_SUBURB_POSTALCODE
    )
    List<StaticAddress> findByCountryAndPostalCodeAndCity(String postalCode, String city, String country);


    @Query(
        SELECT_FROM_STATICADDRESS_A
        + "WHERE a.country = ?3 "
        + "AND ((a.postalcode = ?1 AND a.postalcode <> '') "
        + "OR (a.cityNormalized LIKE ?2 AND a.cityNormalized <> '') "
        + "OR (a.suburbNormalized LIKE ?2 AND a.suburbNormalized <> '')) " + ORDER_BY_CITY_SUBURB_POSTALCODE
    )
    List<StaticAddress> findByCountryAndPostalCodeOrCity(String postalCode, String city, String country);


    @Query(
        SELECT_FROM_STATICADDRESS_A
        + "WHERE (a.latitude between ?1 AND ?2) "
        + "AND (a.longitude between ?3 AND ?4) " + ORDER_BY_CITY_SUBURB_POSTALCODE
    )
    List<StaticAddress> findByBoundingBox(BigDecimal fromLatitude, BigDecimal toLatitude, BigDecimal fromLongitude,
        BigDecimal toLongitude);


    @Query(
        SELECT_FROM_STATICADDRESS_A
        + "WHERE (a.hashKey is null OR a.hashKey = '') "
    )
    Page<StaticAddress> findMissingHashKeys(Pageable pageable);


    StaticAddress findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);


    StaticAddress findByUniqueId(BigInteger uniqueId);


    List<StaticAddress> findByPostalcode(String postalCode);


    List<StaticAddress> findByPostalcodeAndCountry(String postalCode, String country);


    List<StaticAddress> findByCityAndSuburbAndPostalcode(String city, String suburb, String postalcode);


    StaticAddress findByHashKey(String hashKey);
}
