## Migrating an inbound REST endpoint from CXFRS

Link to the video blog:

## Introduction

The templates in used are those in templates/rest.

### Fuse 6 source application

We'll use the claimDemo application, sourced from our Red Hat Learning System.  
It runs on a Fabric8 server, and exposes an API at:  
http://localhost:8182/cxf/status/status/custId/123

To test the Fuse 6 application:  
docker pull weimeilin/fusefabric:naenablement  
docker run -it -p 8181:8181 -p 8182:8182 -p 8184:8184 weimeilin/fusefabric:naenablement

curl http://localhost:8182/cxf/status/status/custId/123

_Note_  
The original web application had a double '/status/' in its URI, which is considered a mistake and will be corrected as an added value to the migration.

### Structure of a Fuse 6 cxfrs application

Leveraging OSGI and Blueprint, the Camel application is constructed from 3 major elements:

*   OSGI-INF/Blueprint.xml  
    This is the main file.  
    It contains the Camel route that
    *   starts with a cxfrs endpoint whose definition is in a cxf
    *   references Java beans declared in separate files
    *   has a CXF server section which points to a Java Service class that defines the Rest API
*   Java files, and among them:
    *   the java beans referenced in the route (Camel processors)
    *   the cxsfr Service class
    *   other libraries, such as Request and Response object
*   Fabric8 pid files (or simple Karaf configuration files)  
    It contains external configuration.

![blueprint](images/cxfrs-blueprint.png?raw=true)

_Note about the CXF engine_

The CXF web server engine had a fixed /cxf context path.  
And the Fabric8 platform exposed the web services behind the port 8182 by default.  
To keep everything similar for local testing, appropriate properties have been added to the application.properties file of the templates.  
Notice that, with Openshift as the target deployment platform, the exposed port should become 8080 as it is the convention of the platform for HTTP-based microservices.

The cxf engine used method names to identify the java operation to perform. An 'operationName' parameter had to be added to the header by the proxy client. Thus, the Camel routes relying on cxfrs usually contained a 'choice' element used to make a routing decision based on the value of that header. This doesn't exist anymore with the new REST implementation. However, to minimize the impact on the client, that part of the route could be kept. Actions need to be taken, however, to enable those routes to new clients that would not fill that parameter anymore.

### Camel 4 and the REST DSL

With Camel 4, there is no cxfrs anymore in the supported CXF framework. Instead, ReST APIs are represented by a dedicated Camel REST endpoint. In addition, Camel provides a separation of concerns between the definition of the REST API and its imeplmentation as a Camel route. For maximm flexibility, both elements can be stored in separate files and be written in different languages  
Finaly, the API definition file does not even have to be written by hand, but can be generated automatically from an openAPI spec.  
https://github.com/apache/camel/tree/main/tooling/maven/camel-restdsl-openapi-plugin

#### OpenAPI spec as a prerequisite for the migration

To leverage the auto-generation of the REST interfaces, an openAPI spec document is needed. Thus, having openAPI spec for all REST API to migrate can be seen as a prerequisite for the migration. It's anyway a best practice for better general governance, so adding this activity as a preparation step for the migration will only be beneficial.  
Apicurio API Designer and Apicurio Service Registry are tools that can be of great help in this activity.

## Migration to Quarkus

In this version of the demo, the Fuse 6 application is going to be migrated toward Camel extension for Quarkus 4.x.

Compared to the Springboot migration described above, the Camel on Quarkus template still relies on the route(s).xml, rest.xml and application.properties file, but releases the SpringBootApplication.java class as well as the beans.xml file. 

To simplify the migration path from older vesion of Camel, there is now support for Spring-like beans declaration in the XML-IO-DSL.  This removes the need to complete the souce code with Quarkus annotations like in a migration toward Camel v3. 

![quarkus](images/rest-quarkus.png?raw=true)

### Quarkus migration process

The migration process involves (in any order):

*   moving the fabric8 pid properties to the application.properties file
*   moving the java objects/packages, excluding the Service interface to the src/main/java directory
*   moving the whole camel route section to the route.xml file (within the \<routes> section)
*   moving the xml bean declaration to the same route.xml file (this time inline as \<bean>)
    *   Notice that the declaration uses now the atributes “name” instead of id and “type” instead of class
*   moving the openapi.yaml document to src/spec so that it can be found by the camel-restdsl-openapi plugin
*   generating the REST API definition from the OpenAPI spec with the plugin, using the command ‘mvn camel-restdsl-openapi:generate-xml’
*   adjusting the 'from' part of the route (replacing ‘cxfrs://bean…’ by ‘direct:\<operationid>…’) based on the generated API definition (openAPI spec's operationID)

![migrate-qk](images/migration-quarkus-rest.png?raw=true)

#### Testing the migrated app

mvn clean package  
mvn quarkus:dev

curl http://localhost:8182/cxf/status/custId/789

## Readiness

### Unit Testing

Both templates (springboot and quarkus) contain a structured placeholder to fill and run Camel 4 compatible unit tests.

### Openshift

Both templates (springboot and quarkus) contain important Openshift-related configuration such as:

*   Runtime and Camel healthchecks
*   Prometheus-compatible runtime and Camel metrics
*   Automatic discovery of ConfigMap for the application.properties file
*   Openshift deployments plugins

Deploy the springboot version to Openshift:  
_mvn install -Popenshift_

Deploy the quarkus to Openshift:  
\_mvn clean package -Popenshift -Dquarkus.kubernetes.deploy=true -Dquarkus.kubernetes-client.trust-certs=true -Dquarkus.openshift.route.expose=true