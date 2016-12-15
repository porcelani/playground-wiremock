package com.porcelani;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.given;

public class WireMockTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9080);

    @Test
    public void should_mock_a_simnple_rest_call() throws Exception {

        wireMockRule.stubFor(get(urlEqualTo("/my/resource"))
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")));

        given()
                .header("Accept", "text/xml")
                .header("Content-Type", "text/xml")
                .body("<message>1234</message>")
                .expect()
                .statusCode(200)
                .when()
                .get("http://localhost:9080/my/resource");

        verify(getRequestedFor(urlMatching("/my/resource"))
                .withRequestBody(matching(".*1234.*"))
                .withHeader("Content-Type", notMatching("application/json")));
    }

}
