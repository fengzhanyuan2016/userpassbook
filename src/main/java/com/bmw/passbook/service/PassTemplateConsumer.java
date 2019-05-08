package com.bmw.passbook.service;

import com.alibaba.fastjson.JSON;
import com.bmw.passbook.constant.Constants;
import com.bmw.passbook.dto.PassTemplate;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

//消费优惠卷
@Slf4j
@Component
public class PassTemplateConsumer {
    private final IHBasePassService _service;
    public PassTemplateConsumer(IHBasePassService service){
        this._service = service;
    }
    @KafkaListener(topics = {Constants.TEMPLATE_TOPIC})
    public void receive(@Payload String passtemplate, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,@Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        log.info("Receive PassTemplate:{} ",passtemplate);
        try{
            var pt = JSON.parseObject(passtemplate, PassTemplate.class);
            log.info("DropPassTemplateToHBase:{}",_service.dropPassTemplateToHBase(pt));

        }catch (Exception ex){
            log.error("parse passtemplate error:{}",ex.getMessage());
            return;
        }
    }
}
