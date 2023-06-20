package com.redhat.appfoundations.camel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.apache.camel.CamelContext;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyFactory;
import org.apache.camel.component.micrometer.messagehistory.MicrometerMessageHistoryFactory;

import org.springframework.beans.factory.annotation.Value;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.activemq.artemis.jms.client.ActiveMQJMSConnectionFactory;

@Configuration
public class MyAppConfig {

    @Value("${app.broker.url}")
    String brokerUrl;
    @Value("${app.broker.username}")
    String username;
    @Value("${app.broker.password}")
    String password;        

    /**
     * Config of the Camel Context
     */
    @Bean
    public CamelContextConfiguration camelContextConfiguration() {

        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext camelContext) {
                camelContext.addRoutePolicyFactory(new MicrometerRoutePolicyFactory());
                //camelContext.setMessageHistoryFactory(new MicrometerMessageHistoryFactory());
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {

            }
        };
    }  

    // Carefull: the name "jms()" must correspond to the camel uri scheme --> jms://queue:...
    @Bean
    public JmsComponent jms() throws Exception {
        ActiveMQJMSConnectionFactory cf = new ActiveMQJMSConnectionFactory();
        cf.setBrokerURL(brokerUrl);
        cf.setUser(username);
        cf.setPassword(password);

        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(cf);
        return jms;
    }
}
