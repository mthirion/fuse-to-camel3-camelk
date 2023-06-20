# Migrating a SOAP client and server from CXF
Link to the video blog: 


## Introduction
The templates in use are those in skeletons/soap.  
### Fuse 6 source application
The source application is simply the SOAP version of the claimDemo application, also from the Red Hat Learning System.  

It expects a SOAP payload like the following:  

And would return the below info:  




### Structure of a cxf application
Leveraging OSGI and Blueprint, the Camel application is constructed from 3 major elements:
- OSGI-INF/Blueprint.xml  
This is the main file.  
It contains the Camel route that
  - starts with a cxf:// endpoint whose definition is in a cxfendpoint section i the header of the file  
  - references Java beans declared in separate files
- Java files, and among them:
  - the java beans referenced in the route
  - SOAP Request and Response object, generated from the wsdl
- Fabric8 pid file  
This contains external configuration.  

![blueprint](images/cxf-blueprint.png?raw=true)
 
_Note about the CXF engine_   
1) The CXF web server engine had a fixed /cxf context path.  
And the Fabric8 platform exposed the web services behind the port 8182 by default.  
To keep everything similar for local testing, appropriate properties have been added to the application.properties file of the templates.
Notice that, with Openshift as the target deployment platform, the exposed port should become 8080 as it is the convention of the platform for HTTP-based microservices.  
2) In pure Java applications, CXF endpoints are usually defined with a 'jaxws:endpoint', for the server, or a 'jaxws:client' one, for the client.  Apache Camel simplified the configuration by providing a single element for both clients and servers: the 'cxfendpoint' element.  
We consider here a Camel application, thus it uses a cxfendpoint entry.  

### Camel 3 and SOAP support
CXF has been ported to Camel 3.  
As usual, the runtime leverages Java objects generated from the WSDL using the wsdl2java tool, which is available as a goal of the CXF Codegen plugin.  
The new version accepts 2 forms of URI:  
- URI with a bean  
This works in a very similar way as in Fuse 6, where a bean is a central point to contain all the CXF parameters.  
- URI with an address  
No bean is required and all the parameters can be set in the URI directly  

To reduce the migration effort, we'll use the URI with bean version.  The only mandatory option required to configure the CXF runtime with a bean is the service class.  The service class is a Java object generated from the WSDL. 

Just note that, as an alternative, it would have been possible to use a combination of:
- the wsdl location
- the service name in the WSDL, unless it's unique
- the endpoint name in the WSDL, unless there is only one for the selected service name

## Migration to Springboot
In this version of the demo, the Fuse 6 application is going to be migrated toward Camel 3.18 on Springboot 2.76.  6

The Camel on Springboot templates uses the below placeholders:   
- SpringBootApplication.java  
It contains the bootstrap class.   
- camel/route(s).xml  
To contain the Camel XML route(s) in NIO XML format.  
The routes will be automatically discovered by the Springboot runtime. 
This specific XML format can only contain Camel Routes, so the other part of the Blueprint XML (beans...) will go somewhere else. 
- spring/beans.xml  
This is where the beans references can be defined.  It's easier to migrate the bean definition directly in XML as they are already defined in XML in the Fuse 6 source application.  
- application.properties  
This is the unique configuration file.  
- wsdl  
This is where the wsdl2java plugin will look for the wsdl files and its associated external XSD.  


![springboot](images/soap-springboot.png?raw=true)
 

### Springboot migration process
The migration process is similar for a CXF server and a CXF client, and involves (in random order):
- generating the Java objects from the wsdl  
_mvn generate-sources_  
- moving the fabric8 pid properties to the application.properties file
- moving the whole camel route section to the route.xml file  
- moving the java objects/packages to src/main/java
- moving the bean declaration to the beans.xml file  
- moving the cxfendpoint declaration to the beans.xml file as well    

![migrate-sb](images/migration-springboot-soap.png?raw=true)


### Check the migrated app

mvn clean package  
mvn spring-boot:run  

curl ...


## Migration to Quarkus
In this version of the demo, the Fuse 6 application is going to be migrated toward Camel 3.18 on Quarkus 2.13.5.    

Compared to the Springboot migration described above, the Camel on Quarkus template still relies on the route(s).xml, rest.xml and application.properties file, but releases the SpringBootApplication.java class as well as the beans.xml file.  The custom beans now have to be directly annotated with @Named("beanid") and @ApplicationScoped.

![quarkus](images/soap-quarkus.png?raw=true)

### Quarkus migration process

The migration process involves (in random order):  
- generating the Java objects from the wsdl  
_mvn generate-sources_  
- moving the fabric8 pid properties to the application.properties file
- moving the whole camel route section to the route.xml file  
- moving the java objects/packages to src/main/java
- add an @Named & @ApplicationScoped annotations to Java classes referenced as custom beans or custom Camel processors
- adding the required Camel cxf endpoint options, such as the service class

![migrate-qk](images/migration-quarkus-soap.png?raw=true)

To configure CXF interceptors, a CxfConfigurer bean is needed and needs to be referenced in the Camel cxf endpoint.  
For more information about that, please have a look at:  
https://developers.redhat.com/articles/2023/03/28/how-configure-soap-web-services-apache-camel  

_Note: choice between Bean and URI format_  
As there is no beans.xml file in Quarkus, the cxfendpoint located in the Blueprint XML will have to be transformed into a Java object.  
And because of this little refactoring phase, a choice exists in  using the bean or the address URI format.  
We would prefer the use of the bean format for the server side, as Fuse 6 CXF applications already uses a similar format.
For the CXF client, it's however more intuitive to use the target service URL.  Another perspective to help make that choice it to say that it's better to have the full capabilities of a Java bean to confiure a SOAP service that we own (as we're exposing it), but we don't need any extra level of complexity to call a remote SOAP service whose confiuration is outside of our control.

### Testing the migrated app
mvn clean package  
mvn quarkus:dev  

curl 


## Readiness

### Unit Testing  
Both templates (springboot and quarkus) contain a structured placeholder to fill and run Camel 3 compatible unit tests.   

### Openshift  
Both templates (springboot and quarkus) contain important Openshift-related configuration such as:
- Runtime and Camel healthchecks  
- Prometheus-compatible runtime and Camel metrics 
- Automatic discovery of ConfigMap for the application.properties file
- Openshift deployments plugins  

