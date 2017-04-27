package net.contargo.iris.address.staticsearch.upload.service;

import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;
import net.contargo.iris.address.staticsearch.upload.persistence.StaticAddressImportJobRepository;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Oliver Messner - messner@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticAddressImportJobServiceImplUnitTest {

    private StaticAddressImportJobServiceImpl sut;

    @Mock
    private StaticAddressImportJobRepository repositoryMock;

    private StaticAddressImportJob job;

    @Before
    public void setUp() {

        sut = new StaticAddressImportJobServiceImpl(repositoryMock);

        job = new StaticAddressImportJob("user@example.com", "bar.csv");
    }


    @Test
    public void addJob() {

        sut.addJob(job);

        verify(repositoryMock).save(job);
    }


    @Test
    public void getTopmostJob() {

        when(repositoryMock.findFirstByOrderByIdAsc()).thenReturn(Optional.of(job));

        assertThat(sut.getTopmostJob(), is(Optional.of(job)));
    }


    @Test
    public void deleteJob() {

        sut.deleteJob(job);

        verify(repositoryMock).delete(job);
    }
}
