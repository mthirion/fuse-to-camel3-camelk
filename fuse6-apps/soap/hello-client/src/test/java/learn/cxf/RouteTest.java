package learn.cxf;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;

import org.junit.Test;
import org.junit.Ignore;

public class RouteTest extends CamelBlueprintTestSupport {
	
    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/blueprint.xml";
    }

	@Test
	public void testRouteCxf() throws Exception{
		Object response = template.sendBody("cxf:bean:mycxf", ExchangePattern.InOut, "junit");
		assertEquals("ups", "[Hello junit]", response.toString());
	}

	@Test
	public void testRouteNetty() throws Exception
	{
		//prepare new request to destination Netty endpoint
		DefaultExchange request = new DefaultExchange(context);
		request.getIn().setHeader("CamelDestinationOverrideUrl", "http://localhost:11000/mycxfserver/hello");
		request.getIn().setBody("junit");
	
		//trigger the request via CXF to Netty
		Exchange response = template.send("cxf:bean:mycxf", request);

		//expect the 'hello' message
		assertEquals("ups", "Hello junit", response.getOut().getBody(String.class));
	}

	@Test
	public void testRouteCxfClientSimple() throws Exception{
		Object response = template.sendBody("direct:call-simple-data", ExchangePattern.InOut, "");
		assertEquals("ups", "[Hello cxf-client]", response.toString());
	}

	@Test
	public void testRouteCxfClientComplex() throws Exception{
		Object response = template.sendBody("direct:call-complex-data", ExchangePattern.InOut, "");
		assertEquals("ups", "[Bonjour Alexander the Great, King of Macedonia]", response.toString());
	}

	@Test
	public void testRouteCxfClientComplexWrapped() throws Exception{
		Object response = template.sendBody("direct:call-complex-data-wrapped", ExchangePattern.InOut, "");
		assertEquals("ups", "[Bonjour Alexander the Great, King of Macedonia]", response.toString());
	}

	@Test
	public void testRouteDataFormatSoap() throws Exception{

        //set expectations
        getMockEndpoint("mock:recover-soap").expectedMessageCount(1);
		getMockEndpoint("mock:recover-soap")
			.expectedBodiesReceived("Bonjour Alexander the Great, King of Macedonia");

		//trigger the process
		template.sendBody("direct:trigger-persist-soap","");

        //assert expectations
        assertMockEndpointsSatisfied();
	}

}
