package com.example.mcs.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.topics.output}")
    private String outputTopic;

    public void sendMessage(String message) {
        kafkaTemplate.send(outputTopic, message);
    }
}
