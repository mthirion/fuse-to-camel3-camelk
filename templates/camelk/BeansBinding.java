import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.BindToRegistry;
import org.apache.camel.Processor;

// ADD IMPORT IF NEEDED
//import <my-dependency-packages>.*;

public class BeansBinding extends RouteBuilder {
    @Override
    public void configure() throws Exception {
    }



    // ADD YOUR BEANS HERE
    @BindToRegistry("my-bean-id")
    public static TYPE camelbean() {
        return new TYPE();
    }


}
