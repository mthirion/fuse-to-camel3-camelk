package com.mycompany.camel.https.securityHandlers;

import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends RouteBuilder {
	/**
	 * Let's configure the Camel routing rules using Java code...
	 */
	@Override
	public void configure() {
		System.out.println("......Inside MyRouteBuilder.....");
		from("jetty:https://0.0.0.0:9090/myapp/myservice/").log("UK message");
	}
}