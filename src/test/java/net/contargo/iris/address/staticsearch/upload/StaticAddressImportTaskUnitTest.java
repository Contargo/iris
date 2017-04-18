package net.contargo.iris.address.staticsearch.upload;

import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportException;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportJobService;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticAddressImportTaskUnitTest {

    private StaticAddressImportTask sut;

    @Mock
    private StaticAddressImportJobService jobServiceMock;
    @Mock
    private StaticAddressImportService importServiceMock;

    @Before
    public void setUp() {

        sut = new StaticAddressImportTask(jobServiceMock, importServiceMock);
    }


    @Test
    public void processNextJob() {

        StaticAddressImportJob job = new StaticAddressImportJob("user@example.com", "addresses.csv");

        when(jobServiceMock.getTopmostJob()).thenReturn(Optional.of(job));

        sut.processNextJob();

        verify(importServiceMock).importAddresses(job);
        verify(jobServiceMock).deleteJob(job);
    }


    @Test
    public void processNextJobEmpty() {

        when(jobServiceMock.getTopmostJob()).thenReturn(Optional.empty());

        sut.processNextJob();

        verifyZeroInteractions(importServiceMock);
        verify(jobServiceMock, never()).deleteJob(any());
    }


    @Test
    public void processNextJobException() {

        StaticAddressImportJob job = new StaticAddressImportJob("user@example.com", "addresses.csv");

        when(jobServiceMock.getTopmostJob()).thenReturn(Optional.of(job));
        doThrow(StaticAddressImportException.class).when(importServiceMock).importAddresses(job);

        sut.processNextJob();

        verify(jobServiceMock, never()).deleteJob(any());
    }
}
