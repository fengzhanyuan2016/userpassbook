package com.bmw.passbook;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class PassbookApplication {
	public static void main(String[] args) {
		SpringApplication.run(PassbookApplication.class, args);
	}
	
}
