package net.contargo.iris.connection.advice;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.Mock;

import org.mockito.runners.MockitoJUnitRunner;

import static net.contargo.iris.route.RouteDirection.EXPORT;
import static net.contargo.iris.route.RouteDirection.IMPORT;
import static net.contargo.iris.route.RouteProduct.ONEWAY;
import static net.contargo.iris.route.RouteProduct.ROUNDTRIP;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.is;


/**
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 */
@RunWith(MockitoJUnitRunner.class)
public class MainRunAdvisorUnitTest {

    private MainRunAdvisor sut;

    @Mock
    private MainRunStrategy mainRunRoundTripImportAdviceMock;
    @Mock
    private MainRunStrategy mainRunRoundTripExportAdviceMock;
    @Mock
    private MainRunStrategy mainRunOneWayImportAdviceMock;
    @Mock
    private MainRunStrategy mainRunOneWayExportAdviceMock;

    @Before
    public void before() {

        sut = new MainRunAdvisor(mainRunRoundTripImportAdviceMock, mainRunRoundTripExportAdviceMock,
                mainRunOneWayImportAdviceMock, mainRunOneWayExportAdviceMock);
    }


    @Test
    public void testMainRunAdviceWithRoundTripImport() {

        assertThat(sut.advice(ROUNDTRIP, IMPORT), is(mainRunRoundTripImportAdviceMock));
    }


    @Test
    public void testMainRunAdviceWithRoundTripExport() {

        assertThat(sut.advice(ROUNDTRIP, EXPORT), is(mainRunRoundTripExportAdviceMock));
    }


    @Test
    public void testMainRunAdviceWithOneWayImport() {

        assertThat(sut.advice(ONEWAY, IMPORT), is(mainRunOneWayImportAdviceMock));
    }


    @Test
    public void testMainRunAdviceWithOneWayExport() {

        assertThat(sut.advice(ONEWAY, EXPORT), is(mainRunOneWayExportAdviceMock));
    }
}
