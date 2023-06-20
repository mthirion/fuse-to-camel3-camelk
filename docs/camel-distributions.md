# Apache Camel distributions

## What are Apache Camel, Fuse x and the Red Hat Buid of Apache Camel ?
Apache Camel is an Open Source integration framework, that can be seen as a kind of direct implementation of the standard EIP in Java.  

JBoss Fuse 6 was an Apache Camel based integration solution.  
The Apache Camel integrations could be described in either Java or XML, the XML format itself being compliant to either a Spring XML or the Blueprint XML schema.  
The integrations ran on OSGI or on JEE.  
Apache Karaf was providing the support for OSGI while JBoss AS 7 (Red Hat EAP 6) was providing the JEE support.  

Red Hat Fuse 7 was the Apache Camel based integration solution mainly designed for Kubernetes.  
The integrations could still be described in the same formats and support was still provided for Karaf and EAP via specific docker images.
However, the new runtime of choice was springboot and Apache Camel integrations could fit with it in either Java or Spring XML.  

From a containerized version, Apache Camel continued its journey to get closer to Serverless and Functions-as-a-Service solutions.  
With a complete rewrite, the framework switched from Apache Camel v2 to Apache Camel v3 (and then v4) and adopted a new modern runtime of choice: Quarkus.  
From a Red Hat point of view, rather than continuing with the Fuse 8, that integration solution was renamed the Red Hat Build of Apache Camel, with support for springboot and quarkus (not any longer for Karaf and EAP) but also with a new fully serverless Camel K distribution.
The new version of Apache Camel allows application to be written in Java and XML, but also in IO XML (optimized language for serverless function), yaml, groovy...