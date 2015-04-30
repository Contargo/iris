package net.contargo.iris.seaport.dto;

import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.List;


/**
 * @author  Arnold Franke - franke@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 */
public class SeaportDtoServiceImpl implements SeaportDtoService {

    private final SeaportService seaportService;

    public SeaportDtoServiceImpl(SeaportService seaportService) {

        this.seaportService = seaportService;
    }

    @Override
    public List<SeaportDto> getAll() {

        List<SeaportDto> seaportDtos = new ArrayList<>();
        List<Seaport> seaports = seaportService.getAll();

        for (Seaport seaport : seaports) {
            seaportDtos.add(new SeaportDto(seaport));
        }

        return seaportDtos;
    }


    @Override
    public List<SeaportDto> getAllActive() {

        List<SeaportDto> seaportDtos = new ArrayList<>();
        List<Seaport> seaports = seaportService.getAllActive();

        for (Seaport seaport : seaports) {
            seaportDtos.add(new SeaportDto(seaport));
        }

        return seaportDtos;
    }


    @Override
    public SeaportDto getById(Long id) {

        Seaport seaport = seaportService.getById(id);

        return seaport == null ? null : new SeaportDto(seaport);
    }


    @Override
    public SeaportDto save(SeaportDto seaPortDto) {

        Seaport seaportToSave = seaPortDto.toEntity();
        Seaport savedSeaport = seaportService.save(seaportToSave);

        return new SeaportDto(savedSeaport);
    }


    @Override
    public boolean existsByUniqueId(BigInteger seaportUid) {

        return seaportService.existsByUniqueId(seaportUid);
    }


    @Override
    public SeaportDto updateSeaport(BigInteger seaportUid, SeaportDto seaportDto) {

        Seaport seaport = seaportService.updateSeaport(seaportUid, seaportDto.toEntity());

        return new SeaportDto(seaport);
    }


    @Override
    public SeaportDto getByUid(BigInteger uid) {

        Seaport seaport = seaportService.getByUniqueId(uid);

        return seaport == null ? null : new SeaportDto(seaport);
    }
}
