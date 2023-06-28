# Migrating a SOAP client and server from CXF
Link to the video blog: 



## Introduction
The templates in use are those in templates/soap.  
The instructions here below will focus on the migration of a SOAP server only, as the process is exactly similar for a SOAP client.  
Notice however that, as described below, the new Camel SOAP implementation proposes 2 kind of definitions, one based on a bean and one based on a direct address.  The migration of the server, described below, will make use of the bean version, as this is the one existing in Fuse 6.  However, in the case of a SOAP client, the use of a direct address simplifies and makes the configuration easier.  

### Fuse 6 source application
The source application is a SOAP version of the claimDemo application.  It's a SOAP Server exposing the claim service at the below endpoint:  
http://localhost:8182/cxf/claim

It expects the below SOAP payload:  
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:cxf="http://cxf.learn/">  
  <soapenv:Header/>
   <soapenv:Body>>  
      <cxf:status>
        <cxf:getbyid>?</cxf:getbyid>
      </cxf:status>  
   </soapenv:Body>  
</soapenv:Envelope>  
```  

And would return the below data:  


The corresponding WSDL is store in the src/main/resources/wsdl folder of the OSGI bundle.  


### Structure of a cxf application
Leveraging OSGI and Blueprint, the Camel application is constructed from 3 major elements:
- OSGI-INF/Blueprint.xml  
This is the main file.  
It contains the Camel route that
  - starts with a cxf:// endpoint whose definition is in a cxfendpoint section in the header of the same file  
  - uses a custom Camel processor
- Java files, and among them:
  - the custom Processor referenced in the route
  - SOAP Request and Response object, generated from the wsdl
- Fabric8 pid file  
This contains external configuration.  

![blueprint](images/cxf-blueprint.png?raw=true)
 
_Note about the CXF engine_   
1) The CXF web server engine had a fixed /cxf context path.  
And the Fabric8 platform exposed the web services behind the port 8182 by default.  
To make the migration as transparent as possible, the templates will use those same port and context path.  
Notice that, with Openshift as the target deployment platform, the exposed port should become 8080 as it is the convention for HTTP-based microservices.  
2) In pure Java applications, CXF endpoints are usually defined with a 'jaxws:endpoint', for the server, or a 'jaxws:client' one, for the client.  Apache Camel simplified the configuration by providing a single element for both clients and servers: the 'cxfendpoint' element.  
We consider here a Camel application, thus it uses a cxfendpoint entry.  

### Camel 3 and SOAP support
CXF has been ported to Camel 3.  
As usual, the runtime leverages Java objects, that can be automatically generated from the WSDL using the wsdl2java tool, embedded into the templates in the CXF Codegen pmaven lugin.  
The new version accepts 2 forms of URI:  
- URI with a bean  
This works in a very similar way as in Fuse 6, where a bean is a central point to contain all the CXF parameters.  
- URI with an address  
No bean is required and all the parameters can be set inline directly, as URI options.  

As Fuse 6 uses beans, we'll use the bean version as well.  The only mandatory options required to configure the CXF runtime with a bean is the service class and address.  The service class is one of the Java objects generated from the WSDL.  In the case of a SOAP server, the address is the relative address of the service (in the case of a SOAP client, that will be a fully qualified absolute address).  
As an alternative to the service class, it would possible to use a combination of:
- the wsdl location
- the service name in the WSDL, unless it's unique
- the endpoint name in the WSDL, unless there is only one for the selected service name

### DataFormats
The example application uses Camel 'marshal' and 'unmarshal' operations to convert the SOAP XML payload to and from Java.  The data format in use is soapjaxb, and is declared outside of the Camel route.  
With the IO XML format, nothing can be defined outside of the route, thus the dataformat must be moved inside the marshal/unmarshal tags.  Also, the soapjaxb data format has been renamed to 'soap'.  
So, the below conversion must occur both for the marshall and unmarshal method:  
```
  <dataFormats>
          <soapjaxb id="soap11" contextPath="learn.cxf" version="1.1"/>
  </dataFormats>

  <route>
  ...
    <unmarshal> <custom ref="soap11"/></unmarshal>
  ...
  </route>
```

```
  <route>
  ...
    <unmarshal> 
      <soap contextPath="learn.cxf" version="1.1"/>
    </unmarshal>
  ...
  </route>
