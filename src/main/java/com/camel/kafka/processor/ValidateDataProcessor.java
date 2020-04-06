package com.camel.kafka.processor;


import com.camel.kafka.domain.Item;
import com.camel.kafka.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Slf4j
public class ValidateDataProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        String message = (String) exchange.getIn().getBody();
        log.info("Message:{}",message);

        Item item = (Item) exchange.getIn().getBody();

        log.info("Item in  ValidateDataProcessor : {}" + item);

        if(ObjectUtils.isEmpty(item.getSku())){
            throw new DataException("Sku is null for " + item.getItemDescription());
        }
    }
}
