package net.contargo.iris.seaport.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.service.SeaportService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class SeaportDtoServiceImplUnitTest {

    private SeaportDtoServiceImpl sut;
    @Mock
    private SeaportService seaportServiceMock;
    private Seaport seaport1;
    private Seaport seaport2;

    @Before
    public void setUp() throws Exception {

        sut = new SeaportDtoServiceImpl(seaportServiceMock);

        seaport1 = new Seaport();
        seaport1.setLatitude(BigDecimal.ONE);
        seaport1.setLongitude(BigDecimal.ONE);
        seaport1.setName("name1");
        seaport1.setEnabled(true);

        seaport2 = new Seaport();
        seaport2.setLatitude(BigDecimal.TEN);
        seaport2.setLongitude(BigDecimal.TEN);
    }


    @Test
    public void testGetAll() {

        when(seaportServiceMock.getAll()).thenReturn(Arrays.asList(seaport1, seaport2));

        assertThat(sut.getAll().size(), is(2));
        assertThat(sut.getAll().get(0).getName(), is("name1"));
        assertThat(sut.getAll().get(0).isEnabled(), is(true));
    }


    @Test
    public void testGetAllActive() {

        Seaport seaport3 = new Seaport();
        seaport3.setLatitude(BigDecimal.TEN);
        seaport3.setLongitude(BigDecimal.TEN);

        when(seaportServiceMock.getAllActive()).thenReturn(Arrays.asList(seaport1, seaport3));

        assertThat(sut.getAllActive().size(), is(2));
        assertThat(sut.getAllActive().get(0).getName(), is("name1"));
        assertThat(sut.getAllActive().get(0).isEnabled(), is(true));
    }


    @Test
    public void testSave() {

        when(seaportServiceMock.save(any(Seaport.class))).thenReturn(seaport1);

        SeaportDto saveSeaportDto = new SeaportDto(seaport1);
        SeaportDto returnSeaportDto = sut.save(saveSeaportDto);
        assertReflectionEquals(returnSeaportDto, saveSeaportDto);
        verify(seaportServiceMock, times(1)).save(any(Seaport.class));
    }


    @Test
    public void testGetById() {

        when(seaportServiceMock.getById(1L)).thenReturn(seaport1);

        SeaportDto returnSeaportDto = sut.getById(1L);

        assertReflectionEquals(returnSeaportDto, new SeaportDto(seaport1));
    }


    @Test
    public void testGetByIdNull() {

        SeaportDto returnSeaportDto = sut.getById(1L);

        assertThat(returnSeaportDto, nullValue());
    }


    @Test
    public void existsByUniqueIdTrue() {

        BigInteger uniqueId = BigInteger.ONE;

        when(seaportServiceMock.existsByUniqueId(uniqueId)).thenReturn(true);

        boolean exists = sut.existsByUniqueId(uniqueId);

        assertThat(exists, is(true));
    }


    @Test
    public void existsByUniqueIdFalse() {

        BigInteger uniqueId = BigInteger.ONE;

        when(seaportServiceMock.existsByUniqueId(uniqueId)).thenReturn(false);

        boolean exists = sut.existsByUniqueId(uniqueId);

        assertThat(exists, is(false));
    }


    @Test
    public void updateSeaport() {

        BigInteger uniqueId = BigInteger.ONE;

        SeaportDto dto = new SeaportDto();
        dto.setUniqueId(uniqueId.toString());
        dto.setEnabled(true);
        dto.setName("foobar");
        dto.setLongitude(BigDecimal.ZERO);
        dto.setLatitude(BigDecimal.ZERO);

        Seaport seaport = dto.toEntity();

        Seaport savedSeaport = new Seaport(new GeoLocation(BigDecimal.ZERO, BigDecimal.ZERO));
        savedSeaport.setId(42L);

        when(seaportServiceMock.updateSeaport(uniqueId, seaport)).thenReturn(savedSeaport);

        SeaportDto updatedDto = sut.updateSeaport(uniqueId, dto);

        assertThat(updatedDto.toEntity(), is(savedSeaport));
    }


    @Test
    public void getByUid() {

        when(seaportServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(seaport1);

        SeaportDto returnSeaportDto = sut.getByUid(BigInteger.ONE);

        assertReflectionEquals(returnSeaportDto, new SeaportDto(seaport1));
    }


    @Test
    public void testGetByUidNull() {

        SeaportDto returnSeaportDto = sut.getByUid(BigInteger.ONE);

        assertThat(returnSeaportDto, nullValue());
    }
}
