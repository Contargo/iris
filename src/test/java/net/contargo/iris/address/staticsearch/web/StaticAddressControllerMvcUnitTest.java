package net.contargo.iris.address.staticsearch.web;

import net.contargo.iris.address.staticsearch.StaticAddress;
import net.contargo.iris.address.staticsearch.service.StaticAddressCoordinatesDuplicationException;
import net.contargo.iris.address.staticsearch.service.StaticAddressDuplicationException;
import net.contargo.iris.address.staticsearch.service.StaticAddressService;
import net.contargo.iris.address.staticsearch.upload.StaticAddressImportJob;
import net.contargo.iris.address.staticsearch.upload.file.StaticAddressFileService;
import net.contargo.iris.address.staticsearch.upload.file.StaticAddressFileStorageException;
import net.contargo.iris.address.staticsearch.upload.service.StaticAddressImportJobService;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.ArgumentCaptor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;

import org.springframework.mock.web.MockMultipartFile;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import static net.contargo.iris.Message.MessageType.ERROR;
import static net.contargo.iris.Message.MessageType.SUCCESS;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Unit tests for controller {@link StaticAddressController}.
 *
 * @author  Michael Herbold - herbold@synyx.de
 * @author  Sandra Thieme - thieme@synyx.de
 * @author  Jörg Alberto Hoffmann - hoffmann@synyx.de
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:web-api-context.xml" })
@WebAppConfiguration
public class StaticAddressControllerMvcUnitTest {

    private static final long ID = 1L;
    private static final String CITY = "City";
    private static final String POSTALCODE = "76137";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private StaticAddressService staticAddressServiceMock;
    @Autowired
    private StaticAddressFileService staticAddressFileServiceMock;
    @Autowired
    private StaticAddressImportJobService importJobServiceMock;

    @Before
    public void setUp() {

        reset(staticAddressServiceMock, staticAddressFileServiceMock, importJobServiceMock);
    }


    @Test
    public void testGetAddressesByDetails() throws Exception {

        List<StaticAddress> staticAddresses = new ArrayList<>();
        staticAddresses.add(new StaticAddress());

        when(staticAddressServiceMock.getAddressesByDetailsWithFallbacks("12345", "karlsruhe", null)).thenReturn(
            staticAddresses);

        MockHttpServletRequestBuilder builder = get("/staticaddresses/?postalcode=12345&city=karlsruhe");

        ResultActions resultActions = perform(builder).andExpect(status().isOk());
        resultActions.andExpect(model().attribute("staticAddresses", staticAddresses));
    }


    @Test
    public void searchRequestEmpty() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = get("/staticaddresses/");

        ResultActions resultActions = perform(requestBuilder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("staticAddresses", nullValue()));

