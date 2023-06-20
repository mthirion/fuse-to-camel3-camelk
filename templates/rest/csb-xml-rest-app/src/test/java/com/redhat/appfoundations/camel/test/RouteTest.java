package com.redhat.appfoundations.camel.test;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

@SpringBootTest(
    webEnvironment = WebEnvironment.DEFINED_PORT
)
public class RouteTest {

    @LocalServerPort
    int serverPort;

    String path="/";

    @Test
    @Disabled
    public void testRestRoute() {

        String url = "http://localhost:"+serverPort + path;
        given()
                .when().get(url)
                .then()
                .statusCode(200)
                ;
    }
}
