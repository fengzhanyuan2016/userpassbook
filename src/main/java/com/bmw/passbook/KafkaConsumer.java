package com.bmw.passbook;

import com.bmw.passbook.dto.PassTemplate;
import com.bmw.passbook.service.IHBasePassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.var;
import com.alibaba.fastjson.JSON;
@Slf4j
@Component
public class KafkaConsumer {
    private final IHBasePassService _service;
    public KafkaConsumer(IHBasePassService service){
        this._service = service;
    }
    @KafkaListener(topics = {"merchants-template"})
    public void receive(String message){
        System.out.println("消费的消息："+message);
        log.info("消费的消息："+message);
        try{
            var pt = JSON.parseObject(message, PassTemplate.class);
            log.info("DropPassTemplateToHBase:{}",_service.dropPassTemplateToHBase(pt));
        }catch (Exception ex){
            log.error("parse passtemplate error:{}",ex.getMessage());
            return;
        }
    }

}
