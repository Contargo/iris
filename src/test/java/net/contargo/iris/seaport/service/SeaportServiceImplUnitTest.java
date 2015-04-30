package net.contargo.iris.seaport.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.seaport.Seaport;
import net.contargo.iris.seaport.persistence.SeaportRepository;
import net.contargo.iris.sequence.service.UniqueIdSequenceServiceImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.fail;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static java.util.Arrays.asList;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class SeaportServiceImplUnitTest {

    private static final String SEAPORT_NAME = "foobar";

    private SeaportServiceImpl sut;

    @Mock
    private SeaportRepository seaportRepositoryMock;
    @Mock
    private UniqueIdSequenceServiceImpl uniqueIdSequenceServiceMock;

    private List<Seaport> ports;

    @Before
    public void setUp() {

        sut = new SeaportServiceImpl(seaportRepositoryMock, uniqueIdSequenceServiceMock);

        Seaport port1 = new Seaport();
        Seaport port2 = new Seaport();

        ports = asList(port1, port2);

        when(seaportRepositoryMock.findAll()).thenReturn(ports);
        when(seaportRepositoryMock.findByEnabled(true)).thenReturn(ports);
    }


    @Test
    public void getAll() {

        List<Seaport> seaports = sut.getAll();

        assertThat("There should be two seaports", seaports, hasSize(2));
    }


    @Test
    public void getAllActive() {

        List<Seaport> seaports = sut.getAllActive();

        assertThat("There should be two active seaports", seaports, hasSize(2));
    }


    @Test
    public void testGetById() {

        Seaport seaport = new Seaport();

        when(seaportRepositoryMock.findOne(seaport.getId())).thenReturn(seaport);

        Seaport result = sut.getById(seaport.getId());

        verify(seaportRepositoryMock).findOne(seaport.getId());
        assertThat(seaport, is(result));
    }


    @Test
    public void testSave() {

        Seaport seaportToSave = new Seaport();
        seaportToSave.setLatitude(BigDecimal.ONE);
        seaportToSave.setLongitude(BigDecimal.ONE);

        Seaport savedSeaport = new Seaport();
        savedSeaport.setId(1L);
        savedSeaport.setLatitude(BigDecimal.ONE);
        savedSeaport.setLongitude(BigDecimal.ONE);
        when(seaportRepositoryMock.save(seaportToSave)).thenReturn(savedSeaport);

        assertThat(seaportToSave, is(savedSeaport));
        assertThat(savedSeaport.getId(), is(1L));
    }


    @Test
    public void testSaveWithoutUniqueId() {

        when(uniqueIdSequenceServiceMock.getNextId("Seaport")).thenReturn(BigInteger.ONE);
        when(seaportRepositoryMock.findByUniqueId(BigInteger.ONE)).thenReturn(null);

        Seaport seaportToSave = new Seaport();
        seaportToSave.setLatitude(BigDecimal.ONE);
        seaportToSave.setLongitude(BigDecimal.ONE);

        sut.save(seaportToSave);

        ArgumentCaptor<Seaport> captor = ArgumentCaptor.forClass(Seaport.class);
        verify(seaportRepositoryMock).save(captor.capture());
        assertThat(captor.getValue().getUniqueId(), notNullValue());
        assertThat(seaportToSave.getUniqueId(), is(BigInteger.ONE));
    }


    @Test
    public void determineUniqueId() {

        when(uniqueIdSequenceServiceMock.getNextId("Seaport")).thenReturn(BigInteger.ONE);
        when(seaportRepositoryMock.findByUniqueId(BigInteger.ONE)).thenReturn(null);

        sut.determineUniqueId();

        verify(uniqueIdSequenceServiceMock).getNextId("Seaport");
    }


    @Test
    public void determineUniqueIdWithSecondRetry() {

        when(uniqueIdSequenceServiceMock.getNextId("Seaport")).thenReturn(BigInteger.ONE);
        when(seaportRepositoryMock.findByUniqueId(BigInteger.ONE)).thenReturn(new Seaport());

        sut.determineUniqueId();

        InOrder order = inOrder(uniqueIdSequenceServiceMock);
        order.verify(uniqueIdSequenceServiceMock).getNextId("Seaport");
        order.verify(uniqueIdSequenceServiceMock).setNextId("Seaport", new BigInteger("2"));
    }


    @Test
    public void getByUniqueId() {

        BigInteger uniqueId = BigInteger.TEN;

        when(seaportRepositoryMock.findByUniqueId(uniqueId)).thenReturn(ports.get(0));

        Seaport seaport = sut.getByUniqueId(uniqueId);

        assertThat(seaport, is(ports.get(0)));
    }


    @Test
    public void existsByUniqueIdTrue() {

        BigInteger uniqueId = BigInteger.ONE;

        when(seaportRepositoryMock.findByUniqueId(uniqueId)).thenReturn(ports.get(0));

        boolean exists = sut.existsByUniqueId(uniqueId);

        assertThat(exists, is(true));
    }


    @Test
    public void existsByUniqueIdFalse() {

        BigInteger uniqueId = BigInteger.ONE;

        when(seaportRepositoryMock.findByUniqueId(uniqueId)).thenReturn(null);

        boolean exists = sut.existsByUniqueId(uniqueId);

        assertThat(exists, is(false));
    }


    @Test
    public void updateSeaport() {

        BigInteger uniqueId = BigInteger.ONE;

        Seaport seaport = new Seaport();
        seaport.setUniqueId(uniqueId);
        seaport.setEnabled(true);
        seaport.setName("foobar");
        seaport.setLongitude(BigDecimal.ZERO);
        seaport.setLatitude(BigDecimal.ZERO);

        Seaport savedSeaport = new Seaport(new GeoLocation(BigDecimal.ZERO, BigDecimal.ZERO));
        savedSeaport.setId(42L);

        when(seaportRepositoryMock.findByUniqueId(uniqueId)).thenReturn(savedSeaport);
        when(seaportRepositoryMock.save(savedSeaport)).thenReturn(savedSeaport);

        Seaport updatedSeaport = sut.updateSeaport(uniqueId, seaport);

        assertThat(updatedSeaport.getName(), is(seaport.getName()));
        assertThat(updatedSeaport.getId(), is(42L));
        assertThat(updatedSeaport.isEnabled(), is(seaport.isEnabled()));
        assertThat(updatedSeaport.getLongitude(), comparesEqualTo(seaport.getLongitude()));
        assertThat(updatedSeaport.getLatitude(), comparesEqualTo(seaport.getLatitude()));
    }


    @Test
    public void checkUniqueConstraintsCreateWithNonUniqueCoordinates() {

        Seaport seaport = new Seaport(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN));
        seaport.setId(null);
        seaport.setName(SEAPORT_NAME);

        when(seaportRepositoryMock.findByLatitudeAndLongitude(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(
            ports.get(0));
        when(seaportRepositoryMock.findByName(SEAPORT_NAME)).thenReturn(null);

        try {
            sut.checkUniqueConstraints(seaport);
            fail("Expected exception did not occur.");
        } catch (NonUniqueSeaportException e) {
            Assert.assertThat(e.getBadFields(), contains("latitude", "longitude"));
        }
    }


    @Test
    public void checkUniqueConstraintsCreateWithNonUniqueName() {

        Seaport seaport = new Seaport(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN));
        seaport.setId(null);
        seaport.setName(SEAPORT_NAME);

        when(seaportRepositoryMock.findByLatitudeAndLongitude(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(
            null);
        when(seaportRepositoryMock.findByName(SEAPORT_NAME)).thenReturn(ports.get(0));

        try {
            sut.checkUniqueConstraints(seaport);
            fail("Expected exception did not occur.");
        } catch (NonUniqueSeaportException e) {
            Assert.assertThat(e.getBadFields(), contains("name"));
        }
    }


    @Test
    public void checkUniqueConstraintsUpdateWithNonUniqueCoordinates() {

        Seaport seaport = new Seaport(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN));
        seaport.setId(42L);
        seaport.setName(SEAPORT_NAME);

        when(seaportRepositoryMock.findByLatitudeAndLongitudeAndIdNot(any(BigDecimal.class), any(BigDecimal.class),
                anyLong())).thenReturn(ports.get(0));
        when(seaportRepositoryMock.findByNameAndIdNot(SEAPORT_NAME, 42L)).thenReturn(null);

        try {
            sut.checkUniqueConstraints(seaport);
            fail("Expected exception did not occur.");
        } catch (NonUniqueSeaportException e) {
            Assert.assertThat(e.getBadFields(), contains("latitude", "longitude"));
        }
    }


    @Test
    public void checkUniqueConstraintsUpdateWithNonUniqueName() {

        Seaport seaport = new Seaport(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN));
        seaport.setId(42L);
        seaport.setName(SEAPORT_NAME);

        when(seaportRepositoryMock.findByLatitudeAndLongitudeAndIdNot(any(BigDecimal.class), any(BigDecimal.class),
                anyLong())).thenReturn(null);
        when(seaportRepositoryMock.findByNameAndIdNot(SEAPORT_NAME, 42L)).thenReturn(ports.get(0));

        try {
            sut.checkUniqueConstraints(seaport);
            fail("Expected exception did not occur.");
        } catch (NonUniqueSeaportException e) {
            Assert.assertThat(e.getBadFields(), contains("name"));
        }
    }
}
