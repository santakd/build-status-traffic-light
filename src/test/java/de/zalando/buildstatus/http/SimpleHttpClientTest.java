package de.zalando.buildstatus.http;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;

import static org.junit.Assert.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class SimpleHttpClientTest {

    private ClientAndServer mockServer;

    @Before
    public void setup() {
        mockServer = ClientAndServer.startClientAndServer(8081);
    }

    @After
    public void tearDown() {
        mockServer.stop();
    }

    @Test
    public void sendRequestWithBasicAuth() {

        final String expectedResponse = "hello john";
        final String path = "/test";

        mockServer.when(
                request().withPath(path).withHeader("Authorization: Basic am9obmRvZTpwYXNzd29yZA==")).respond(
                response().withStatusCode(200).withBody(expectedResponse));

        String response = new SimpleHttpClient().sendGetWithBasicAuth("http://localhost:8081" + path, "johndoe",
                "password", false);
        assertEquals(expectedResponse, response);
    }

    @Test
    public void ifAuthInfoIsNotSpecifiedAuthHeaderShouldNotBeAddedToRequest() {

        final String expectedResponse = "hello world";
        final String path = "/test";

        mockServer.when(
                request().withPath(path)).respond(
                response().withStatusCode(200).withBody(expectedResponse));

        new SimpleHttpClient().sendGetWithBasicAuth("http://localhost:8081" + path, null, null, false);

        HttpRequest[] actualRequests = mockServer.retrieveRecordedRequests(request().withPath(path));
        assertEquals(1, actualRequests.length);
        assertEquals("", actualRequests[0].getFirstHeader("Authorization"));
    }
}
