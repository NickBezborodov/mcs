package com.example.mcs.converter;

import com.example.mcs.exception.ConversionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConverterFactory {
    private final List<FileConverter> converters;

    public FileConverter getConverter(String format) {
        for (FileConverter converter : converters) {
            if (converter.getFormat().equalsIgnoreCase(format)) {
                return converter;
            }
        }
        throw new ConversionException("No converter for format: " + format, null);
    }
}