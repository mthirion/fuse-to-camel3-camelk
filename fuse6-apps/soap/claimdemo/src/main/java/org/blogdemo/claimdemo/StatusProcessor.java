package org.blogdemo.claimdemo;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import learn.cxf.*;

public class StatusProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {

            Status status = new Status();

            StatusResponse response = new StatusResponse();
            response.setCustid(status.getGetbyid());
            
            response.setPolno("A123456789");
            response.setClaimno("34567789");
            response.setStatus("OK");
            response.setFuseversion((String)exchange.getIn().getHeader("fuseversion"));
            
            exchange.getIn().setBody(response); 
        }

}
