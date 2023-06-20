# Migrating Camel 2 to Camel 3 on either Springboot or Quarkus
## Overview
This repository contains support material aimed at providing an accelerated path to migrating applications from Camel 2 to Camel 3 or Camel 4.  
More concretely, the material should help people migrating any kind of Fuse 6 or Fuse 7 applications to the latest Red Hat Build of Apache Camel.  

[What are Camel, Fuse x, and the Red Hat Build of Camel ?](docs/camel-distributions.md)  

## Finding your migration path
The present material can be used to migrate any Fuse 6 or 7 distribution to the Red Hat Build of Apache Camel.  
The demos will however focus on migrating a Fuse 6 application, and more concretely a Camel v2.17 one, in Blueprint XML format, running on Karaf 2.  The XML format will be converted to the optimized IO XML one.   
This path has been chosen for the demonstration because it can be considered the longest one.  Applications running on Fuse 7, no matter which distribution, could use the exact same approach and will bnefit from a smallest migration effort. 

In general, the following migration strategy should be considered:  
Language  
- If you have integrations in Java, keep them in Java DSL 
- If you have integrations in XML, migrate to the optimized IO XML

Runtime  
- If you are already on Fuse 7 springboot, migrate toward springboot  
- In any other cases, you could target Quarkus

Target  
Probably, the target should remain a "standard" container application, ready for Serverless but not mandatorily a Function.  
Thus, the demos will showcase several migrations to "plain" springboot or quarkus applications, and there will be one extra demo to showcase a migration from that result to Camel K.

## How can this material help ?
A Fuse / Apache Camel based application can be seen as a composition of multiple layers: the Apache Camel layer, the runtime layer (i.e. OSGI) and the platform layer (i.e. Karaf/Fabric8).  

![blueprint](docs/images/runtimes-0.png?raw=true)

Theoretically, the migration would involve changes in all layers.  
The material here consists in ready to use templates that are already perfectly suitable for the target so that the migration effort will be reduced to a small subset of changes on the Apache Camel layers.  
The templates are Maven artifacts, preconfigured with all required dependencies, and holding all the needed placeholders to migrate code and configuration in a timely fashion.  

![blueprint](docs/images/runtimes-1.png?raw=true)

Thus, the migration effort will be concentrated in named and deprecated components.  
[Changes in components](docs/camel-changes.md)  

### Decription of the material
The main content consists of recorded demos showing the detailed procedure to migrate an example of Fuse 6 applications leveraging the templates described above.  

The templates are located in the /templates subdirectories, and are sorted by endpoint type ant target runtimes.  

There is a decated demo for each of the most used Camel endpoints:  
 - rest
 - soap
 - amq (consumer and providers)
 - http (inbound and outbound)  

In the videos, the migration takes a Fuse v6 application and migrate it toward the 2 target runtimes at the same time, in parallel.


## Migrations in action
[Migrating a REST endpoint from CXFRS](docs/migration-rest.md)  
[Migrating SOAP clients and servers from CXF](docs/migration-soap.md)  
[Migrating AMQ endpoints to ActiveMQ Artemis](docs/migration-amq.md)  
[Migrating HTTP endpoints](docs/migration-http.md)  
[Migrating to Camel K](docs/migration-camelk.md)  
