package com.redhat.integration.camel;

import java.util.*;

import javax.enterprise.context.ApplicationScoped;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingInInterceptor;
import org.apache.cxf.ext.logging.LoggingOutInterceptor;
import org.apache.cxf.frontend.AbstractWSDLBasedEndpointFactory;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ConfigurationConstants;
import javax.inject.Named;

import org.apache.camel.component.cxf.jaxws.CxfConfigurer;

@Named("mycxfconfigurer")
@ApplicationScoped
public class MyCxfConfigurer implements CxfConfigurer {

    public MyCxfConfigurer() {
        super();
    }

    @Override
    public void configure(AbstractWSDLBasedEndpointFactory factoryBean) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'configure'");
    }

    @Override
    public void configureClient(Client client) {
       
        //addClientInterceptors();

        //throw new UnsupportedOperationException("Unimplemented method 'configureClient'");
    }

    @Override
    public void configureServer(Server server) {
        
        //addServerInterceptors();

        //throw new UnsupportedOperationException("Unimplemented method 'configureServer'");
    }






    /* CUSTOM METHOD -- EXAMPLE OF ADDING INTERCEPTORS */
    private void addServerInterceptors(Server server) {
        Map<String,Object> wsproperties = new HashMap<String, Object>();
        wsproperties.put(ConfigurationConstants.ACTION, ConfigurationConstants.USERNAME_TOKEN_NO_PASSWORD);
        wsproperties.put(ConfigurationConstants.ALLOW_USERNAMETOKEN_NOPASSWORD, "true");
        WSS4JInInterceptor wssecurity = new WSS4JInInterceptor(wsproperties);  
        server.getEndpoint().getInInterceptors().add(wssecurity);
        

        LoggingInInterceptor loggingin = new LoggingInInterceptor();
        server.getEndpoint().getInInterceptors().add(loggingin);
    }

    private void addClientInterceptors(Client client) {
        Map<String,Object> wsproperties = new HashMap<String, Object>();
        wsproperties.put(ConfigurationConstants.ACTION, ConfigurationConstants.USERNAME_TOKEN_NO_PASSWORD);
        //wsproperties.put(ConfigurationConstants.ALLOW_USERNAMETOKEN_NOPASSWORD, "true");
        wsproperties.put(ConfigurationConstants.USER, "test");
        wsproperties.put(ConfigurationConstants.MUST_UNDERSTAND, "0");
        WSS4JOutInterceptor wssecurity = new WSS4JOutInterceptor(wsproperties);  
        client.getOutInterceptors().add(wssecurity);

        LoggingOutInterceptor loggingin = new LoggingOutInterceptor();
        client.getOutInterceptors().add(loggingin);   
    }
}

