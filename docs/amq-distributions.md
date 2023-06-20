# Messaging engines distributions

## Fuse 6, AMQ 6, Fuse 7 and AMQ 7
Fuse 6 used to embed an Apache activeMQ 5 server.  The corresponding standalone Red Hat product was Red Hat AMQ 6.  
Like for Fuse, Red Hat upgraded AMQ to v7, following a major change in the upstream community with the arrival of Apache activeMQ Artemis.  From that version, Fuse 7 and AMQ 7 was disociated and one was no longer included into the other.  

Therefore, the migration of a Fuse 6 application relying on the embedded AMQ will have to be accompanied by a migration of the AMQ messaging engine itself.  
At the end, the migrated Camel integration will have to interact with an external ActiveMQ Artemis broker.

### Apache activeMQ and Apache activeMQ Artemis
Apache ActiveMQ Artemis is pretty much like a merge of the powerful Hornetq engine with the flexible Apache activeMQ interface.  
As a result, the multi-protocols activeMQ Artemis arrives with:
- openwire; the protocol used by activeMQ v5, for backward compatibility
- hornetq; for comaptibility with former HornetQ systems
- core; the new, native protocol
- amqp; more recent messaging protocol, aimed at extending the compatibility of messaging systems to non-java / non JMS clients.

![activemq and artemis](images/amq-56.png?raw=true)

### Clients library for Red Hat AMQ 7
For the compatibility of the clients, what we'll need to have a look at is the Red Hat AMQ 7 clients library.  This library is a set of drivers for different languages and runtimes, at the moment all grouped under the version 2.11 of the clients library. 

With Springboot as the target, the drivers for each protocols are as follows:  
- 'openwire' is supported through 'org.apache.activemq::activemq-client'  
- 'core' is supported through 'org.apache.activemq::artemis-jms-client'
- 'amqp' is supported through 'org.apache.qpid::qpid-jms-client'

The latter (and only the latter) can be made available via the springboot specific dependency 'amqp-10-jms-spring-boot-starter' and it will benefits from auto-configuration properties such as :  
* amqphub.amqp10jms.remoteUrl
* amqphub.amqp10jms.username
* amqphub.amqp10jms.password

![amq clients](images/amq-clients.png?raw=true)

With Quarkus as the target, only the amqp driver is currently supported, via the 'org.amqphub.quarkus:quarkus-qpid-jms' quarkus extension.

### Camel 3 AMQ endpoints
The AMQ drivers listed above are used behind the scene by the Apache Camel framework as simple protocol providers.  
On top of that, Apache Camel exposes camel endpoints via the following components:
- camel-activemq, providing 'activemq://' URI endpoints, and also available through a springboot specific starter named 'spring-boot-starter-ativemq'  
- camel-jms-starter or camel-quarkus-jms, providing 'jms://' URI endpoints
- camel-amqp-starter or camel-quarkus-amqp, providing 'amqp://' URI endpoints

![camel amq](images/amq-camel.png?raw=true)