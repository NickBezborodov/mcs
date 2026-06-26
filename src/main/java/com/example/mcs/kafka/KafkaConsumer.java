package com.example.mcs.kafka;

import com.example.mcs.service.ConversionService;
import com.example.mcs.dto.FileEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final ConversionService conversionService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${kafka.topics.input}", groupId = "converter-group")
    public void listen(String message) {
        log.info("Received raw message: {}", message);
        try {
            FileEvent event = objectMapper.readValue(message, FileEvent.class);
            log.info("Deserialized event: {}", event);
            conversionService.convert(event);
        } catch (Exception e) {
            log.error("Failed to deserialize message: {}", e);
        }
    }
}


//1. Spring подписывается на топик "file-converter-input"
//        2. В топик приходит новое сообщение
//3. Spring вызывает listen() и передаёт JSON-строку
//4. Логируем: "Received raw message: {...}"
//        5. Пытаемся превратить JSON в FileEvent через ObjectMapper
//   └── Если успех → логируем "Deserialized event"
//        └── Если ошибка → логируем "Failed to deserialize"
//        6. Если десериализация успешна → conversionService.convert(event)
//7. ConversionService делает всю работу (скачивает файл, конвертирует, загружает)
//8. Метод завершается, consumer готов к следующему сообщению