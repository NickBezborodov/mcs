package com.example.mcs.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileEvent {
    private String filePath;
    private String format;
    private EventType type;
}
