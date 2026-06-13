package com.example.mcs.service.serviceImpl;

import com.example.mcs.converter.ConverterFactory;
import com.example.mcs.converter.FileConverter;
import com.example.mcs.dto.EventType;
import com.example.mcs.dto.FileEvent;
import com.example.mcs.kafka.KafkaProducer;
import com.example.mcs.service.ConversionService;
import com.example.mcs.service.InboxService;
import com.example.mcs.service.MinioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversionServiceImpl implements ConversionService {

    private final MinioService minioService;
    private final KafkaProducer kafkaProducer;
    private final ConverterFactory converterFactory;
    private final InboxService inboxService;

    public void convert(FileEvent event) {
        if (inboxService.isProcessed(event.getFilePath())) {
            log.info("Already processed: {}", event.getFilePath());
            return;
        }
        try {
            byte[] fileBytes = minioService.downloadFile(event.getFilePath());

            FileConverter converter = converterFactory.getConverter(event.getFormat());
            byte[] pdfBytes = converter.convert(fileBytes);

            String pdfPath = event.getFilePath() + ".pdf";
            minioService.uploadFile(pdfPath, pdfBytes);

            kafkaProducer.sendMessage(new FileEvent(pdfPath, null, EventType.SUCCESS));
            inboxService.markProcessed(event.getFilePath());
        } catch (Exception e) {
            log.error("Conversion failed: {}", e.getMessage());
            try {
                kafkaProducer.sendMessage(new FileEvent(event.getFilePath(), null, EventType.ERROR));
            } catch (JsonProcessingException ex) {
                log.error("Conversion failed: {}", ex.getMessage());
            }
        }
    }
}
