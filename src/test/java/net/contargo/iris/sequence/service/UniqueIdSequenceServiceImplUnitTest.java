package net.contargo.iris.sequence.service;

import net.contargo.iris.sequence.UniqueIdSequence;
import net.contargo.iris.sequence.persistence.UniqueIdSequenceRepository;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.core.Is.is;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class UniqueIdSequenceServiceImplUnitTest {

    public static final String TERMINAL_ENTITY_NAME = "Terminal";
    UniqueIdSequenceServiceImpl sut;

    @Mock
    private UniqueIdSequenceRepository uniqueIdSequenceRepositoryMock;
    private BigInteger validId1;
    private BigInteger validId2;

    @Before
    public void setUp() {

        sut = new UniqueIdSequenceServiceImpl(uniqueIdSequenceRepositoryMock);
        validId1 = new BigInteger(UniqueIdSequence.SYSTEM_UNIQUEID_PREFIX + "000000000001");
        validId2 = new BigInteger(UniqueIdSequence.SYSTEM_UNIQUEID_PREFIX + "000000000002");
    }


    @Test
    public void testGetNextId() {

        UniqueIdSequence uniqueIdSequence = new UniqueIdSequence(TERMINAL_ENTITY_NAME, validId1);

        when(uniqueIdSequenceRepositoryMock.findByEntityName(TERMINAL_ENTITY_NAME)).thenReturn(uniqueIdSequence);

        assertThat(sut.getNextId(TERMINAL_ENTITY_NAME), is(validId1));

        ArgumentCaptor<UniqueIdSequence> captor = ArgumentCaptor.forClass(UniqueIdSequence.class);

        InOrder order = inOrder(uniqueIdSequenceRepositoryMock);
        order.verify(uniqueIdSequenceRepositoryMock).findByEntityName(TERMINAL_ENTITY_NAME);
        order.verify(uniqueIdSequenceRepositoryMock).save(captor.capture());

        assertThat(captor.getValue().getNextId(), is(validId2));
    }


    @Test(expected = UniqueIdSequenceServiceException.class)
    public void getNextIdForNonExistingEntity() {

        when(uniqueIdSequenceRepositoryMock.findByEntityName("non-existing-entity")).thenReturn(null);

        sut.getNextId(TERMINAL_ENTITY_NAME);

        verify(uniqueIdSequenceRepositoryMock).findByEntityName("non-existing-entity");
        verifyNoMoreInteractions(uniqueIdSequenceRepositoryMock);
    }


    @Test
    public void setNextId() {

        UniqueIdSequence sequenceFromDb = new UniqueIdSequence(TERMINAL_ENTITY_NAME, validId1);

        when(uniqueIdSequenceRepositoryMock.findByEntityName(TERMINAL_ENTITY_NAME)).thenReturn(sequenceFromDb);

        ArgumentCaptor<UniqueIdSequence> captor = ArgumentCaptor.forClass(UniqueIdSequence.class);

        sut.setNextId(TERMINAL_ENTITY_NAME, validId2);

        verify(uniqueIdSequenceRepositoryMock).save(captor.capture());

        assertThat(captor.getValue().getEntityName(), is(TERMINAL_ENTITY_NAME));
        assertThat(captor.getValue().getNextId(), is(validId2));
    }


    @Test
    public void checkValidity() {

        sut.checkValidity(validId1);
    }


    @Test(expected = UniqueIdSequenceServiceException.class)
    public void checkValidityWithTooShortId() {

        sut.checkValidity(new BigInteger("130100000000001"));
    }


    @Test(expected = UniqueIdSequenceServiceException.class)
    public void checkValidityWithIdHavingWrongPrefix() {

        sut.checkValidity(new BigInteger("1111000000000001"));
    }
}
