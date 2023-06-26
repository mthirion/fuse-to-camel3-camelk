
kamel run --name camelk-migration \
   --open-api file:<path>/openapi.yaml \
   -d camel-jackson -d camel-servlet \
   -d mvn:com.redhat.appfoundation.camelk.dependency:java-dependency:1.0.0 \
   --config file:<path>/src/main/resources/application.properties \
   --build-property file:$<path>/src/main/resources/application.properties \
<path>/src/main/resources/camel/MyQuarkusRoute.xml <path>/BeansBinding.java


kamel run --name camelk-migration -d mvn:com.redhat.appfoundation.camelk.dependency:java-dependency:1.0.0 --open-api file:<path>/openapi.yaml -d camel-jackson -d camel-servlet --config file:<path>/src/main/resources/application.properties --build-property file:$<path>/src/main/resources/application.properties <path>/src/main/resources/camel/MyQuarkusRoute.xml <path>/BeansBinding.java
