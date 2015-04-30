package net.contargo.iris.startup;

import net.contargo.iris.address.staticsearch.service.StaticAddressService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.context.event.ContextRefreshedEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * @author  Arnold Franke - franke@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class IrisStartupWatcherUnitTest {

    private IrisStartupWatcher sut;
    @Mock
    private StaticAddressService staticAddressServiceMock;

    @Before
    public void setUp() {

        sut = new IrisStartupWatcher(staticAddressServiceMock);
    }


    @Test
    public void testOnApplicationEvent() {

        ContextRefreshedEvent eventMock = mock(ContextRefreshedEvent.class);
        sut.onApplicationEvent(eventMock);
        verify(staticAddressServiceMock, times(1)).fillMissingHashKeys();
        sut.onApplicationEvent(eventMock);
        verify(staticAddressServiceMock, times(1)).fillMissingHashKeys();
    }
}
