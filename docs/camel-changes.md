# Changes in Camel components from v2 to v3/v4
## Renaming
Some components have been renamed for standardisation purpose while their behaviour hasn't changed much.
- http4 -> http
- netty4 -> netty
- file2 -> file
- hdfs2 -> hdfs
- quartz2 -> quartz
- mina2 -> mina

## Deprecation
Some components or plugins have been deprecated and a new implementation should be considered: 
- swagger -> openAPI v3
- xmljson -> JAXB; Json Jackson & Jackson XML
- xstream -> JAXB
- openTracing -> openTelemetry
- fabric8 maven plugin -> JKube maven plugin

## Architectural changes
Here's a short list of architectural things to consider.  

As Openshift replaces Fabric8 as the runtime platform and is also the recommended target for the migration.
This leads to several architectural considerations:  
- Fabric8 service discovery will rely on Kubernets Service Discovery  
- Bundle configuration will leverage Kubernetes ConfigMaps and Secrets  
- file-based route should look at s3 technology.  If a standard filesystem remains in use, the resulting Spring Boot or Quarkus applications should probably be deployed on standard RHEL VM rather than a cloud platform
- secured TLS (https) inbound traffic should be delegated to the Openshift Router, and an SSL offloading should happen at that level
- Hystrix and circuit breaker features should be delegated to Openshift serviceMesh

