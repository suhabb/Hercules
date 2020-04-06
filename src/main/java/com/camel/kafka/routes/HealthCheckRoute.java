package com.camel.kafka.routes;


import com.camel.kafka.processor.HealthCheckProcessor;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class HealthCheckRoute extends RouteBuilder{


    @Autowired
    HealthCheckProcessor healthCheckProcessor;

    Predicate isNotMock =  header("env").isNotEqualTo("mock");


    @Override
    public void configure() throws Exception {
        //healthRoute: timer:health?period=10s
            from("{{healthRoute}}")
                .routeId("healthRoute")
                .choice() // Content based EIP
                    .when(isNotMock) // not mock check health
                        .pollEnrich("http://localhost:8080/health")//checks health every 10s
                    .end()
                .process(healthCheckProcessor)
                .choice()
                    .when(header("error").isEqualTo(true))
                .end();



    }
}
