# Migrating an activemq route to Artemis
Link to the video blog:


## Introduction
The templates in used are those in templates/amq.  

### Available messaging engines
[Messaging engines](amq-distributions.md)  

### Fuse 6 source application


###  Structure of a Fuse 6 amq application
Leveraging OSGI and Blueprint, the Camel application is constructed from 3 major elements:
- OSGI-INF/Blueprint.xml  
This is the main file.  
It contains the Camel route that
  - has an amq:// endpoint that points to or discovers an activemq broker 
  - references Java beans declared in separate files
- Java files, and among them:
  - the java beans referenced in the Camel route
  - other libraries, such as data handling components
- Fabric8 pid file  
It contains external configuration.  


## Migration to Springboot
In this version of the demo, the Fuse 6 application is going to be migrated toward Camel 3.20 on Springboot 2.7.12.  

The Camel on Springboot templates is located in templates/amq.  

The Camel on Springboot template uses the below placeholders:  
- SpringBootApplication.java  
It contains the bootstrap class.   
- camel/route(s).xml  
To contain the Camel XML route(s) in NIO XML format.  
The routes will be automatically discovered by the Springboot runtime.  
This specific XML format can only contain Camel Routes, so the other part of the Blueprint XML (beans...) will go somewhere else.  
- spring/beans.xml  
This is where the custom beans references will be listed.  It's easier to migrate the bean definition directly in XML as they are already defined in XML in the Fuse 6 source application.
- application.properties  
This will be the unique configuration file.  


### Target protocol and scheme for Springboot
The target protocols should be either core or amqp.  Indeed, the openwire protocol is not recommended as it's dropped from the list of supported protocols.

The amqp library offers a simplified configuration, entirely contained in the application.properties file.  
The core protocol, however, requires the definition of a Bean of type JMSComponent, that will contain the details of the ConnectionFactory.  

The material relies on the bean definition, as this is closer to what should be in most of the Fuse 6 applications.  Indeed, a good thing with the bean method is that it allows customizing the endpoint scheme.  For example, to keep endpoint prefixes such as  'fabric://' simply needs the bean method named accordingly.  

The template already contains a bean definition, and holds placeholders in the application.properties file to allow configuring the bean without writing code.  

### Springboot migration process
The process is short, as it only consists in copying the right information about the remote AMQ Broker to the application.properties file.

### Check the migrated app
mvn clean package  
mvn spring-boot:run

## Migration to Quarkus
In this version of the demo, the Fuse 6 application is going to be migrated toward Camel Quarkus 2.13.  

### Target protocol and scheme for Springboot
Currently the only supported protocol provider for Quarkus is amqp.

### Springboot migration process
The configuration is entirely contained within the application.properties file, so the migration only consists in copying the right information about the remote AMQ Broker to the application.properties file.

### Check the migrated app
mvn clean package  
mvn quarkus:dev  

## Readiness

### Unit Testing  
Both templates (springboot and quarkus) contain a structured placeholder to fill and run Camel 3 compatible unit tests.  

### Openshift  
Both templates (springboot and quarkus) contain important Openshift-related configuration such as:
- Runtime and Camel healthchecks  
- Prometheus-compatible runtime and Camel metrics 
- Automatic discovery of ConfigMap for the application.properties file
- Openshift deployments plugins  
