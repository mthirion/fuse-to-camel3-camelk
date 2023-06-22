package com.redhat.appfoundations.camel.test;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.quarkus.test.CamelQuarkusTestSupport;
import org.junit.jupiter.api.*;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CamelRouteTest extends CamelQuarkusTestSupport {

    @Test
    @Disabled
    public void testCamelRoute() throws Exception {

        Object o = template.requestBodyAndHeader("direct:", "body", "headername", "headervalue");

	    Assertions.assertNotNull(o);
    }
}
