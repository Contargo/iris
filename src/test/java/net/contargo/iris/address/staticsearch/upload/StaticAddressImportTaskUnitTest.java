package net.contargo.iris.address.staticsearch.upload;

import net.contargo.iris.address.staticsearch.upload.file.StaticAddressFileService;
import net.contargo.iris.address.staticsearch.upload.service.AddressMailService;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportException;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportJobService;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportReport;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.InOrder;
import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticAddressImportTaskUnitTest {

    private StaticAddressImportTask sut;

    @Mock
    private StaticAddressImportJobService jobServiceMock;
    @Mock
    private StaticAddressImportService importServiceMock;
    @Mock
    private StaticAddressFileService fileServiceMock;
    @Mock
    private AddressMailService addressMailServiceMock;

    @Before
    public void setUp() {

        sut = new StaticAddressImportTask(jobServiceMock, importServiceMock, fileServiceMock, addressMailServiceMock);
    }


    @Test
    public void processNextJob() {

        StaticAddressImportJob job = new StaticAddressImportJob("user@example.com", "addresses.csv");
        InputStream reportData = new ByteArrayInputStream("".getBytes());
        StaticAddressImportReport report = new StaticAddressImportReport(reportData);

        when(importServiceMock.importAddresses(job)).thenReturn(report);
        when(jobServiceMock.getTopmostJob()).thenReturn(Optional.of(job));

        sut.processNextJob();

        InOrder inOrder = inOrder(importServiceMock, jobServiceMock, fileServiceMock, addressMailServiceMock);
        inOrder.verify(jobServiceMock).getTopmostJob();
        inOrder.verify(importServiceMock).importAddresses(job);
        inOrder.verify(addressMailServiceMock).sendSuccessMail("user@example.com", "addresses.csv", reportData);
        inOrder.verify(jobServiceMock).deleteJob(job);
        inOrder.verify(fileServiceMock).delete("addresses.csv");
    }


    @Test
    public void processNextJobEmpty() {

        when(jobServiceMock.getTopmostJob()).thenReturn(Optional.empty());

        sut.processNextJob();

        verify(jobServiceMock).getTopmostJob();
        verifyZeroInteractions(importServiceMock);
        verifyZeroInteractions(addressMailServiceMock);
        verifyNoMoreInteractions(jobServiceMock);
        verifyZeroInteractions(fileServiceMock);
    }


    @Test
    public void processNextJobException() {

        StaticAddressImportJob job = new StaticAddressImportJob("user@example.com", "addresses.csv");

        when(jobServiceMock.getTopmostJob()).thenReturn(Optional.of(job));
        doThrow(StaticAddressImportException.class).when(importServiceMock).importAddresses(job);

        sut.processNextJob();

        verify(addressMailServiceMock).sendErrorMail("user@example.com", "addresses.csv");
        verify(jobServiceMock).deleteJob(job);
        verify(fileServiceMock).delete("addresses.csv");
    }
}
