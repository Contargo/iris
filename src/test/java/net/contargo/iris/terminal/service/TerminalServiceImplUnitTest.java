package net.contargo.iris.terminal.service;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.sequence.service.UniqueIdSequenceServiceImpl;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.persistence.TerminalRepository;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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
public class TerminalServiceImplUnitTest {

    private static final String TERMINAL_NAME = "foobar";

    private TerminalServiceImpl sut;

    @Mock
    private TerminalRepository terminalRepositoryMock;
    @Mock
    private UniqueIdSequenceServiceImpl uniqueIdSequenceServiceMock;

    private List<Terminal> terminals;

    @Before
    public void setUp() {

        sut = new TerminalServiceImpl(terminalRepositoryMock, uniqueIdSequenceServiceMock);

        Terminal terminal1 = new Terminal();
        Terminal terminal2 = new Terminal();

        terminals = asList(terminal1, terminal2);

        when(terminalRepositoryMock.findAll()).thenReturn(terminals);
        when(terminalRepositoryMock.findByEnabled(true)).thenReturn(terminals);
    }


    @Test
    public void getAll() {

        List<Terminal> terminals = sut.getAll();

        assertThat("There should be two terminals", terminals, hasSize(2));
    }


    @Test
    public void getAllActive() {

        List<Terminal> terminals = sut.getAllActive();

        assertThat("There should be two active terminals", terminals, hasSize(2));
    }


    @Test
    public void testGetById() {

        Terminal terminal = new Terminal();
        terminal.setId(1L);

        Mockito.when(terminalRepositoryMock.findOne(terminal.getId())).thenReturn(terminal);

        Terminal result = sut.getById(terminal.getId());

        verify(terminalRepositoryMock).findOne(terminal.getId());
        assertThat(terminal, is(result));
    }


    @Test
    public void testSave() {

        Terminal terminalToSave = new Terminal();
        terminalToSave.setLatitude(BigDecimal.ONE);
        terminalToSave.setLongitude(BigDecimal.ONE);
        terminalToSave.setUniqueId(BigInteger.ONE);

        Terminal savedTerminal = new Terminal();
        savedTerminal.setId(1L);
        savedTerminal.setLatitude(BigDecimal.ONE);
        savedTerminal.setLongitude(BigDecimal.ONE);
        savedTerminal.setUniqueId(BigInteger.ONE);
        when(terminalRepositoryMock.save(terminalToSave)).thenReturn(savedTerminal);

        Terminal actualSavedTerminal = sut.save(terminalToSave);

        assertThat(terminalToSave, is(actualSavedTerminal));
        assertThat(actualSavedTerminal.getId(), is(1L));
    }


    @Test
    public void testSaveWithoutUniqueId() {

        when(uniqueIdSequenceServiceMock.getNextId("Terminal")).thenReturn(BigInteger.ONE);
        when(terminalRepositoryMock.findByUniqueId(BigInteger.ONE)).thenReturn(null);

        Terminal terminalToSave = new Terminal();
        terminalToSave.setLatitude(BigDecimal.ONE);
        terminalToSave.setLongitude(BigDecimal.ONE);

        sut.save(terminalToSave);

        ArgumentCaptor<Terminal> captor = ArgumentCaptor.forClass(Terminal.class);
        verify(terminalRepositoryMock).save(captor.capture());
        assertThat(captor.getValue().getUniqueId(), notNullValue());
        assertThat(terminalToSave.getUniqueId(), is(BigInteger.ONE));
    }


    @Test
    public void determineUniqueId() {

        when(uniqueIdSequenceServiceMock.getNextId("Terminal")).thenReturn(BigInteger.ONE);
        when(terminalRepositoryMock.findByUniqueId(BigInteger.ONE)).thenReturn(null);

        sut.determineUniqueId();

        verify(uniqueIdSequenceServiceMock).getNextId("Terminal");
    }


    @Test
    public void determineUniqueIdWithSecondRetry() {

        when(uniqueIdSequenceServiceMock.getNextId("Terminal")).thenReturn(BigInteger.ONE);
        when(terminalRepositoryMock.findByUniqueId(BigInteger.ONE)).thenReturn(new Terminal());

        sut.determineUniqueId();

        InOrder order = inOrder(uniqueIdSequenceServiceMock);
        order.verify(uniqueIdSequenceServiceMock).getNextId("Terminal");
        order.verify(uniqueIdSequenceServiceMock).setNextId("Terminal", new BigInteger("2"));
    }


    @Test
    public void getByUniqueId() {

        BigInteger uniqueId = BigInteger.TEN;

        when(terminalRepositoryMock.findByUniqueId(uniqueId)).thenReturn(terminals.get(0));

        Terminal terminal = sut.getByUniqueId(uniqueId);

        assertThat(terminal, is(terminals.get(0)));
    }


