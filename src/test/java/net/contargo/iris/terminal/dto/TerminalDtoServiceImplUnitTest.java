package net.contargo.iris.terminal.dto;

import net.contargo.iris.GeoLocation;
import net.contargo.iris.terminal.Terminal;
import net.contargo.iris.terminal.service.TerminalService;

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
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class TerminalDtoServiceImplUnitTest {

    private TerminalDtoServiceImpl sut;
    @Mock
    private TerminalService terminalServiceMock;
    private Terminal terminal1;
    private Terminal terminal2;

    @Before
    public void setUp() throws Exception {

        sut = new TerminalDtoServiceImpl(terminalServiceMock);

        terminal1 = new Terminal();
        terminal1.setLatitude(BigDecimal.ONE);
        terminal1.setLongitude(BigDecimal.ONE);
        terminal1.setName("name1");
        terminal1.setEnabled(true);

        terminal2 = new Terminal();
        terminal2.setLatitude(BigDecimal.TEN);
        terminal2.setLongitude(BigDecimal.TEN);
    }


    @Test
    public void testGetAll() {

        when(terminalServiceMock.getAll()).thenReturn(Arrays.asList(terminal1, terminal2));

        assertThat(sut.getAll().size(), is(2));
        assertThat(sut.getAll().get(0).getName(), is("name1"));
        assertThat(sut.getAll().get(0).isEnabled(), is(true));
    }


    @Test
    public void testGetAllActive() {

        Terminal terminal2 = new Terminal();
        terminal2.setLatitude(BigDecimal.TEN);
        terminal2.setLongitude(BigDecimal.TEN);

        when(terminalServiceMock.getAllActive()).thenReturn(Arrays.asList(terminal1, terminal2));

        assertThat(sut.getAllActive().size(), is(2));
        assertThat(sut.getAllActive().get(0).getName(), is("name1"));
        assertThat(sut.getAllActive().get(0).isEnabled(), is(true));
    }


    @Test
    public void testSave() {

        when(terminalServiceMock.save(any(Terminal.class))).thenReturn(terminal1);

        TerminalDto saveTerminalDto = new TerminalDto(terminal1);
        TerminalDto returnTerminalDto = sut.save(saveTerminalDto);
        assertReflectionEquals(returnTerminalDto, saveTerminalDto);
        verify(terminalServiceMock, times(1)).save(any(Terminal.class));
    }


    @Test
    public void testGetById() {

        when(terminalServiceMock.getByUniqueId(BigInteger.ONE)).thenReturn(terminal1);

        TerminalDto returnTerminalDto = sut.getByUid(BigInteger.ONE);

        assertReflectionEquals(returnTerminalDto, new TerminalDto(terminal1));
    }


    @Test
    public void testGetByIdNull() {

        TerminalDto returnTerminalDto = sut.getByUid(BigInteger.ONE);

        assertThat(returnTerminalDto, nullValue());
    }


    @Test
    public void existsByUniqueIdTrue() {

        BigInteger uniqueId = BigInteger.ONE;

        when(terminalServiceMock.existsByUniqueId(uniqueId)).thenReturn(true);

        boolean exists = sut.existsByUniqueId(uniqueId);

        assertThat(exists, is(true));
    }


    @Test
    public void existsByUniqueIdFalse() {

        BigInteger uniqueId = BigInteger.ONE;

        when(terminalServiceMock.existsByUniqueId(uniqueId)).thenReturn(false);

        boolean exists = sut.existsByUniqueId(uniqueId);

        assertThat(exists, is(false));
    }


    @Test
    public void updateTerminal() {

        BigInteger uniqueId = BigInteger.ONE;

        TerminalDto dto = new TerminalDto();
        dto.setUniqueId(uniqueId.toString());
        dto.setEnabled(true);
        dto.setName("foobar");
        dto.setLongitude(BigDecimal.ZERO);
        dto.setLatitude(BigDecimal.ZERO);

        Terminal terminal = dto.toEntity();

        Terminal savedTerminal = new Terminal(new GeoLocation(BigDecimal.ZERO, BigDecimal.ZERO));
        savedTerminal.setId(42L);

        when(terminalServiceMock.updateTerminal(uniqueId, terminal)).thenReturn(savedTerminal);

        TerminalDto updatedDto = sut.updateTerminal(uniqueId, dto);

        assertThat(updatedDto.toEntity(), is(savedTerminal));
    }
}
