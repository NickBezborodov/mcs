package com.example.mcs.service;

import com.example.mcs.AbstractIT;
import com.example.mcs.dto.FileEvent;
import com.example.mcs.repository.InboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.mcs.dto.EventType;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Testcontainers
class ConversionServiceTest extends AbstractIT {

    @Value("${kafka.topics.input}")
    private String inputTopic;

    @Value("${kafka.topics.output}")
    private String outputTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InboxRepository inboxRepository;

    @Test
    void shouldConvertTxtToPdf() throws Exception {
        // Given
        FileEvent event = new FileEvent("test-file.txt", "txt", EventType.SUCCESS);
        String message = objectMapper.writeValueAsString(event);

        // When
        kafkaTemplate.send(inputTopic, message);

        Thread.sleep(5000); // подождать обработку
        assertTrue(inboxRepository.existsByMessageKey("test-file.txt"));
    }
}
