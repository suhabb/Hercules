package com.camel.kafka;

import com.camel.kafka.domain.Item;
import com.camel.kafka.routes.KafkaRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("mock")
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisableJmx(true)
public class KafkaRouteMockTest extends CamelTestSupport{


    @Autowired
    private CamelContext context;

    @Autowired
    protected CamelContext createCamelContext() {
        return context;
    }

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private ConsumerTemplate consumerTemplate;

    @Override
    protected RouteBuilder createRouteBuilder(){
        return new KafkaRoute();
    }

    @Autowired
    Environment environment;

    @Before
    public void setUp(){

    }

    @Test
    public void unMarshalTest(){
        String input = "{\"transactionType\":\"ADD\", \"sku\":\"100\", \"itemDescription\":\"SamsungTV\", \"price\":\"500.00\"}";

        String output = "INSERT INTO ITEMS(SKU, ITEM_DESCRIPTION,PRICE) VALUES ('100','SamsungTV',500.00)";
        String actualValue  = (String) producerTemplate.requestBodyAndHeader(environment.getProperty("fromRoute"), input,"env","mock");

        System.out.println("Item : " + actualValue);
        assertEquals(output, actualValue);

    }

    @Test(expected = CamelExecutionException.class)
    public void unMarshalTest_error(){
        String input = "{\"transactionType\":\"ADD\", \"sku\":\"\", \"itemDescription\":\"SamsungTV\", \"price\":\"500.00\"}";

        Item item = (Item) producerTemplate.requestBodyAndHeader(environment.getProperty("fromRoute"), input,"env","mock");


    }




}
