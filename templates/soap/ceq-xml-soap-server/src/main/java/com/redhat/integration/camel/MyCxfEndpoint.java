package com.redhat.integration.camel;


import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.component.cxf.common.DataFormat;
import org.apache.camel.component.cxf.jaxws.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.oracle.svm.core.annotate.Inject;

import javax.inject.Named;
import javax.xml.namespace.QName;

@Named("mycxfendpoint")
@ApplicationScoped
public class MyCxfEndpoint extends CxfEndpoint {

    @Inject
    @ConfigProperty(name="my.cxf.serviceclass")
    String serviceclass;

    @Inject
    @ConfigProperty(name="my.cxf.wsdlnamespace")
    String wsdlnamespace;

    @Inject
    @ConfigProperty(name="my.cxf.wsdlservice")
    String wsdlservice;
    
    @Inject
    @ConfigProperty(name="my.cxf.wsdlendpoint")
    String wsdlendpoint;    

    @Inject
    @ConfigProperty(name="my.cxf.localaddress-bean")
    String address;    

    @Inject
    @ConfigProperty(name="my.cxf.dataformat")
    String dataformat;  


    public MyCxfEndpoint() {
        super();
        
    }

    @PostConstruct
    private void configure() throws ClassNotFoundException {

        if (serviceclass != null)
            this.setServiceClass(serviceclass);

        if (wsdlservice != null) {
            QName serviceQname = new QName(wsdlnamespace, wsdlservice);
            this.setServiceNameAsQName(serviceQname);   
        }
        if (wsdlendpoint != null) {
            QName endpointQname = new QName(wsdlnamespace, wsdlendpoint);
            this.setEndpointNameAsQName(endpointQname);
        }

        this.setDataFormat(DataFormat.valueOf(dataformat));
        this.setAddress(address);
    }


}