```

## Migration of a CXF Server to Springboot
In this version of the demo, the Fuse 6 application is going to be migrated toward Camel 3.18 on Springboot 2.76. 

The Camel on Springboot templates uses the below placeholders:   
- SpringBootApplication.java  
It contains the bootstrap class.   
- camel/route(s).xml  
To contain the Camel XML route(s) in NIO XML format.  
The routes will be automatically discovered by the Springboot runtime. 
This specific XML format can only contain Camel Routes, so the other part of the Blueprint XML (beans...) will go somewhere else. 
- spring/beans.xml  
This is where the beans references can be defined.  It's easier to migrate the bean definition directly in XML as they are already defined in XML in the Fuse 6 source application.  
This same file will also contain the declaration of the CXF server bean.
- application.properties  
This is the unique configuration file.  
- wsdl  
This is where the wsdl2java plugin will look for the wsdl files and its associated external XSD.  


![springboot](images/soap-springboot.png?raw=true)
 

### Springboot migration process
The migration process involves the below tasks (in random order):
- Copy of the wsdl file to the src/main/resources/wsdl folder of the template.  
You must ensure that the name of the WSDL matches the expected file in the maven pluging definition of the pom.xml, defaulting to 'soapservice.wsdl'  
- Generating the Java objects from the wsdl with the plugin:  
_mvn generate-sources_  
- Copying the fabric8 pid properties to the application.properties file
- Copying the 'route' section from the blueprint.xml to the route.xml file  
- Copying the 'cxfendpoint' declaration to the beans.xml file  
Make sure the name of the bean in the Camel 'from' matches the bean id  
- Moving the DataFormat declaration inline, and replacing the soapjaxb name by 'soap'  
- Filling out the service class and relative address in the application.properties file  
- Copying the custom java objects/packages to src/main/java
- Placing the custom Processor bean registration to the beans.xml file  
- Making sure the CXF dataformat property in the application.properties matches your need.  

![migrate-sb](images/migration-springboot-soap.png?raw=true)


### Check the migrated app

mvn clean package  
mvn spring-boot:run  

Test with, for example, SOAPUI.


## Migration of a CXF Server to Quarkus
In this version of the demo, the Fuse 6 application is going to be migrated toward Camel 3.18 on Quarkus 2.13.5.    

Compared to the Springboot migration described above, the Camel on Quarkus template still relies on the route(s).xml and application.properties file, but releases the SpringBootApplication.java class as well as the beans.xml file.  
Custom beans now have to be directly annotated with @Named("beanid") and @ApplicationScoped.  
The cxfendpoint is represented by a bean of type CxfEndpoint whose id is 'mycxfservice', and whose class is in the src/main/java folder.  
All the properties of this CxfEndpoint bean have been externalized and have entries in the application.properties file.  It's therefore not needed to manipulate this Java class directy; setting the right value in the application.properties will suffice.  


![quarkus](images/soap-quarkus.png?raw=true)

### Quarkus migration process

The migration process involves (in random order):  
- Generating the Java objects from the wsdl with the plugin  
  - Copy of the wsdl file to the src/main/resources/wsdl folder of the template.  
  - You must ensure that the name of the WSDL matches the expected file in the maven pluging definition of the pom.xml, defaulting to 'soapservice.wsdl'  
  - _mvn generate-sources_  
- Copying the fabric8 pid properties to the application.properties file
- Copying the 'route' section from the blueprint.xml to the route.xml file  
- Making sure that the name of the bean in the Camel 'from' matches 'mycxfservice'; otherwise the bean id can be changed in the MyCxfEndpoint java file  
- Moving the DataFormat declaration inline, and replacing the 'soapjaxb' by 'soap'  
- Filling out the required properties (service class, relative address, service name, endpoint name... in the application.properties file from the value indicated in the cxfendpoint of the source application's blueprint.xml  
- Copying the custom java objects/packages to src/main/java
- Annotating referenced Camel beans/processors with @Named and @ApplicationScoped  
- Making sure the CXF dataformat property in the application.properties matches your need.  

![migrate-qk](images/migration-quarkus-soap.png?raw=true)

### Choice between Bean and URI format
As there is no beans.xml file in Quarkus, the cxfendpoint located in the Blueprint XML will have to be transformed into a Java object.  
And because of this little refactoring phase, a choice exists in  using the bean or the address URI format.  
We would prefer the use of the bean format for the server side, as Fuse 6 CXF applications already uses a similar format.
For the CXF client, it's however more intuitive to use the target service URL.  Another perspective to help make that choice it to say that it's better to have the full capabilities of a Java bean to confiure a SOAP service that we own (as we're exposing it), but we don't need any extra level of complexity to call a remote SOAP service whose confiuration is outside of our control.

![using-address-mode](images/soap-address.png?raw=true)

### Testing the migrated app
mvn clean package  
mvn quarkus:dev  

Tes, for example, with SOAPUI.  


## WS-Security and other SOAP features
WS-Security and other SOAP features are implemented by CXF via Interceptors.  
To configure CXF interceptors, a CxfConfigurer bean is needed and needs to be referenced in the Camel cxf endpoint.  
For more information about that, please have a look at:  
https://developers.redhat.com/articles/2023/03/28/how-configure-soap-web-services-apache-camel  


## Readiness

### Unit Testing  
Both templates (springboot and quarkus) contain a structured placeholder to fill and run Camel 3 compatible unit tests.   

### Openshift  
Both templates (springboot and quarkus) contain important Openshift-related configuration such as:
- Runtime and Camel healthchecks  
- Prometheus-compatible runtime and Camel metrics 
- Automatic discovery of ConfigMap for the application.properties file
- Openshift deployments plugins  

Deploy the springboot version to Openshift:  
_mvn install -Popenshift_ 

Deploy the quarkus to Openshift:  
_mvn clean package -Popenshift -Dquarkus.kubernetes.deploy=true -Dquarkus.kubernetes-client.trust-certs=true -Dquarkus.openshift.route.expose=true


