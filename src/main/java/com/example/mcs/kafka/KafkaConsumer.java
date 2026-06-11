package com.example.mcs.kafka;

import com.example.mcs.service.ConversionService;
import com.example.mcs.dto.FileEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final ConversionService conversionService;
    @KafkaListener(topics = "file-converter-input", groupId = "my_consumer")
    public void listen(FileEvent event){
    log.info("Received event: {}", event);
    conversionService.convert(event);
    }
}
