package com.example.mcs.converter;

import com.example.mcs.exception.ConversionException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConverterFactory {
    private final Map<String, FileConverter> converters;

    public ConverterFactory(List<FileConverter> converterList) {
        converters = converterList.stream()
                .collect(Collectors.toMap(FileConverter::getFormat, c -> c));
    }

    public FileConverter getConverter(String format) {
        FileConverter converter = converters.get(format.toLowerCase());
        if (converter == null) {
            throw new ConversionException("No converter for format: " + format, null);
        }
        return converter;
    }
}