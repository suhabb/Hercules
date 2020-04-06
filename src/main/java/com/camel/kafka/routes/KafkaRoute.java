package com.camel.kafka.routes;

import com.camel.kafka.domain.Item;
import com.camel.kafka.exception.DataException;
import com.camel.kafka.processor.BuildSQLProcessor;
import com.camel.kafka.processor.ValidateDataProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class KafkaRoute extends RouteBuilder {

    @Autowired
    Environment environment;

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Autowired
    ValidateDataProcessor mailProcessor;

    @Autowired
    BuildSQLProcessor validateDataProcessor;

    @Autowired
    BuildSQLProcessor buildSQLProcessor;


    @Override
    public void configure() throws Exception {

        Predicate isNotMock = header("env").isNotEqualTo("mock");

        GsonDataFormat itemFormat = new GsonDataFormat(Item.class);

        onException(PSQLException.class).log(LoggingLevel.ERROR, "PSQLException in the route ${body}")
                .maximumRedeliveries(3).redeliveryDelay(3000).backOffMultiplier(2).retryAttemptedLogLevel(LoggingLevel.ERROR);

        onException(DataException.class, RuntimeException.class).log(LoggingLevel.ERROR, "DataException in the route ${body}")
                .choice()
                .when(isNotMock)
                .process(mailProcessor)
                .end()
                .to("{{errorRoute}}");


        from("{{fromRoute}}")
                .log("Read Message from Kafka ${body}")
                .unmarshal(itemFormat)
                .log("Unmarshalled message is ${body}")
                //.process(validateDataProcessor)
                .log("Read validated ${body}")
                .process(buildSQLProcessor)
                .log("Read build SQL query ${body}")
                .to("{{toRoute}}")
                .to("{{selectNode}}")
                .log("Read item from DB is ${body}");

    }
}
