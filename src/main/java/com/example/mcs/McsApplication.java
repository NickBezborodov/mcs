package com.example.mcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class McsApplication {

	public static void main(String[] args) {
		SpringApplication.run(McsApplication.class, args);
	}

	@Bean
	public CommandLineRunner testKafka(KafkaTemplate<String, String> kafkaTemplate) {
		return args -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			// Убираем поле type - оно опционально
			kafkaTemplate.send("file-converter-input",
					"{\"filePath\": \"test.txt\", \"format\": \"txt\"}");
			System.out.println(">>> TEST MESSAGE SENT <<<");
		};
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}