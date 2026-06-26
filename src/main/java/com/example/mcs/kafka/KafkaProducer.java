package com.example.mcs.kafka;

import com.example.mcs.dto.FileEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Value("${kafka.topics.output}")
    private String outputTopic;

    public void sendMessage(FileEvent event) throws JsonProcessingException {
        kafkaTemplate.send(outputTopic, objectMapper.writeValueAsString(event));
    }
}