        verify(staticAddressServiceMock, never()).getAddressesByDetailsWithFallbacks(null, null, null);
    }


    @Test
    public void getStaticAddress() throws Exception {

        when(staticAddressServiceMock.findById(1L)).thenReturn(new StaticAddress());

        MockHttpServletRequestBuilder requestBuilder = get("/staticaddresses/1");

        ResultActions resultActions = perform(requestBuilder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("staticAddress", new StaticAddress()));
    }


    @Test
    public void prepareCreateStaticAddress() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = get("/staticaddresses/new");

        ResultActions resultActions = perform(requestBuilder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("staticAddress", any(StaticAddress.class)));
    }


    @Test
    public void createStaticAddress() throws Exception {

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setPostalcode(POSTALCODE);
        staticAddress.setCity(CITY);
        staticAddress.setCityNormalized(CITY.toUpperCase());
        staticAddress.setLatitude(BigDecimal.ONE);
        staticAddress.setLongitude(BigDecimal.ONE);

        StaticAddress savedStaticAddress = new StaticAddress();
        savedStaticAddress.setId(ID);

        when(staticAddressServiceMock.saveStaticAddress(staticAddress)).thenReturn(savedStaticAddress);

        MockHttpServletRequestBuilder builder = post("/staticaddresses/");
        builder.flashAttr("staticAddress", staticAddress);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("staticAddress", savedStaticAddress));
        resultActions.andExpect(model().attribute("message",
                hasProperty("message", is("staticaddress.save.success"))));

        verify(staticAddressServiceMock).saveStaticAddress(staticAddress);
    }


    @Test
    public void createStaticAddressWithDuplicate() throws Exception {

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setPostalcode(POSTALCODE);
        staticAddress.setCity(CITY);
        staticAddress.setCityNormalized(CITY.toUpperCase());
        staticAddress.setLatitude(BigDecimal.ONE);
        staticAddress.setLongitude(BigDecimal.ONE);

        when(staticAddressServiceMock.saveStaticAddress(staticAddress)).thenThrow(
            new StaticAddressDuplicationException());

        MockHttpServletRequestBuilder builder = post("/staticaddresses/");
        builder.flashAttr("staticAddress", staticAddress);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("staticAddress", staticAddress));
        resultActions.andExpect(model().attribute("message",
                hasProperty("message", is("staticaddress.error.duplicate"))));

        verify(staticAddressServiceMock).saveStaticAddress(staticAddress);
    }


    @Test
    public void createStaticAddressWithDuplicateCoordinates() throws Exception {

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setPostalcode(POSTALCODE);
        staticAddress.setCity(CITY);
        staticAddress.setCityNormalized(CITY.toUpperCase());
        staticAddress.setLatitude(BigDecimal.ONE);
        staticAddress.setLongitude(BigDecimal.ONE);

        when(staticAddressServiceMock.saveStaticAddress(staticAddress)).thenThrow(
            new StaticAddressCoordinatesDuplicationException());

        MockHttpServletRequestBuilder requestBuilder = post("/staticaddresses/");
        requestBuilder.flashAttr("staticAddress", staticAddress);

        ResultActions resultActions = perform(requestBuilder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("staticAddress", staticAddress));
        resultActions.andExpect(model().attribute("message",
                hasProperty("message", is("staticaddress.error.coordinates.duplicate"))));

        verify(staticAddressServiceMock).saveStaticAddress(staticAddress);
    }


    @Test
    public void createStaticAddressWithErrors() throws Exception {

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setPostalcode(POSTALCODE);
        staticAddress.setCity(CITY);
        staticAddress.setCityNormalized(CITY.toUpperCase());

        MockHttpServletRequestBuilder builder = post("/staticaddresses/");
        builder.flashAttr("staticAddress", staticAddress);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("staticAddress", staticAddress));

        verify(staticAddressServiceMock, never()).saveStaticAddress(staticAddress);
    }


    @Test
    public void updateStaticAddress() throws Exception {

        StaticAddress staticAddress = new StaticAddress();
        staticAddress.setPostalcode(POSTALCODE);
        staticAddress.setCity(CITY);
        staticAddress.setCityNormalized(CITY.toUpperCase());
        staticAddress.setId(ID);
        staticAddress.setLatitude(BigDecimal.ONE);
        staticAddress.setLongitude(BigDecimal.ONE);

        when(staticAddressServiceMock.saveStaticAddress(staticAddress)).thenReturn(staticAddress);

        MockHttpServletRequestBuilder builder = put("/staticaddresses/1");
        builder.flashAttr("staticAddress", staticAddress);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(model().attribute("staticAddress", staticAddress));
        resultActions.andExpect(model().attribute("message",
                hasProperty("message", is("staticaddress.update.success"))));

        verify(staticAddressServiceMock).saveStaticAddress(staticAddress);
    }


    @Test
    public void getImportForm() throws Exception {

        ResultActions resultActions = perform(get("/staticaddresses/import"));
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(view().name("staticAddressManagement/importForm"));
    }


    @Test
    public void importSuccess() throws Exception {

        MockMultipartFile addressList = new MockMultipartFile("file", "addresses.csv", MediaType.TEXT_PLAIN_VALUE,
                "postalcode;city;".getBytes());
        MockMultipartHttpServletRequestBuilder builder = fileUpload("/staticaddresses/import");
        builder.file(addressList);
        builder.param("email", "a@b.de");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().is3xxRedirection());
        resultActions.andExpect(flash().attribute("message", hasProperty("type", is(SUCCESS))));

        verify(staticAddressFileServiceMock).saveFile(addressList);

        ArgumentCaptor<StaticAddressImportJob> captor = ArgumentCaptor.forClass(StaticAddressImportJob.class);
        verify(importJobServiceMock).addJob(captor.capture());

        StaticAddressImportJob job = captor.getValue();
        assertThat(job.getEmail(), is("a@b.de"));
        assertThat(job.getCsvPath(), is("addresses.csv"));
    }


    @Test
    public void importFail() throws Exception {

        MockMultipartFile addressList = new MockMultipartFile("file", "postalcode;city;".getBytes());
        MockMultipartHttpServletRequestBuilder builder = fileUpload("/staticaddresses/import");
        builder.file(addressList);
        builder.param("email", "a@b.de");

        doThrow(StaticAddressFileStorageException.class).when(staticAddressFileServiceMock).saveFile(addressList);

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().is3xxRedirection());
        resultActions.andExpect(flash().attribute("message", hasProperty("type", is(ERROR))));
    }


    @Test
    public void importWithoutEmail() throws Exception {

        MockMultipartFile addressList = new MockMultipartFile("file", "postalcode;city;".getBytes());
        MockMultipartHttpServletRequestBuilder builder = fileUpload("/staticaddresses/import");
        builder.file(addressList);
        builder.param("email", "");

        ResultActions resultActions = perform(builder);
        resultActions.andExpect(status().is2xxSuccessful());
        resultActions.andExpect(model().attribute("message", hasProperty("type", is(ERROR))));

        verifyZeroInteractions(staticAddressFileServiceMock);
    }


    private ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {

        return webAppContextSetup(webApplicationContext).build().perform(builder);
    }
}
