package com.redhat.appfoundations.camel.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.*;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

@ActiveProfiles("production")
@CamelSpringBootTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CamelRouteTest {

    @Autowired
    private ProducerTemplate template;

    @Test
    @Disabled
    public void testCamelRoute() throws Exception {

        Object o = template.requestBodyAndHeader("direct:", null, "headername", "headervalue");

        assertNotNull(o);
    }
}
