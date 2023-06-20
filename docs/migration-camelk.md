# Migrating toward Camel K Serverless

Link to the video blog:  


## Introduction
The templates are in templates/camelk.

### Source application
We'll start with the result of the migration of the claimDemo application (the REST API one from CXFRS) to Quarkus.  
The process would be exactly similar with the springboot version as the starting point.  

### Final migration step
What we look to here is a 2 steps process to migrate Fuse 6 application toward Camel K, going to Camel on Quarks or Camel on Springboot as an intermediate step.  

Technically, both steps could easily be performed as one.  
The reason why it's articulated here as a 2 steps process is because a Fuse 6 application is not likely to be run as a serverless application as such.  However, it's important that the migration results in a landscape of applications that are on the right track to embrace that new modern concept and go toward that direction.  
To proove that this benefit is real thanks to the use of the present material to complete the migration, here's a demo of how to complete this final step and migrate the result to a Serverless Function.

### Camel K
With Camel K, there is no longer a need to take care of the packaging and deployment of the application.  We can simply deliver the Camel route as the unique piece of the source code, and the framework perfoms all the steps required to build a Quarkus application and deploy it.  

Accepted Camel routes are, for example, those described in optimized XML, which is exatly what already have.  
Additionnal dependencies, such as custom Java beans, need to be explicitely listed, and the best way to do that is to use a Maven artifact repository, such as Nexus.  

![Camel K framework](images/camelk.png?raw=true)

## Migration process
The migration process involves the following steps:  
1. Prepare the Java dependency  
- Place the Java dependencies (custom beans and processors) in a separate maven project, such as the on located in templates/camelk/javadependency
- Adapt the groupId, artifactId and version in the pom.xml of the javadependency project if required  
- Adapt the URL of your Nexus repository in the 'distribution Management' section  
- Ensure your local maven settings.xml contains an associated server entry with the right credentials
- Deploy the dependency to Nexus  
_mvn deploy_ 
2. Prepare the Camel K environment
A settings.xml containing a point to your Nexus is required by Camel K
- adapt the URL of your Nexus repository in the settings.xml file
- create a ConfigMap to hold this settings.xml  
cf. scripts/create-maven-cm script 
- Configure Camel K to reference that ConfigMap  
cf. scripts/register-maven-cm script
- instantiate your custom beans within the CamelBeans.java file, all annotated with the BindToRegistry annotation  
3. Deploy with Camel K  
cf. scripts/kamel-run.sh
