package net.contargo.iris.seaport.dto;

import java.math.BigInteger;

import java.util.List;


/**
 * View Bean Service for mapping of {@link net.contargo.iris.seaport.Seaport}s to {@link SeaportDto}s, Maps all return
 * values of the methods of @link SeaportService} to View Beans. For detailed Javadoc see interface
 * {@link net.contargo.iris.seaport.service.SeaportService}.
 *
 * @author  Arnold Franke - franke@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public interface SeaportDtoService {

    List<SeaportDto> getAll();


    List<SeaportDto> getAllActive();


    SeaportDto getById(Long id);


    SeaportDto save(SeaportDto seaPortDto);


    boolean existsByUniqueId(BigInteger seaportUid);


    SeaportDto updateSeaport(BigInteger seaportUid, SeaportDto seaportDto);


    SeaportDto getByUid(BigInteger uid);
}