    @Test
    public void existsByUniqueIdTrue() {

        BigInteger uniqueId = BigInteger.ONE;

        when(terminalRepositoryMock.findByUniqueId(uniqueId)).thenReturn(terminals.get(0));

        boolean exists = sut.existsByUniqueId(uniqueId);

        assertThat(exists, is(true));
    }


    @Test
    public void existsByUniqueIdFalse() {

        BigInteger uniqueId = BigInteger.ONE;

        when(terminalRepositoryMock.findByUniqueId(uniqueId)).thenReturn(null);

        boolean exists = sut.existsByUniqueId(uniqueId);

        assertThat(exists, is(false));
    }


    @Test
    public void updateTerminal() {

        BigInteger uniqueId = BigInteger.ONE;

        Terminal terminal = new Terminal();
        terminal.setUniqueId(uniqueId);
        terminal.setEnabled(true);
        terminal.setName("foobar");
        terminal.setLongitude(BigDecimal.ZERO);
        terminal.setLatitude(BigDecimal.ZERO);

        Terminal savedTerminal = new Terminal(new GeoLocation(BigDecimal.ZERO, BigDecimal.ZERO));
        savedTerminal.setId(42L);

        when(terminalRepositoryMock.findByUniqueId(uniqueId)).thenReturn(savedTerminal);
        when(terminalRepositoryMock.save(savedTerminal)).thenReturn(savedTerminal);

        Terminal updatedTerminal = sut.updateTerminal(uniqueId, terminal);

        assertThat(updatedTerminal.getName(), is(terminal.getName()));
        assertThat(updatedTerminal.getId(), is(42L));
        assertThat(updatedTerminal.isEnabled(), is(terminal.isEnabled()));
        assertThat(updatedTerminal.getLongitude(), comparesEqualTo(terminal.getLongitude()));
        assertThat(updatedTerminal.getLatitude(), comparesEqualTo(terminal.getLatitude()));
    }


    @Test
    public void checkUniqueConstraintsCreateWithNonUniqueCoordinates() {

        Terminal terminal = new Terminal(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN));
        terminal.setId(null);
        terminal.setName(TERMINAL_NAME);

        when(terminalRepositoryMock.findByLatitudeAndLongitude(any(BigDecimal.class), any(BigDecimal.class)))
            .thenReturn(terminals.get(0));
        when(terminalRepositoryMock.findByName(TERMINAL_NAME)).thenReturn(null);

        try {
            sut.checkUniqueConstraints(terminal);
            fail("Expected exception did not occur.");
        } catch (NonUniqueTerminalException e) {
            assertThat(e.getBadFields(), contains("latitude", "longitude"));
        }
    }


    @Test
    public void checkUniqueConstraintsCreateWithNonUniqueName() {

        Terminal terminal = new Terminal(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN));
        terminal.setId(null);
        terminal.setName(TERMINAL_NAME);

        when(terminalRepositoryMock.findByLatitudeAndLongitude(any(BigDecimal.class), any(BigDecimal.class)))
            .thenReturn(null);
        when(terminalRepositoryMock.findByName(TERMINAL_NAME)).thenReturn(terminals.get(0));

        try {
            sut.checkUniqueConstraints(terminal);
            fail("Expected exception did not occur.");
        } catch (NonUniqueTerminalException e) {
            assertThat(e.getBadFields(), contains("name"));
        }
    }


    @Test
    public void checkUniqueConstraintsUpdateWithNonUniqueCoordinates() {

        Terminal terminal = new Terminal(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN));
        terminal.setId(42L);
        terminal.setName(TERMINAL_NAME);

        when(terminalRepositoryMock.findByLatitudeAndLongitudeAndIdNot(any(BigDecimal.class), any(BigDecimal.class),
                anyLong())).thenReturn(terminals.get(0));
        when(terminalRepositoryMock.findByNameAndIdNot(TERMINAL_NAME, 42L)).thenReturn(null);

        try {
            sut.checkUniqueConstraints(terminal);
            fail("Expected exception did not occur.");
        } catch (NonUniqueTerminalException e) {
            assertThat(e.getBadFields(), contains("latitude", "longitude"));
        }
    }


    @Test
    public void checkUniqueConstraintsUpdateWithNonUniqueName() {

        Terminal terminal = new Terminal(new GeoLocation(BigDecimal.ONE, BigDecimal.TEN));
        terminal.setId(42L);
        terminal.setName(TERMINAL_NAME);

        when(terminalRepositoryMock.findByLatitudeAndLongitudeAndIdNot(any(BigDecimal.class), any(BigDecimal.class),
                anyLong())).thenReturn(null);
        when(terminalRepositoryMock.findByNameAndIdNot(TERMINAL_NAME, 42L)).thenReturn(terminals.get(0));

        try {
            sut.checkUniqueConstraints(terminal);
            fail("Expected exception did not occur.");
        } catch (NonUniqueTerminalException e) {
            assertThat(e.getBadFields(), contains("name"));
        }
    }
}
