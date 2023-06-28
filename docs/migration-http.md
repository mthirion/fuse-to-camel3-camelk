# Migrating an http route
Link to the video blog:

## Introduction
There is a change only for inbound routes.  
Outbound routes were leveraging the camel http4 component, which simply has been renamed camel 'http', with no significant change in behaviour.  

The templates to migrate inbound http routes are in templates/jetty.  

### Camel PlatformHttp
Fuse 6 embedded an OSGI version of the Jetty web server that was used by Camel, with the camel-jetty component, to expose inbound http endpoints.  

Following the same approch as the one already set for AMQ, with a separation of concerns between the Camel AMQ endpoints and the underlying protocol providers (JMS/amqp), Camel 3 introduces a similar separation of concern between the Camel HTTP interface and the underlying HTTP implementation provider.  
The generic HTTP interface component is "platform-http".  
The platform-http component publishes Camel endpoints with schemes like the following:  
_"platform-http://_path-to-the-resource__   

## Fuse 6 source application

## Structure of a Fuse 6 Jetty application
Leveraging OSGI and Blueprint, the Camel application is constructed from 3 major elements:
- OSGI-INF/Blueprint.xml  
This is the main file.  
It contains the Camel route that
  - starts with a jetty:// inbound endpoint
  - references Java beans declared in separate files
- Java files, and among them:
  - the java beans referenced in the Camel routes
  - other libraries, such as data handling components
- Fabric8 pid file  
This contains configuration.  

## Migration to Springboot
In this version of the demo, the Fuse 6 application is going to be migrated toward Camel 3.18 on Springboot 2.7.6.  

The Camel on Springboot templates is located in template/jetty.  

It uses the below elements:  
- SpringBootApplication.java  
It contains the bootstrap class.   
- camel/route(s).xml  
To contain the Camel XML route(s) in NIO XML format.  
The routes will be automatically discovered by the Springboot runtime.  
This specific XML format can only contain Camel Routes, so the other part of the Blueprint XML (beans...) will go somewhere else.    
- spring/beans.xml  
This is where the beans references can be listed.   
- application.properties  
This is the main configuration file.  

### Springboot migration process
The jetty endpoint simply has to be replaced by the platform-http one.  
The pom.xml contains the approptiate dependencies to import the platform-http camel component, hooking to camel-servlet as the implementation provider, itself using the configuration of the spring-boot-starter-web server.

### Check the migrated app

mvn clean package  
mvn spring-boot:run

curl  

## Migration to Quarkus
In this version of the demo, the Fuse 6 application is going to be migrated toward Camel on Quarkus 2.13.  

The jetty endpoint simply has to be replaced by the platform-http one.  
The pom.xml contains the appropriate dependencies to import the platform-http camel extension, which has a transitive dependency to camel-platform-http-vertx, itself relying on the Quarkus http vertx web server implementation.

### Check the migrated app
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

