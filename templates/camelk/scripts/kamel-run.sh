APP_ROOT=..



APP_SOURCE=$APP_ROOT/example-quarkus-app

kamel run --name camelk-migration \
   --open-api file:$APP_ROOT/openapi.yaml \
   -d camel-jackson -d camel-servlet \
   -d mvn:com.redhat.appfoundation.camelk.dependency:java-dependency:1.0.0 \
   --config file:$APP_SOURCE/src/main/resources/application.properties \
   --build-property file:$APP_SOURCE/src/main/resources/application.properties \
$APP_SOURCE/src/main/resources/camel/MyQuarkusRoute.xml $APP_ROOT/CamelBeans.java



# ===========
# Explanation
# ===========

# --open-api				: auto-generate the Camel REST DSL code, bound to the Camel Routes by 'operationId'
#  camel-jackson & camel-servlet 	: dependency needed bythe REST DSL

# CamelBeans				: file to bind Java dependency into Camel

